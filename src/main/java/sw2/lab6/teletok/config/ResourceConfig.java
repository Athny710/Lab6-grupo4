package sw2.lab6.teletok.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    public void  addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/fotos/**").addResourceLocations("file:C://Users//a2014//Desktop//fotos");

    }
}
