package com.example.food_deliv.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.noCache());

        // Обработка статических ресурсов для конкретных папок
        registry.addResourceHandler("/dishes/**")
                .addResourceLocations("classpath:/static/dishes")
                .setCacheControl(CacheControl.noCache());

        registry.addResourceHandler("/main/**")
                .addResourceLocations("classpath:/static/main/")
                .setCacheControl(CacheControl.noCache());

        registry.addResourceHandler("/cart/**")
                .addResourceLocations("classpath:/static/cart")
                .setCacheControl(CacheControl.noCache());

        registry.addResourceHandler("/lunch/**")
                .addResourceLocations("classpath:/static/lunch")
                .setCacheControl(CacheControl.noCache());

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.noCache());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/lunch").setViewName("forward:/lunches.html");
        registry.addViewController("/dishes").setViewName("forward:/dishes.html");
        registry.addViewController("/cart").setViewName("forward:/cart.html");
    }
}
