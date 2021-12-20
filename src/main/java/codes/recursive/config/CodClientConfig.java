package codes.recursive.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;

@Introspected
@ConfigurationProperties("cod")
public class CodClientConfig {
    @Nullable
    private String ssoToken;

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(@Nullable String ssoToken) {
        this.ssoToken = ssoToken;
    }
}
