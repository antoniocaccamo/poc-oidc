package poc.oidc.client.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.reactive.function.client.WebClient;
import poc.oidc.client.support.GroupsClaimMapper;
import poc.oidc.client.support.NamedOidcUser;


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtAuthorizationProperties.class)
public class AadOAuth2LoginSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository, JwtAuthorizationProperties props) throws Exception {
        
        String base_uri = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, base_uri);
        // Responsible for enabling PKCE, to generate code verifier, code challenge
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

        // @formatter:off
        return http

               .authorizeHttpRequests( r -> r.anyRequest().authenticated())
               .oauth2Login(oauth2 -> {oauth2
                          .userInfoEndpoint(ep -> ep.oidcUserService(customOidcUserService(props)));
                   oauth2.authorizationEndpoint( conf -> conf.authorizationRequestResolver(resolver));
               })
               .oauth2Client(Customizer.withDefaults())
               .build();
        // @formatter:on
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> customOidcUserService(JwtAuthorizationProperties props) {
        final OidcUserService delegate = new OidcUserService();
        final GroupsClaimMapper mapper = new GroupsClaimMapper(
                props.getAuthoritiesPrefix(),
                props.getGroupsClaim(),
                props.getGroupToAuthorities());

        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            // Enrich standard authorities with groups
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            mappedAuthorities.addAll(oidcUser.getAuthorities());
            mappedAuthorities.addAll(mapper.mapAuthorities(oidcUser));

            oidcUser = new NamedOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo(),oidcUser.getName());

            return oidcUser;
        };
    }

    @Value("${app.poc.aad.wa.baseUrl}")
    private String baseUrl;
    
    @Bean(name = "azureWebAppClient")
    @Profile("azure")
    public WebClient azureWebAppClient(OAuth2AuthorizedClientManager authorizedClientManager) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction filter =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);


        return WebClient.builder()
                       .apply( filter.oauth2Configuration())
                       .build();
    }




}