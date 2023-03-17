package com.sam2n.backend.config;

import io.hypersistence.utils.hibernate.naming.CamelCaseToSnakeCaseNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import static com.sam2n.backend.config.DataBaseConfig.TABLE_NAME_PREFIX;

public class TableNamingStrategy extends CamelCaseToSnakeCaseNamingStrategy {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        Identifier newIdentifier = new Identifier(TABLE_NAME_PREFIX + name.getText(), name.isQuoted());
        return super.toPhysicalTableName(newIdentifier, context);
    }
}