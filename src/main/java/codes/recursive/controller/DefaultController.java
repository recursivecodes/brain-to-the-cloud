package codes.recursive.controller;

import codes.recursive.task.RecentMatches;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.Map;

@Controller("/")
public class DefaultController {

    private final RecentMatches recentMatches;

    public DefaultController(
            RecentMatches recentMatches
    ) {
        this.recentMatches = recentMatches;
    }

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }

    @Post(uri = "/token", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public HttpResponse saveToken(Map<String, String> data) {
        recentMatches.setActivisionToken(data.get("token"));
        return HttpResponse.ok();
    }
}