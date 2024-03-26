package poc.oidc.resource.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = true)
@EnableWebSecurity
public class AadOAuth2ResourceServerSecurityConfig {

    private static final String[] patterns = {
            "swagger-ui.html",
            "/swagger-ui/*",
            "/v3/*", "/v3/*/*",
            "/", "index.html",
            "/*.js", "/*.css"
    };

    /**
     * Add configuration logic as needed.
     */
    @Bean
    public SecurityFilterChain htmlFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeHttpRequests(
                    authz ->
                            authz.requestMatchers(patterns).permitAll()
                                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
        ;
        // @formatter:on
        return http.build();
    }


}
