package codes.recursive.client;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;

import java.util.HashMap;
import static io.micronaut.http.HttpHeaders.CONTENT_TYPE;
import static io.micronaut.http.HttpHeaders.COOKIE;
import static io.micronaut.http.HttpHeaders.USER_AGENT;
import static io.micronaut.http.HttpHeaders.ACCEPT;

@Client("https://my.callofduty.com/api/papi-client")
@Header(name = "X-XSRF-TOKEN", value = "${cod.xsrf-token}")
@Header(name = "ACT_SSO_COOKIE", value = "${cod.sso-token}")
@Header(name = CONTENT_TYPE, value = MediaType.APPLICATION_JSON)
@Header(name = ACCEPT, value = MediaType.APPLICATION_JSON)
@Header(name = COOKIE, value = "new_SiteId=cod; ACT_SSO_LOCALE=en_US;country=US;ACT_SSO_COOKIE=${cod.sso-token};XSRF-TOKEN=${cod.xsrf-token};API_CSRF_TOKEN=${cod.xsrf-token}")
@Header(name = USER_AGENT, value = "${cod.user-agent}")
@Header(name = "x-requested-with", value = "${cod.user-agent}")
public interface CodAuthClient {
    @Get(value = "/crm/cod/v2/title/{game}/platform/{platform}/gamer/{gamertag}/matches/mp/start/0/end/0/details?locale=en")
    HashMap<String, Object> getMatchDetails(String game, String platform, String gamertag);
}
