package codes.recursive.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;

@Introspected
@ConfigurationProperties("cod")
public class CodClientConfig {
    private String ssoToken;

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }
}
