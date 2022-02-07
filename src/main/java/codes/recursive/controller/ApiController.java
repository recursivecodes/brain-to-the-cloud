package codes.recursive.controller;

import codes.recursive.client.CodPublicClient;
import codes.recursive.model.Brain;
import codes.recursive.model.BrainSession;
import codes.recursive.model.Game;
import codes.recursive.repository.AbstractBrainRepository;
import codes.recursive.repository.BrainRepository;
import codes.recursive.repository.BrainSessionRepository;
import codes.recursive.repository.GameRepository;
import codes.recursive.service.TokenService;
import com.oracle.bmc.vault.responses.UpdateSecretResponse;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
@SuppressWarnings("rawtypes")
public class ApiController {
    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    private final CodPublicClient codPublicClient;
    private final GameRepository gameRepository;
    private final BrainRepository brainRepository;
    private final AbstractBrainRepository abstractBrainRepository;
    private final BrainSessionRepository brainSessionRepository;
    private final TokenService tokenService;

    public ApiController(
            CodPublicClient codPublicClient,
            GameRepository gameRepository,
            BrainRepository brainRepository,
            AbstractBrainRepository abstractBrainRepository,
            BrainSessionRepository brainSessionRepository,
            TokenService tokenService) {
        this.codPublicClient = codPublicClient;
        this.gameRepository = gameRepository;
        this.brainRepository = brainRepository;
        this.abstractBrainRepository = abstractBrainRepository;
        this.brainSessionRepository = brainSessionRepository;
        this.tokenService = tokenService;
    }

    @Get(uri = "/codLookups")
    public HttpResponse getCodLookups() {
        return HttpResponse.ok(
                codPublicClient.getLookupValues()
        );
    }

    @Get(uri = "/recentGames{/offsetParam}{/maxParam}")
    public Page<Game> getRecentGames(Optional<Integer> offsetParam, Optional<Integer> maxParam) {
        Integer offset = offsetParam.orElse(0);
        Integer max = maxParam.orElse(25);
        Pageable pageable = Pageable.from(offset, max);
        return gameRepository.findAll(pageable);
    }

    @Get(uri = "/highlightedGames{/offsetParam}{/maxParam}")
    public Page<Game> getHighlightedGames(Optional<Integer> offsetParam, Optional<Integer> maxParam) {
        Integer offset = offsetParam.orElse(0);
        Integer max = maxParam.orElse(25);
        Pageable pageable = Pageable.from(offset, max);
        return gameRepository.findAllByIsHighlightedEqual(true, pageable);
    }

    @Get(uri = "/gamesWithBrainReadings{/offsetParam}{/maxParam}")
    public Page<Game> getGamesWithBrainReadings(Optional<Integer> offsetParam, Optional<Integer> maxParam) {
        Integer offset = offsetParam.orElse(0);
        Integer max = maxParam.orElse(25);
        Pageable pageable = Pageable.from(offset, max);
        return gameRepository.findAllWithBrainReadings(pageable);
    }

    @Get(uri = "/brainForGame/{gameId}")
    public List<Brain> getBrainForMatch(Long gameId) {
        return brainRepository.getBrainForMatch(gameId);
    }

    @Put(uri = "/highlightGame/{matchId}")
    public HttpResponse highlightGame(Long matchId) {
        Optional<Game> result = gameRepository.findById(matchId);
        if(result.isPresent()) {
            Game game = result.get();
            gameRepository.updateIsHighlighted(game.getId(), true);
        }
        return HttpResponse.noContent();
    }

    @Put(uri = "/markGameDistracted/{matchId}")
    public HttpResponse markGameDistracted(Long matchId) {
        Optional<Game> result = gameRepository.findById(matchId);
        if(result.isPresent()) {
            Game game = result.get();
            gameRepository.updateIsDistracted(game.getId(), true);
        }
        return HttpResponse.noContent();
    }

    @Put(uri = "/notes/{matchId}", consumes = MediaType.TEXT_PLAIN)
    public HttpResponse saveNotes(@PathVariable Long matchId, @Body String notes) {
        gameRepository.updateNotes(matchId, notes);
        return HttpResponse.noContent();
    }

    @Post(uri = "/brainSession", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public HttpResponse saveBrainSession(@Body BrainSession brainSession) {
        brainSessionRepository.save(brainSession);
        return HttpResponse.ok();
    }

    @Post(uri = "/token", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public HttpResponse saveToken(Map<String, String> data) {
        /*
        expects a JSON object containing a token. ex: {"token": "OD15...."}
        */
        UpdateSecretResponse updateSecretResponse = tokenService.updateToken(data.get("token"));
        return HttpResponse.ok();
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get(uri = "/brainSessionDetails/{sessionId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse getBrainSessionDetails(Long sessionId) {
        return HttpResponse.ok(
                brainSessionRepository.listBrainSessionDetails(sessionId)
        );
    }

    @Delete(uri = "/brainSession/{brainSessionId}")
    public HttpResponse deleteBrainSession(Long brainSessionId) {
        abstractBrainRepository.deleteBrainByBrainSessionId(brainSessionId);
        brainSessionRepository.deleteById(brainSessionId);
        return HttpResponse.noContent();
    }
}