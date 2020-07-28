package blog;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Handler implements WebMvcConfigurer {

    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pics/**").addResourceLocations("classpath:/public");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts");
        registry.addResourceHandler("/icons/**").addResourceLocations("classpath:/static/icons");
        super.addResourceHandlers(registry);
    }*/
}
