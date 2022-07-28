package codes.recursive.controller;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

@Controller()
@Secured(SecurityRule.IS_AUTHENTICATED)
@SuppressWarnings("rawtypes")
public class PageController {
    private static final Logger LOG = LoggerFactory.getLogger(PageController.class);
    @Property(name = "codes.recursive.ivs.playback-url") String playbackUrl;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/games")
    ModelAndView gameAnalysis(@Nullable Principal principal) {
        return new ModelAndView("game-analysis", CollectionUtils.mapOf(
                "currentView", "game-analysis",
                "isLoggedIn", principal != null
        ));
    }

    @Get(uri ="/live")
    ModelAndView live(@Nullable Principal principal) {
        return new ModelAndView("live", CollectionUtils.mapOf(
                "currentView", "live",
                "isLoggedIn", principal != null
        ));
    }

    @Get(uri ="/live-stream")
    ModelAndView liveStream(@Nullable Principal principal) {
        return new ModelAndView("live-stream", CollectionUtils.mapOf(
                "currentView", "live-stream",
                "isLoggedIn", principal != null,
                "playbackUrl", playbackUrl
        ));
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get(uri ="/")
    ModelAndView home(@Nullable Principal principal) {
        return new ModelAndView("home", CollectionUtils.mapOf(
                "currentView", "home",
                "isLoggedIn", principal != null
        ));
    }

}