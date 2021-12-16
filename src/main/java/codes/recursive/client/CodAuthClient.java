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
@Header(name = "X-XSRF-TOKEN", value = "68e8b62e-1d9d-4ce1-b93f-cbe5ff31a041")
@Header(name = "ACT_SSO_COOKIE", value = "${cod.ssoToken:ODI5NjQ2NDoxNjQwODE5NTkzMzU0OjdlM2JhZjAyM2RmOWM5MzRiYTgzNTA5NDUwYTljZTgz}")
@Header(name = CONTENT_TYPE, value = MediaType.APPLICATION_JSON)
@Header(name = ACCEPT, value = MediaType.APPLICATION_JSON)
@Header(name = COOKIE, value = "new_SiteId=cod; ACT_SSO_LOCALE=en_US;country=US;ACT_SSO_COOKIE=${cod.ssoToken:ODI5NjQ2NDoxNjQwODE5NTkzMzU0OjdlM2JhZjAyM2RmOWM5MzRiYTgzNTA5NDUwYTljZTgz};XSRF-TOKEN=68e8b62e-1d9d-4ce1-b93f-cbe5ff31a041;API_CSRF_TOKEN=68e8b62e-1d9d-4ce1-b93f-cbe5ff31a041")
@Header(name = USER_AGENT, value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36w")
@Header(name = "x-requested-with", value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36w")
public interface CodAuthClient {
    @Get(value = "/crm/cod/v2/title/{game}/platform/{platform}/gamer/{gamertag}/matches/mp/start/0/end/0/details?locale=en")
    HashMap<String, Object> getMatchDetails(String game, String platform, String gamertag);
}
