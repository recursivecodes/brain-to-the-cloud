package codes.recursive.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;

@Introspected
@ConfigurationProperties("cod")
public class CodClientConfig {
    @Nullable
    private String ssoToken;
    private String userAgent;
    private String xsrfToken;

    public String getXsrfToken() {
        return xsrfToken;
    }

    public void setXsrfToken(String xsrfToken) {
        this.xsrfToken = xsrfToken;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(@Nullable String ssoToken) {
        this.ssoToken = ssoToken;
    }
}
