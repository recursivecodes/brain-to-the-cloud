package codes.recursive.controller;

import codes.recursive.model.Brain;
import codes.recursive.model.Game;
import codes.recursive.repository.AbstractGameRepository;
import codes.recursive.repository.BrainRepository;
import codes.recursive.repository.GameRepository;
import codes.recursive.task.RecentMatches;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.views.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Controller("/")
public class DefaultController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultController.class);

    private final RecentMatches recentMatches;
    private final GameRepository gameRepository;
    private final BrainRepository brainRepository;
    private final AbstractGameRepository abstractGameRepository;

    public DefaultController(
            RecentMatches recentMatches,
            GameRepository gameRepository,
            BrainRepository brainRepository,
            AbstractGameRepository abstractGameRepository
    ) {
        this.recentMatches = recentMatches;
        this.gameRepository = gameRepository;
        this.brainRepository = brainRepository;
        this.abstractGameRepository = abstractGameRepository;
    }

    @Get("/")
    ModelAndView home() {
        return new ModelAndView("home", CollectionUtils.mapOf("currentView", "home"));
    }

    @Get("/demo")
    ModelAndView demo() {
        return new ModelAndView("demo", CollectionUtils.mapOf("currentView", "demo"));
    }

    @Get("/recentMatches")
    public Page<Game> getRecentMatches() {
        Pageable pageable = Pageable.from(0, 25);
        return gameRepository.findAll(pageable);
    }

    @Get("/brainForMatch/{startTime}/{endTime}")
    public List<Brain> getBrainForMatch(Integer startTime, Integer endTime) {
        return brainRepository.getBrainForMatch(startTime, endTime);
    }

    @Post(uri = "/token", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public HttpResponse saveToken(Map<String, String> data) {
        recentMatches.setActivisionToken(data.get("token"));
        return HttpResponse.ok();
    }
}