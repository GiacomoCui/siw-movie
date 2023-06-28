package it.uniroma3.siw.controller.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ImageConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path fotoUploadDir = Paths.get("./foto-movie");
        String fotoUploadPath = fotoUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/foto-movie/**").addResourceLocations("file:/" + fotoUploadPath + "/");
    }
}
