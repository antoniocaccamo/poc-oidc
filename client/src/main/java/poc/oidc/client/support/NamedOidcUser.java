package poc.oidc.client.support;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

/**
 * @auhtor antonio.caccamo on 2024-03-14 @ 00:09
 */

public class NamedOidcUser extends DefaultOidcUser {
    private static final long serialVersionUID = 1L;
    private final String userName;

    public NamedOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken,
                         OidcUserInfo userInfo, String userName) {
        super(authorities,idToken,userInfo);
        this.userName= userName;
    }

    @Override
    public String getName() {
        return this.userName;
    }
}
