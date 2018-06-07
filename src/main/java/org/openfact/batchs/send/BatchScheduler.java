package org.openfact.batchs.send;

import org.jboss.logging.Logger;

import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.time.LocalDateTime;
import java.util.Properties;

@Startup
@Singleton
public class BatchScheduler {

    private static final Logger logger = Logger.getLogger(BatchScheduler.class);

    @Schedule(hour = "3", persistent = false, timezone = "America/Lima")
    public void test() {
        logger.infof("Starting batch send documents at %s", LocalDateTime.now());
        Properties properties = new Properties();
        BatchRuntime.getJobOperator().start("send_closed_documents", properties);
    }

}
