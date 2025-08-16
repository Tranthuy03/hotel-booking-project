package com.hotel.hotelbooking.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> {
            // Không giới hạn kích thước swallow
            connector.setProperty("maxSwallowSize", "-1");
            // Giới hạn số file tối đa trong request (mặc định Tomcat 10 là 10)
            connector.setProperty("maxFileCount", "100");
        });
    }
}
