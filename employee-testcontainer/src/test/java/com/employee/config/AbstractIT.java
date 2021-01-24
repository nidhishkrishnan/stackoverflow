package com.employee.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

//@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.main.allow-bean-definition-overriding=true"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // don't replace our DB with an in-memory one
@ContextConfiguration(initializers = AbstractIT.DockerPostgresDataSourceInitializer.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractIT {

    private static PostgreSQLContainer<?> postgresDBContainer = new PostgreSQLContainer<>("postgres:9.6")
            .withUrlParam("TC_DAEMON", "true")
            .withFileSystemBind("docker/db", "/docker-entrypoint-initdb.d", BindMode.READ_WRITE);

    static {
        postgresDBContainer.start();
    }

    public static class DockerPostgresDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Assertions.assertNotNull(applicationContext);
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true",
                    "spring.datasource.driver-class-name="+postgresDBContainer.getDriverClassName(),
                    "spring.datasource.url=" + postgresDBContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresDBContainer.getUsername(),
                    "spring.datasource.password=" + postgresDBContainer.getPassword()
            );
        }
    }
//    @Configuration
//    static class Config {
//        @Bean
//        public DogClient dogClient() {
//            return () -> {
//                DogApiResponse dogApiResponse = new DogApiResponse();
//                dogApiResponse.setMessage("mes1");
//                dogApiResponse.setStatus("sat1");
//                return dogApiResponse;
//            };
//        }
//    }
}
