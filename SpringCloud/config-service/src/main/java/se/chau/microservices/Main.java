package se.chau.microservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigServer
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
        String repoLocation = ctx.getEnvironment().getProperty("spring.cloud.config.server.native.searchLocations");
        LOG.info("Serving configurations from folder: " + repoLocation);
    }
}