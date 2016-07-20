package org.openfact.connections.jpa.updater.liquibase.lock;

import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.statement.core.InitializeDatabaseChangeLogLockTableStatement;

import java.util.ArrayList;
import java.util.List;

public class CustomInsertLockRecordGenerator extends AbstractSqlGenerator<InitializeDatabaseChangeLogLockTableStatement> {

    @Override
    public int getPriority() {
        return super.getPriority() + 1; // Ensure bigger priority than InitializeDatabaseChangeLogLockTableGenerator
    }

    @Override
    public ValidationErrors validate(InitializeDatabaseChangeLogLockTableStatement initializeDatabaseChangeLogLockTableStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return new ValidationErrors();
    }

    @Override
    public Sql[] generateSql(InitializeDatabaseChangeLogLockTableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        // Generated by InitializeDatabaseChangeLogLockTableGenerator
        Sql[] sqls = sqlGeneratorChain.generateSql(statement, database);

        // Removing delete statement
        List<Sql> result = new ArrayList<>();
        for (Sql sql : sqls) {
            String sqlCommand = sql.toSql();
            if (!sqlCommand.toUpperCase().contains("DELETE")) {
                result.add(sql);
            }
        }

        return result.toArray(new Sql[result.size()]);
    }
}
