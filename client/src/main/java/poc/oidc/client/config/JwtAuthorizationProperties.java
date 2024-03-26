package poc.oidc.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auhtor antonio.caccamo on 2024-03-14 @ 00:09
 */

@Getter @Setter
@ConfigurationProperties(prefix="app.jwt.authorization")
public class JwtAuthorizationProperties {

    // Claim that has the group list
    private String groupsClaim = "groups";

    private String authoritiesPrefix = "ROLE_";

    // map groupIds to a list of authorities.
    private Map<String, List<String>> groupToAuthorities = new HashMap<>();
}
