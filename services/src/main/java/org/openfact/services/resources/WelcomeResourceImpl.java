package org.openfact.services.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.openfact.Config;
import org.openfact.common.util.MimeTypeUtil;
import org.openfact.models.BrowserSecurityHeaders;
import org.openfact.models.OpenfactModelUtils;
import org.openfact.models.OpenfactSession;
import org.openfact.services.managers.ApplianceBootstrap;
import org.openfact.services.util.CacheControlUtil;
import org.openfact.services.util.CookieHelper;
import org.openfact.theme.BrowserSecurityHeaderSetup;
import org.openfact.theme.FreeMarkerUtil;
import org.openfact.theme.Theme;
import org.openfact.theme.ThemeProvider;

public class WelcomeResourceImpl implements WelcomeResource {

    private static final Logger logger = Logger.getLogger(WelcomeResourceImpl.class);

    private static final String OPENFACT_STATE_CHECKER = "OPENFACT_STATE_CHECKER";

    private boolean bootstrap;

    @Context
    protected HttpHeaders headers;

    @Context
    private UriInfo uriInfo;

    @Context
    private OpenfactSession session;

    public WelcomeResourceImpl(boolean bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public Response getWelcomePage() throws URISyntaxException {
        checkBootstrap();

        String requestUri = uriInfo.getRequestUri().toString();
        if (!requestUri.endsWith("/")) {
            return Response.seeOther(new URI(requestUri + "/")).build();
        } else {
            return createWelcomePage(null, null);
        }
    }

    @Override
    public Response createUser(MultivaluedMap<String, String> formData) {
        checkBootstrap();

        if (!bootstrap) {
            return createWelcomePage(null, null);
        } else {
            if (!isLocal()) {
                logger.error("Rejected non-local attempt to create initial user from " + session.getContext().getConnection().getRemoteAddr());                
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }

            String cookieStateChecker = getCsrfCookie();
            String formStateChecker = formData.getFirst("stateChecker");
            csrfCheck(cookieStateChecker, formStateChecker);

            String username = formData.getFirst("username");
            String password = formData.getFirst("password");
            String passwordConfirmation = formData.getFirst("passwordConfirmation");

            if (username == null || username.length() == 0) {
                return createWelcomePage(null, "Username is missing");
            }

            if (password == null || password.length() == 0) {
                return createWelcomePage(null, "Password is missing");
            }

            if (!password.equals(passwordConfirmation)) {
                return createWelcomePage(null, "Password and confirmation doesn't match");
            }

            ApplianceBootstrap applianceBootstrap = new ApplianceBootstrap(session);
            if (applianceBootstrap.isNoMasterUser()) {
                bootstrap = false;
                applianceBootstrap.createMasterRealmUser(username, password);

                logger.info("Created initial admin user with username " + username);
                return createWelcomePage("User created", null);
            } else {
                logger.warn("Rejected attempt to create initial user as user is already created");
                return createWelcomePage(null, "Users already exists");
            }
        }
    }

    @Override
    public Response getResource(String path) {
        try {
            InputStream resource = getTheme().getResourceAsStream(path);
            if (resource != null) {
                String contentType = MimeTypeUtil.getContentType(path);
                Response.ResponseBuilder builder = Response.ok(resource).type(contentType)
                        .cacheControl(CacheControlUtil.getDefaultCacheControl());
                return builder.build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (IOException e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Response createWelcomePage(String successMessage, String errorMessage) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("bootstrap", bootstrap);
            if (bootstrap) {
                boolean isLocal = isLocal();
                map.put("localUser", isLocal);

                if (isLocal) {
                    String stateChecker = updateCsrfChecks();
                    map.put("stateChecker", stateChecker);
                }
            }
            if (successMessage != null) {
                map.put("successMessage", successMessage);
            }
            if (errorMessage != null) {
                map.put("errorMessage", errorMessage);
            }
            FreeMarkerUtil freeMarkerUtil = new FreeMarkerUtil();
            String result = freeMarkerUtil.processTemplate(map, "index.ftl", getTheme());

            ResponseBuilder rb = Response.status(errorMessage == null ? Status.OK : Status.BAD_REQUEST).entity(result).cacheControl(CacheControlUtil.noCache());
            BrowserSecurityHeaderSetup.headers(rb, BrowserSecurityHeaders.defaultHeaders);
            return rb.build();
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Theme getTheme() {
        Config.Scope config = Config.scope("theme");
        ThemeProvider themeProvider = session.getProvider(ThemeProvider.class, "extending");
        try {
            return themeProvider.getTheme(config.get("welcomeTheme"), Theme.Type.WELCOME);
        } catch (IOException e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void checkBootstrap() {
        if (bootstrap) {
            bootstrap = new ApplianceBootstrap(session).isNoMasterUser();
        }
    }
    
    private boolean isLocal() {
        try {
            InetAddress inetAddress = InetAddress.getByName(session.getContext().getConnection().getRemoteAddr());
            return inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress();
        } catch (UnknownHostException e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private String updateCsrfChecks() {
        String stateChecker = getCsrfCookie();
        if (stateChecker != null) {
            return stateChecker;
        } else {
            stateChecker = OpenfactModelUtils.generateSecret();
            String cookiePath = uriInfo.getPath();
            boolean secureOnly = uriInfo.getRequestUri().getScheme().equalsIgnoreCase("https");
            CookieHelper.addCookie(OPENFACT_STATE_CHECKER, stateChecker, cookiePath, null, null, -1, secureOnly, true);
            return stateChecker;
        }
    }

    private String getCsrfCookie() {
        Cookie cookie = headers.getCookies().get(OPENFACT_STATE_CHECKER);
        return cookie == null ? null : cookie.getValue();
    }

    private void csrfCheck(String cookieStateChecker, String formStateChecker) {
        if (cookieStateChecker == null || !cookieStateChecker.equals(formStateChecker)) {
            throw new ForbiddenException();
        }
    }

}
