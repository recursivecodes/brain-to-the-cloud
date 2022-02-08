package codes.recursive.client;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;

import java.util.Map;

import static io.micronaut.http.HttpHeaders.CONNECTION;

@Client("https://my.callofduty.com")
@Header(name = CONNECTION, value = "keep-alive")
public interface CodPublicClient {
    @Get("/content/atvi/callofduty/mycod/web/en/data/json/iq-content-xweb.js")
    Map<String, Object> getLookupValues();
}
