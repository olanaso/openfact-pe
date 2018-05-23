package org.openfact.report;

import org.jboss.logging.Logger;
import org.openfact.common.Version;
import org.openfact.report.ReportProviderType.ProviderType;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Startup
@DependsOn(value = {"JarReportThemeProvider"})
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@ReportProviderType(type = ProviderType.EXTENDING)
public class ExtendingReportThemeManager implements ReportThemeProvider {

    private static final Logger log = Logger.getLogger(ExtendingReportThemeManager.class);

    private ConcurrentHashMap<ThemeKey, ReportTheme> themeCache;

    private List<ReportThemeProvider> providers;
    private String defaultTheme;

    @Inject
    @Any
    @ReportThemeManagerSelector
    private Instance<ReportThemeProvider> themeProviders;

    @Inject
    @ConfigurationValue("org.openfact.report.defaultTheme")
    private Optional<String> defaultThemeConfig;

    @Inject
    @ConfigurationValue("org.openfact.report.cacheThemes")
    private Optional<Boolean> cacheThemesConfig;

    @PostConstruct
    public void init() {
        this.defaultTheme = defaultThemeConfig.orElse("openfact");
        loadProviders();

        if (cacheThemesConfig.orElse(Boolean.TRUE)) {
            themeCache = new ConcurrentHashMap<>();
        }
    }

    private void loadProviders() {
        providers = new LinkedList();

        Iterator<ReportThemeProvider> it = themeProviders.iterator();
        while (it.hasNext()) {
            ReportThemeProvider themeProvider = it.next();
            if (!(themeProvider instanceof ExtendingReportThemeManager)) {
                if (!themeProvider.getClass().equals(ExtendingReportThemeManager.class)) {
                    providers.add(themeProvider);
                }
            }
        }

        Collections.sort(providers, (o1, o2) -> o2.getProviderPriority() - o1.getProviderPriority());
    }

    @Override
    public int getProviderPriority() {
        return 0;
    }

    @Override
    public ReportTheme getTheme(String name, ReportTheme.Type type) throws IOException {
        if (name == null) {
            name = defaultTheme;
        }

        if (themeCache != null) {
            ThemeKey key = ThemeKey.get(name, type);
            ReportTheme theme = themeCache.get(key);
            if (theme == null) {
                theme = loadTheme(name, type);
                if (theme == null) {
                    theme = loadTheme("openfact", type);
                    if (theme == null) {
                        theme = loadTheme("base", type);
                    }
                    log.errorv("Failed to find {0} report theme {1}, using built-in report themes", type, name);
                } else if (themeCache.putIfAbsent(key, theme) != null) {
                    theme = themeCache.get(key);
                }
            }
            return theme;
        } else {
            return loadTheme(name, type);
        }
    }

    private ReportTheme loadTheme(String name, ReportTheme.Type type) throws IOException {
        ReportTheme theme = findTheme(name, type);
        if (theme != null && (theme.getParentName() != null || theme.getImportName() != null)) {
            List<ReportTheme> themes = new LinkedList<>();
            themes.add(theme);

            if (theme.getImportName() != null) {
                String[] s = theme.getImportName().split("/");
                themes.add(findTheme(s[1], ReportTheme.Type.valueOf(s[0].toUpperCase())));
            }

            if (theme.getParentName() != null) {
                for (String parentName = theme.getParentName(); parentName != null; parentName = theme.getParentName()) {
                    theme = findTheme(parentName, type);
                    themes.add(theme);

                    if (theme.getImportName() != null) {
                        String[] s = theme.getImportName().split("/");
                        themes.add(findTheme(s[1], ReportTheme.Type.valueOf(s[0].toUpperCase())));
                    }
                }
            }

            return new ExtendingTheme(themes);
        } else {
            return theme;
        }
    }

    @Override
    public Set<String> nameSet(ReportTheme.Type type) {
        Set<String> themes = new HashSet<>();
        for (ReportThemeProvider p : providers) {
            themes.addAll(p.nameSet(type));
        }
        return themes;
    }

    @Override
    public boolean hasTheme(String name, ReportTheme.Type type) {
        for (ReportThemeProvider p : providers) {
            if (p.hasTheme(name, type)) {
                return true;
            }
        }
        return false;
    }

    private ReportTheme findTheme(String name, ReportTheme.Type type) {
        for (ReportThemeProvider p : providers) {
            if (p.hasTheme(name, type)) {
                try {
                    return p.getTheme(name, type);
                } catch (IOException e) {
                    log.errorv(e, p.getClass() + " failed to load report theme, type={0}, name={1}", type, name);
                }
            }
        }
        return null;
    }

}
