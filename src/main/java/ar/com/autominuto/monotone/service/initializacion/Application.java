package ar.com.autominuto.monotone.service.initializacion;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan({"ar.com.autominuto.monotone"})
@PropertySources({
    @PropertySource(value = "classpath:application.properties"),
})
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
    
//    @Bean
//    public BeanPostProcessor beanPostProcessor() {
//        return new BeanPostProcessor() {
//
//			
//			@Override
//			public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
//				 if (arg0 instanceof RequestMappingHandlerMapping && "requestMappingHandlerMapping".equals(arg1)) {
//	                    ((RequestMappingHandlerMapping) arg0).setUseRegisteredSuffixPatternMatch(false);
//	              }
//				 return null;
//			}
//
//			@Override
//			public Object postProcessAfterInitialization(Object arg0, String arg1) throws BeansException {
//				// TODO Auto-generated method stub
//				return null;
//			}
//        }
//    }   
    
}
