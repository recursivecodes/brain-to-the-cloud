package codes.recursive.service;

import com.oracle.bmc.vault.VaultsClient;
import com.oracle.bmc.vault.model.Base64SecretContentDetails;
import com.oracle.bmc.vault.model.UpdateSecretDetails;
import com.oracle.bmc.vault.requests.UpdateSecretRequest;
import com.oracle.bmc.vault.responses.UpdateSecretResponse;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.Introspected;
import jakarta.inject.Singleton;

@Introspected
@Singleton
public class TokenService {
    private final VaultsClient vaultsClient;
    private final String secretOcid;

    public TokenService(VaultsClient vaultsClient, @Property(name = "codes.recursive.secret-ocid") String secretOcid) {
        this.vaultsClient = vaultsClient;
        this.secretOcid = secretOcid;
    }

    public UpdateSecretResponse updateToken(String token) {
        Base64SecretContentDetails contentDetails = Base64SecretContentDetails.builder()
                .content(token)
                .build();
        UpdateSecretDetails secretDetails = UpdateSecretDetails.builder()
                .secretContent(contentDetails)
                .build();
        UpdateSecretRequest updateSecretRequest = UpdateSecretRequest.builder()
                .secretId(secretOcid)
                .body$(secretDetails)
                .build();
        return vaultsClient.updateSecret(updateSecretRequest);
    }
}
