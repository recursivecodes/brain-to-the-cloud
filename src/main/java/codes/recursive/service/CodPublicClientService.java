package codes.recursive.service;

import codes.recursive.client.CodPublicClient;
import io.micronaut.core.annotation.Introspected;
import jakarta.inject.Singleton;

import java.util.Map;

@Introspected
@Singleton
public class CodPublicClientService {
    private final CodPublicClient codPublicClient;
    private Map<String, Object> codLookups;

    public CodPublicClientService(CodPublicClient codPublicClient) {
        this.codPublicClient = codPublicClient;
    }

    public Map<String, Object> getCodLookups() {
        if(codLookups == null) {
            codLookups = codPublicClient.getLookupValues();
        }
        return codLookups;
    }
}
