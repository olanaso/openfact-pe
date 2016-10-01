/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openfact.services.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.openfact.Config;
import org.openfact.exportimport.ExportImportManager;
import org.openfact.migration.MigrationModelManager;
import org.openfact.models.*;
import org.openfact.models.dblock.DBLockProvider;
import org.openfact.models.ubl.InvoiceModel;
import org.openfact.models.dblock.DBLockManager;
import org.openfact.models.utils.OpenfactModelUtils;
import org.openfact.models.utils.PostMigrationEvent;
import org.openfact.models.utils.RepresentationToModel;
import org.openfact.representations.idm.OrganizationRepresentation;
import org.openfact.representations.idm.ubl.InvoiceRepresentation;
import org.openfact.services.DefaultOpenfactSessionFactory;
import org.openfact.services.ServicesLogger;
import org.openfact.services.filters.OpenfactTransactionCommitter;
import org.openfact.services.managers.ApplianceBootstrap;
import org.openfact.services.managers.OrganizationManager;
import org.openfact.services.managers.UblSyncManager;
import org.openfact.services.resources.admin.AdminRoot;
import org.openfact.services.resources.admin.AdminRootImpl;
import org.openfact.services.scheduled.ClearExpiredEvents;
import org.openfact.services.scheduled.ClearExpiredUblSessions;
import org.openfact.services.scheduled.ClusterAwareScheduledTaskRunner;
import org.openfact.services.util.JsonConfigProvider;
import org.openfact.services.util.ObjectMapperResolver;
import org.openfact.timer.TimerProvider;
import org.openfact.transaction.JtaTransactionManagerLookup;
import org.openfact.util.JsonSerialization;
import org.openfact.common.util.SystemEnvProperties;

