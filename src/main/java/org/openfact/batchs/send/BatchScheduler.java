package org.openfact.batchs.send;

import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.Resource;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.*;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Startup
@Singleton
public class BatchScheduler {


}
