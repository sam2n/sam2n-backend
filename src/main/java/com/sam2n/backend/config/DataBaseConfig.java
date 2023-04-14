package com.sam2n.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({ "com.sam2n.backend.repository" })
@EnableJpaAuditing
@EnableTransactionManagement
public class DataBaseConfig {

    // Regex for acceptable logins
    public static final String CREATED_BY_USER = "FAKER";
    public static final String TABLE_NAME_PREFIX = "SAM2N_";

}