import javax.servlet.ServletContext;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.*;
import org.jboss.dmr.ModelNode;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class OpenfactApplication extends Application {
    // This param name is defined again in Openfact Server Subsystem class
    // org.openfact.subsystem.server.extension.OpenfactServerDeploymentProcessor.  We have this value in
    // two places to avoid dependency between Openfact Subsystem and Openfact Services module.
    public static final String OPENFACT_CONFIG_PARAM_NAME = "org.openfact.server-subsystem.Config";

    private static final ServicesLogger logger = ServicesLogger.ROOT_LOGGER;

    protected boolean embedded = false;

    protected Set<Object> singletons = new HashSet<Object>();
    protected Set<Class<?>> classes = new HashSet<Class<?>>();

    protected OpenfactSessionFactory sessionFactory;
    protected String contextPath;

    public OpenfactApplication(@Context ServletContext context, @Context Dispatcher dispatcher) {
        try {
            if ("true".equals(context.getInitParameter("openfact.embedded"))) {
                embedded = true;
            }

            loadConfig(context);

            this.contextPath = context.getContextPath();
            this.sessionFactory = createSessionFactory();

            dispatcher.getDefaultContextObjects().put(OpenfactApplication.class, this);
            ResteasyProviderFactory.pushContext(OpenfactApplication.class, this); // for injection
            context.setAttribute(OpenfactSessionFactory.class.getName(), this.sessionFactory);

            singletons.add(new ServerVersionResourceImpl());
            singletons.add(new RobotsResourceImpl());
            singletons.add(new OrganizationsResourceImpl());
            singletons.add(new AdminRootImpl());
            //classes.add(ThemeResource.class);
            //classes.add(JsResource.class);

            classes.add(OpenfactTransactionCommitter.class);

            singletons.add(new ObjectMapperResolver(Boolean.parseBoolean(System.getProperty("openfact.jsonPrettyPrint", "false"))));

            ExportImportManager[] exportImportManager = new ExportImportManager[1];

            OpenfactModelUtils.runJobInTransaction(sessionFactory, new OpenfactSessionTask() {

                @Override
                public void run(OpenfactSession lockSession) {
                    DBLockManager dbLockManager = new DBLockManager(lockSession);
                    dbLockManager.checkForcedUnlock();
                    DBLockProvider dbLock = dbLockManager.getDBLock();
                    dbLock.waitForLock();
                    try {
                        exportImportManager[0] = migrateAndBootstrap();
                    } finally {
                        dbLock.releaseLock();
                    }
                }

            });


            if (exportImportManager[0].isRunExport()) {
                exportImportManager[0].runExport();
            }

            boolean bootstrapAdminUser = false;
            OpenfactSession session = sessionFactory.create();
            try {
                session.getTransactionManager().begin();
                bootstrapAdminUser = new ApplianceBootstrap(session).isNoMasterOrganization();

                session.getTransactionManager().commit();
            } finally {
                session.close();
            }

            sessionFactory.publish(new PostMigrationEvent());

            singletons.add(new WelcomeResourceImpl(bootstrapAdminUser));

            setupScheduledTasks(sessionFactory);
        } catch (Throwable t) {
            if (!embedded) {
                exit(1);
            }
            throw t;
        }
    }

    // Migrate model, bootstrap master organization, import organizations and create admin user. This is done with acquired dbLock
    protected ExportImportManager migrateAndBootstrap() {
        ExportImportManager exportImportManager;
        logger.debug("Calling migrateModel");
        migrateModel();

        logger.debug("bootstrap");
        OpenfactSession session = sessionFactory.create();
        try {
            session.getTransactionManager().begin();
            JtaTransactionManagerLookup lookup = (JtaTransactionManagerLookup) sessionFactory.getProviderFactory(JtaTransactionManagerLookup.class);
            if (lookup != null) {
                if (lookup.getTransactionManager() != null) {
                    try {
                        Transaction transaction = lookup.getTransactionManager().getTransaction();
                        logger.debugv("bootstrap current transaction? {0}", transaction != null);
                        if (transaction != null) {
                            logger.debugv("bootstrap current transaction status? {0}", transaction.getStatus());
                        }
                    } catch (SystemException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


            ApplianceBootstrap applianceBootstrap = new ApplianceBootstrap(session);
            exportImportManager = new ExportImportManager(session);

            boolean createMasterOrganization = applianceBootstrap.isNewInstall();
            if (exportImportManager.isRunImport() && exportImportManager.isImportMasterIncluded()) {
                createMasterOrganization = false;
            }

            if (createMasterOrganization) {
                applianceBootstrap.createMasterOrganization(contextPath);
            }
            if (createMasterOrganization) {
                applianceBootstrap.createDefaultCatalog(contextPath);
            }
            session.getTransactionManager().commit();
        } catch (RuntimeException re) {
            if (session.getTransactionManager().isActive()) {
                session.getTransactionManager().rollback();
            }
            throw re;
        } finally {
            session.close();
        }

        if (exportImportManager.isRunImport()) {
            exportImportManager.runImport();
        } else {
            importOrganizations();
        }

        importAddUser();

        return exportImportManager;
    }


    protected void migrateModel() {
        OpenfactSession session = sessionFactory.create();
        try {
            session.getTransactionManager().begin();
            MigrationModelManager.migrate(session);
            session.getTransactionManager().commit();
        } catch (Exception e) {
            session.getTransactionManager().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public String getContextPath() {
        return contextPath;
    }

    /**
     * Get base URI of WAR distribution, not JAX-RS
     *
     * @param uriInfo
     * @return
     */
    public URI getBaseUri(UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().replacePath(getContextPath()).build();
    }

    public static void loadConfig(ServletContext context) {
        try {
            JsonNode node = null;
            
            String dmrConfig = loadDmrConfig(context);
            if (dmrConfig != null) {
                node = new ObjectMapper().readTree(dmrConfig);
                logger.loadingFrom("standalone.xml or domain.xml");
            }

            String configDir = System.getProperty("jboss.server.config.dir");
            if (node == null && configDir != null) {
                File f = new File(configDir + File.separator + "openfact-server.json");
                if (f.isFile()) {
                    logger.loadingFrom(f.getAbsolutePath());
                    node = new ObjectMapper().readTree(f);
                }
            }

            if (node == null) {
                URL resource = Thread.currentThread().getContextClassLoader().getResource("META-INF/openfact-server.json");
                if (resource != null) {
                    logger.loadingFrom(resource);
                    node = new ObjectMapper().readTree(resource);
                }
            }

            if (node != null) {
                Properties properties = new SystemEnvProperties();
                Config.init(new JsonConfigProvider(node, properties));
            } else {
                throw new RuntimeException("Openfact config not found.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }
    
    private static String loadDmrConfig(ServletContext context) {
        String dmrConfig = context.getInitParameter(OPENFACT_CONFIG_PARAM_NAME);
        if (dmrConfig == null) return null;

        ModelNode dmrConfigNode = ModelNode.fromString(dmrConfig);
        if (dmrConfigNode.asPropertyList().isEmpty()) return null;
        
        // note that we need to resolve expressions BEFORE we convert to JSON
        return dmrConfigNode.resolve().toJSONString(true);
    }

    public static OpenfactSessionFactory createSessionFactory() {
        DefaultOpenfactSessionFactory factory = new DefaultOpenfactSessionFactory();
        factory.init();
        return factory;
    }

    public static void setupScheduledTasks(final OpenfactSessionFactory sessionFactory) {
        long interval = Config.scope("scheduled").getLong("interval", 60L) * 1000;

        OpenfactSession session = sessionFactory.create();
        try {
            TimerProvider timer = session.getProvider(TimerProvider.class);
            timer.schedule(new ClusterAwareScheduledTaskRunner(sessionFactory, new ClearExpiredEvents(), interval), interval, "ClearExpiredEvents");
            timer.schedule(new ClusterAwareScheduledTaskRunner(sessionFactory, new ClearExpiredUblSessions(), interval), interval, "ClearExpiredUserSessions");
            new UblSyncManager().bootstrapPeriodic(sessionFactory, timer);
        } finally {
            session.close();
        }
    }

    public OpenfactSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    public void importOrganizations() {
        String files = System.getProperty("openfact.import");
        if (files != null) {
            StringTokenizer tokenizer = new StringTokenizer(files, ",");
            while (tokenizer.hasMoreTokens()) {
                String file = tokenizer.nextToken().trim();
                OrganizationRepresentation rep;
                try {
                    rep = loadJson(new FileInputStream(file), OrganizationRepresentation.class);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                importOrganization(rep, "file " + file);
            }
        }
    }

    public void importOrganization(OrganizationRepresentation rep, String from) {
        OpenfactSession session = sessionFactory.create();
        boolean exists = false;
        try {
            session.getTransactionManager().begin();

            try {
                OrganizationManager manager = new OrganizationManager(session);
                manager.setContextPath(getContextPath());

                if (rep.getId() != null && manager.getOrganization(rep.getId()) != null) {
                    logger.organizationExists(rep.getOrganization(), from);
                    exists = true;
                }

                if (manager.getOrganizationByName(rep.getOrganization()) != null) {
                    logger.organizationExists(rep.getOrganization(), from);
                    exists = true;
                }
                if (!exists) {
                    OrganizationModel organization = manager.importOrganization(rep);
                    logger.importedOrganization(organization.getName(), from);
                }
                session.getTransactionManager().commit();
            } catch (Throwable t) {
                session.getTransactionManager().rollback();
                if (!exists) {
                    logger.unableToImportOrganization(t, rep.getOrganization(), from);
                }
            }
        } finally {
            session.close();
        }
    }

    public void importAddUser() {
        String configDir = System.getProperty("jboss.server.config.dir");
        if (configDir != null) {
            File addUserFile = new File(configDir + File.separator + "openfact-add-user.json");
            if (addUserFile.isFile()) {
                logger.importingInvoicesFrom(addUserFile);

                List<OrganizationRepresentation> organizations;
                try {
                    organizations = JsonSerialization.readValue(new FileInputStream(addUserFile), new TypeReference<List<OrganizationRepresentation>>() {
                    });
                } catch (IOException e) {
                    logger.failedToLoadInvoices(e);
                    return;
                }

                for (OrganizationRepresentation organizationRep : organizations) {
                    for (InvoiceRepresentation invoiceRep : organizationRep.getInvoices()) {
                        OpenfactSession session = sessionFactory.create();
                        try {
                            session.getTransactionManager().begin();

                            OrganizationModel organization = session.organizations().getOrganizationByName(organizationRep.getOrganization());
                            if (organization == null) {
                                logger.addInvoiceFailedOrganizationNotFound(invoiceRep.getIdUbl(), organizationRep.getOrganization());
                            } else {
                                InvoiceModel invoice = session.invoices().addInvoice(organization, invoiceRep.getIdUbl());
                                /*invoice.setEnabled(invoiceRep.isEnabled());
                                RepresentationToModel.createCredentials(invoiceRep, invoice);
                                RepresentationToModel.createRoleMappings(invoiceRep, invoice, organization);*/
                            }

                            session.getTransactionManager().commit();
                            logger.addInvoiceSuccess(invoiceRep.getIdUbl(), organizationRep.getOrganization());
                        } catch (ModelDuplicateException e) {
                            session.getTransactionManager().rollback();
                            logger.addInvoiceFailedInvoiceExists(invoiceRep.getIdUbl(), organizationRep.getOrganization());
                        } catch (Throwable t) {
                            session.getTransactionManager().rollback();
                            logger.addInvoiceFailed(t, invoiceRep.getIdUbl(), organizationRep.getOrganization());
                        } finally {
                            session.close();
                        }
                    }
                }

                if (!addUserFile.delete()) {
                    logger.failedToDeleteFile(addUserFile.getAbsolutePath());
                }
            }
        }
    }

    private static <T> T loadJson(InputStream is, Class<T> type) {
        try {
            return JsonSerialization.readValue(is, type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse json", e);
        }
    }

    private void exit(int status) {
        new Thread() {
            @Override
            public void run() {
                System.exit(status);
            }
        }.start();
    }

}
