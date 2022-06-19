package com.upm.miw.tfm.eatitusersapp.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "eat-it.products")
public class ProductClientProperties {
    private String url;
}
