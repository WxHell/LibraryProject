package com.luv2code.spring_boot_library.config;

import com.luv2code.spring_boot_library.entity.Book;
import com.luv2code.spring_boot_library.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
// Bu sınıf, Spring Data REST yapılandırmasını özelleştirmek için kullanılır.
public class MyDataRestConfig implements RepositoryRestConfigurer {

    // CORS politikası için izin verilen kaynak URL'si
    private String theAllowedOrigins = "http://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        // Desteklenmeyen HTTP metodları (Kullanıcıların yapmasını istemediğimiz işlemler)
        HttpMethod[] theUnsupportedActions = {
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT};

        // Book sınıfı için ID alanlarını dışarıya açıyoruz
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);

        // Belirlenen HTTP metodlarını devre dışı bırakıyoruz
        disableHttpMethods(Book.class, config, theUnsupportedActions);
        disableHttpMethods(Review.class,config,theUnsupportedActions);

        // CORS yapılandırmasını ayarlıyoruz, belirli bir kaynaktan gelen isteklere izin veriyoruz
        cors.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);
    }

    // HTTP metodlarını belirli bir sınıf için devre dışı bırakma işlemi
    private void disableHttpMethods(Class theClass,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));
    }
}
