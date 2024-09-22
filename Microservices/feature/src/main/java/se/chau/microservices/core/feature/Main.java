package se.chau.microservices.core.feature;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication(scanBasePackages = {"se.chau.microservices.core.feature", "se.chau.microservices.util.http"})
public class Main {
    @Value("${api.common.appTitle}") String title;
    @Value("${api.common.appDescription}") String description;
    @Value("${api.common.apiVersion}") String version;
    @Value("${api.common.apiContactName}") String contactName;
    @Value("${api.common.appContactUrl}") String contactUrl;
    @Value("${api.common.appContactEmail}") String contactEmail;
    @Value("${api.common.apiTermOfService}") String termOfLicense;

    @Value("${api.common.appLicense}") String license;

    @Value("${api.common.apiLicenseUrl}") String licenseUrl;

    @Value("${api.common.apiExternalDoc}") String externalDocs;

    @Value("${api.common.apiExternalUrl}") String externalUrl;

    private final Integer threadPoolSize;
    private final Integer taskQueueSize;
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    @Autowired
    ReactiveMongoOperations mongoTemplate;
    @Autowired
    public Main(
            @Value("${app.threadPoolSize:10}") Integer threadPoolSize,
            @Value("${app.taskQueueSize:100}") Integer taskQueueSize
    ) {
        this.threadPoolSize = threadPoolSize;
        this.taskQueueSize = taskQueueSize;
    }

    @Bean
    public OpenAPI getOpenAPIDocumentation(){
        return new OpenAPI()
                .info(new Info().title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact()
                                .name(contactName)
                                .url(contactUrl)
                                .email(contactEmail)
                        )
                        .termsOfService(termOfLicense)
                        .license(new License()
                                .name(license)
                                .url(licenseUrl)
                        )
                )
                .externalDocs(new ExternalDocumentation().description(externalDocs).url(externalUrl))
                ;
    }

    @Bean
    public Scheduler publishEventScheduler() {
        LOG.info("Creates a messagingScheduler with connectionPoolSize = {}", threadPoolSize);
        return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "publish-pool");
    }
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }
}