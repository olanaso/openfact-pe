package org.openfact.connections.jpa;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.stat.CollectionStatistics;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;
import org.jboss.logging.Logger;
import org.openfact.models.RepeidSession;
import org.openfact.timer.ScheduledTask;

import javax.persistence.EntityManagerFactory;

public class HibernateStatsReporter implements ScheduledTask {

    private static final int LIMIT = 100; // Just hardcoded for now

    private final EntityManagerFactory emf;
    private static final Logger logger = Logger.getLogger(HibernateStatsReporter.class);

    public HibernateStatsReporter(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public void run(RepeidSession session) {
        SessionFactory sessionFactory = ((EntityManagerFactoryImpl) emf).getSessionFactory();
        Statistics stats = sessionFactory.getStatistics();

        logStats(stats);

        stats.clear(); // For now, clear stats after each iteration
    }


    protected void logStats(Statistics stats) {
        String lineSep = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder(lineSep).append(stats.toString()).append(lineSep).append(lineSep);

        logEntities(builder, lineSep, stats);
        logCollections(builder, lineSep, stats);
        logQueries(builder, lineSep, stats);

        logger.infof(builder.toString());
    }


    protected void logEntities(StringBuilder builder, String lineSep, Statistics stats) {
        builder.append("Important entities statistics: ").append(lineSep);
        for (String entity : stats.getEntityNames()) {
            EntityStatistics entityStats = stats.getEntityStatistics(entity);
            if (entityStats.getInsertCount() > LIMIT || entityStats.getDeleteCount() > LIMIT || entityStats.getUpdateCount() > LIMIT || entityStats.getLoadCount() > LIMIT || entityStats.getFetchCount() > LIMIT) {
                builder.append(entity + " - ")
                        .append("inserted: " + entityStats.getInsertCount())
                        .append(", updated: " + entityStats.getUpdateCount())
                        .append(", removed: " + entityStats.getDeleteCount())
                        .append(", loaded: " + entityStats.getLoadCount())
                        .append(", fetched: " + entityStats.getFetchCount())
                        .append(lineSep);
            }
        }
        builder.append(lineSep);
    }


    protected void logCollections(StringBuilder builder, String lineSep, Statistics stats) {
        builder.append("Important collections statistics: ").append(lineSep);
        for (String col : stats.getCollectionRoleNames()) {
            CollectionStatistics collectionStats = stats.getCollectionStatistics(col);
            if (collectionStats.getRecreateCount() > LIMIT || collectionStats.getUpdateCount() > LIMIT || collectionStats.getRemoveCount() > LIMIT ||
                    collectionStats.getLoadCount() > LIMIT || collectionStats.getFetchCount() > LIMIT) {
                builder.append(col + " - ")
                        .append("recreated: " + collectionStats.getRecreateCount())
                        .append(", updated: " + collectionStats.getUpdateCount())
                        .append(", removed: " + collectionStats.getRemoveCount())
                        .append(", loaded: " + collectionStats.getLoadCount())
                        .append(", fetched: " + collectionStats.getFetchCount())
                        .append(lineSep);
            }
        }
        builder.append(lineSep);
    }


    protected void logQueries(StringBuilder builder, String lineSep, Statistics stats) {
        builder.append("Important queries statistics: ").append(lineSep).append(lineSep);
        for (String query : stats.getQueries()) {
            QueryStatistics queryStats = stats.getQueryStatistics(query);

            if (queryStats.getExecutionCount() > LIMIT || (queryStats.getExecutionCount() * queryStats.getExecutionAvgTime() > LIMIT)) {
                builder.append(query).append(lineSep)
                        .append("executionCount=" + queryStats.getExecutionCount()).append(lineSep)
                        .append("executionAvgTime=" + queryStats.getExecutionAvgTime()).append(" ms").append(lineSep)
                        .append(lineSep)
                        .append(lineSep);
            }
        }
    }

}
