package codes.recursive.controller;

import codes.recursive.model.Brain;
import codes.recursive.model.Game;
import codes.recursive.repository.AbstractGameRepository;
import codes.recursive.repository.BrainRepository;
import codes.recursive.repository.GameRepository;
import codes.recursive.service.CodPublicClient;
import codes.recursive.task.RecentMatches;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller("/api")
@SuppressWarnings("rawtypes")
public class ApiController {
    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    private final CodPublicClient codPublicClient;
    private final RecentMatches recentMatches;
    private final GameRepository gameRepository;
    private final BrainRepository brainRepository;
    private final AbstractGameRepository abstractGameRepository;

    public ApiController(
            CodPublicClient codPublicClient,
            RecentMatches recentMatches,
            GameRepository gameRepository,
            BrainRepository brainRepository,
            AbstractGameRepository abstractGameRepository
    ) {
        this.codPublicClient = codPublicClient;
        this.recentMatches = recentMatches;
        this.gameRepository = gameRepository;
        this.brainRepository = brainRepository;
        this.abstractGameRepository = abstractGameRepository;
    }

    @Get(uri = "/codLookups")
    public HttpResponse getCodLookups() {
        return HttpResponse.ok(
                codPublicClient.getLookupValues()
        );
    }

    @Get(uri = "/recentMatches{/offsetParam}{/maxParam}")
    public Page<Game> getRecentMatches(Optional<Integer> offsetParam, Optional<Integer> maxParam) {
        Integer offset = offsetParam.orElse(0);
        Integer max = maxParam.orElse(25);
        Pageable pageable = Pageable.from(offset, max);
        return gameRepository.findAll(pageable);
    }

    @Get(uri = "/highlightedMatches{/offsetParam}{/maxParam}")
    public Page<Game> getHighlightedMatches(Optional<Integer> offsetParam, Optional<Integer> maxParam) {
        Integer offset = offsetParam.orElse(0);
        Integer max = maxParam.orElse(25);
        Pageable pageable = Pageable.from(offset, max);
        return gameRepository.findAllByIsHighlightedEqual(true, pageable);
    }

    @Get(uri = "/brainForMatch/{startTime}/{endTime}")
    public List<Brain> getBrainForMatch(Integer startTime, Integer endTime) {
        return brainRepository.getBrainForMatch(startTime, endTime);
    }

    @Put(uri = "/highlightGame/{matchId}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse highlightGame(Long matchId) {
        Optional<Game> result = gameRepository.findById(matchId);
        if(result.isPresent()) {
            Game game = result.get();
            gameRepository.update(game.getId(), true);
        }
        return HttpResponse.noContent();
    }

    @Post(uri = "/token", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public HttpResponse saveToken(Map<String, String> data) {
        recentMatches.setActivisionToken(data.get("token"));
        return HttpResponse.ok();
    }
}