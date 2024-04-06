package com.prueba.reservas.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;


@Configuration
@EnableR2dbcRepositories
public class ConnectionConfigR2dbc extends AbstractR2dbcConfiguration {


    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactoryOptions connectionFactoryOptions = ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql")
                .option(HOST, "localhost")
                .option(USER, "admin")
                .option(PASSWORD, "docker")
                .option(DATABASE, "reservas")
                .build();

        ConnectionFactory connectionFactory = ConnectionFactories.get(connectionFactoryOptions);

        ConnectionPoolConfiguration connectionPoolConfiguration = ConnectionPoolConfiguration.builder()
                .connectionFactory(connectionFactory)
                .build();

        return new ConnectionPool(connectionPoolConfiguration);
    }
}
