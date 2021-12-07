package codes.recursive.controller;

import codes.recursive.task.RecentMatches;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.views.ModelAndView;

import java.util.Map;

@Controller("/")
public class DefaultController {

    private final RecentMatches recentMatches;

    public DefaultController(
            RecentMatches recentMatches
    ) {
        this.recentMatches = recentMatches;
    }

    @Get("/")
    ModelAndView home() {
        return new ModelAndView("home", CollectionUtils.mapOf("currentView", "home"));
    }

    @Get("/demo")
    ModelAndView demo() {
        return new ModelAndView("demo", CollectionUtils.mapOf("currentView", "demo"));
    }

    @Post(uri = "/token", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public HttpResponse saveToken(Map<String, String> data) {
        recentMatches.setActivisionToken(data.get("token"));
        return HttpResponse.ok();
    }
}