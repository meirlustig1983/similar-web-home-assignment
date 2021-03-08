package similar.web.home.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class AppConfig {

    @Bean
    PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver(){
        return new PathMatchingResourcePatternResolver();
    }
}
