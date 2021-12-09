package codes.recursive.service;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.Map;

@Client("https://my.callofduty.com")
public interface CodPublicClient {
    @Get("/content/atvi/callofduty/mycod/web/en/data/json/iq-content-xweb.js")
    Map<String, Object> getLookupValues();
}
