package codes.recursive.controller;

import codes.recursive.client.CodPublicClient;
import codes.recursive.model.*;
import codes.recursive.repository.BrainSessionRepository;
import codes.recursive.repository.GameRepository;
import codes.recursive.service.CodPublicClientService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.naming.NameUtils;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;

@Controller("/reports")
@Secured(SecurityRule.IS_ANONYMOUS)
@SuppressWarnings({"rawtypes", "unused", "unchecked", "Duplicates"})
public class ReportController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);
    private final GameRepository gameRepository;
    private final BrainSessionRepository brainSessionRepository;
    private final CodPublicClientService codPublicClientService;

    public ReportController(
            GameRepository gameRepository,
            CodPublicClient codPublicClient,
            BrainSessionRepository brainSessionRepository,
            CodPublicClientService codPublicClientService) {
        this.gameRepository = gameRepository;
        this.brainSessionRepository = brainSessionRepository;
        this.codPublicClientService = codPublicClientService;
    }

    //@Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/game-details{/offsetParam}{/maxParam}{/filter}{/selectedGame}")
    ModelAndView gameDetails(@Nullable Principal principal, @PathVariable @Nullable Integer offsetParam, @PathVariable @Nullable Integer maxParam, @PathVariable @Nullable String filter, @PathVariable @Nullable String selectedGame) {
        String game = selectedGame != null ? selectedGame : CallOfDuty.VANGUARD;
        int offset = offsetParam != null ? offsetParam : 0;
        int max = maxParam != null ? maxParam : 25;
        Pageable pageable = Pageable.from(offset, max);
        Page<GameDetailDTO> page;

        if(filter == null || filter.equals("all")) {
            page = gameRepository.listGameDetails(pageable, game);
        }
        else {
            switch (filter) {
                case "highlight":
                    page = gameRepository.listHighlightedGameDetails(pageable, game);
                    break;
                case "brain":
                    page = gameRepository.listGameDetailsWithBrain(pageable, game);
                    break;
                default:
                    page = gameRepository.listGameDetails(pageable, game);
                    break;
            }
        }

        GameDetailDTOCollection collection = GameDetailDTOCollection.builder()
                .gameDetailDTOList(page.getContent())
                .codLookups(codPublicClientService.getCodLookups())
                .build();
        return new ModelAndView("game-details", CollectionUtils.mapOf(
                "collection", collection,
                "vanguard", CallOfDuty.VANGUARD,
                "games", CallOfDuty.GAMES,
                "selectedGame", game,
                "gamePage", page,
                "currentView", "game-details",
                "isLoggedIn", principal != null,
                "currentOffset", offset,
                "currentMax", max,
                "startRecord", page.getOffset() + 1,
                "endRecord", page.getNumberOfElements() + page.getOffset(),
                "filter", filter
        ));
    }

    @Get(uri = "/attention-meditation{/selectedGame}")
    ModelAndView timeMovingReport(@Nullable Principal principal, @PathVariable @Nullable String selectedGame) {
        String game = selectedGame != null ? selectedGame : CallOfDuty.VANGUARD;
        RangeSummaryDTOCollection attentionCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getBrainSummary("attention", game)).build();
        RangeSummaryDTOCollection meditationCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getBrainSummary("meditation", game)).build();
        RangeSummaryDTOCollection ratioCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getBrainSummary("ratio", game)).build();

        return new ModelAndView("attention-meditation-report", CollectionUtils.mapOf(
                "currentView", "attention-meditation",
                "games", CallOfDuty.GAMES,
                "selectedGame", game,
                "isLoggedIn", principal != null,
                "attentionCollection", attentionCollection,
                "meditationCollection", meditationCollection,
                "ratioCollection", ratioCollection
        ));
    }

    @Get(uri = "/time-moving{/selectedGame}")
    ModelAndView attentionMeditationSummary(@Nullable Principal principal, @PathVariable @Nullable String selectedGame) {
        String game = selectedGame != null ? selectedGame : CallOfDuty.VANGUARD;
        RangeSummaryDTOCollection summaryCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getTimeMovingSummary(0, game)).build();
        RangeSummaryDTOCollection brainSummaryCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getTimeMovingSummary(1, game)).build();

        return new ModelAndView("time-moving-report", CollectionUtils.mapOf(
                "currentView", "time-moving",
                "games", CallOfDuty.GAMES,
                "selectedGame", game,
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection,
                "brainSummaryCollection", brainSummaryCollection
        ));
    }

    @Get(uri = "/game-summary/{withBrain}{/selectedGame}")
    ModelAndView overallSummary(@Nullable Principal principal, @PathVariable Boolean withBrain, @PathVariable @Nullable String selectedGame) {
        String game = selectedGame != null ? selectedGame : CallOfDuty.VANGUARD;
        GameSummaryDTOCollection summaryCollection = GameSummaryDTOCollection.builder()
                .gameSummaryDTOList(gameRepository.getGameSummary(withBrain ? 1 : 0, game))
                .build();

        return new ModelAndView("game-summary", CollectionUtils.mapOf(
                "currentView", withBrain ? "game-summary-with-brain" : "game-summary",
                "games", CallOfDuty.GAMES,
                "selectedGame", game,
                "withBrain", withBrain,
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection
        ));
    }

    @Get(uri = "/game-summary-by-type/{type}{/selectedGame}")
    ModelAndView summaryByType(@Nullable Principal principal, @PathVariable String type, @PathVariable @Nullable String selectedGame) {
        String game = selectedGame != null ? selectedGame : CallOfDuty.VANGUARD;
        Boolean withBrain = type.toLowerCase().contains("withbrain");
        String viewSuffix = NameUtils.hyphenate(type);
        String currentView = "game-summary-" + viewSuffix;
        String viewFriendlyName = WordUtils.capitalizeFully(viewSuffix.replaceAll("-", " "));
        String groupingType = type.toLowerCase().replace("by", "").replace("withbrain", "");

        List<GameSummaryDTO> summaries = gameRepository.getGameSummaryByType(groupingType, withBrain ? 1 : 0, game);
        GameSummaryDTOCollection summaryCollection = GameSummaryDTOCollection.builder()
                .gameSummaryDTOList(summaries)
                .codLookups(codPublicClientService.getCodLookups())
                .build();

        return new ModelAndView("game-summary-by-type", CollectionUtils.mapOf(
                "currentView", currentView,
                "games", CallOfDuty.GAMES,
                "selectedGame", game,
                "withBrain", withBrain,
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection,
                "vanguard", CallOfDuty.VANGUARD,
                "grouping", groupingType,
                "type", viewFriendlyName,
                "rawType", type
        ));
    }

    @Get(uri = "/recorded-sessions")
    ModelAndView recordedSessions(@Nullable Principal principal) {
        return new ModelAndView("recorded-sessions", CollectionUtils.mapOf(
                "currentView", "recorded-sessions",
                "isLoggedIn", principal != null,
                "sessionSummaries", brainSessionRepository.listBrainSessionSummaries()
        ));
    }
}