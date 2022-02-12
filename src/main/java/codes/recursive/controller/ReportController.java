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
import java.util.Map;
import java.util.function.Function;

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
    @Get("/game-details{/offsetParam}{/maxParam}{/filter}")
    ModelAndView gameDetails(@Nullable Principal principal, @PathVariable @Nullable Integer offsetParam, @PathVariable @Nullable Integer maxParam, @PathVariable @Nullable String filter) {
        int offset = offsetParam != null ? offsetParam : 0;
        int max = maxParam != null ? maxParam : 25;
        Pageable pageable = Pageable.from(offset, max);
        Page<GameDetailDTO> page;

        if(filter == null || filter.equals("all")) {
            page = gameRepository.listGameDetails(pageable);
        }
        else {
            switch (filter) {
                case "highlight":
                    page = gameRepository.listHighlightedGameDetails(pageable);
                    break;
                case "brain":
                    page = gameRepository.listGameDetailsWithBrain(pageable);
                    break;
                default:
                    page = gameRepository.listGameDetails(pageable);
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

    @Get(uri = "/attention-meditation")
    ModelAndView timeMovingReport(@Nullable Principal principal) {
        RangeSummaryDTOCollection attentionCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getBrainSummary("attention")).build();
        RangeSummaryDTOCollection meditationCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getBrainSummary("meditation")).build();
        RangeSummaryDTOCollection ratioCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getBrainSummary("ratio")).build();

        return new ModelAndView("attention-meditation-report", CollectionUtils.mapOf(
                "currentView", "attention-meditation",
                "isLoggedIn", principal != null,
                "attentionCollection", attentionCollection,
                "meditationCollection", meditationCollection,
                "ratioCollection", ratioCollection
        ));
    }

    @Get(uri = "/time-moving")
    ModelAndView attentionMeditationSummary(@Nullable Principal principal) {
        RangeSummaryDTOCollection summaryCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getTimeMovingSummary(0)).build();
        RangeSummaryDTOCollection brainSummaryCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getTimeMovingSummary(1)).build();

        return new ModelAndView("time-moving-report", CollectionUtils.mapOf(
                "currentView", "time-moving",
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection,
                "brainSummaryCollection", brainSummaryCollection
        ));
    }

    @Get(uri = "/game-summary/{withBrain}")
    ModelAndView overallSummary(@Nullable Principal principal, @PathVariable Boolean withBrain) {
        GameSummaryDTOCollection summaryCollection = GameSummaryDTOCollection.builder()
                .gameSummaryDTOList(gameRepository.getGameSummary(withBrain ? 1 : 0))
                .build();

        return new ModelAndView("game-summary", CollectionUtils.mapOf(
                "currentView", withBrain ? "game-summary-with-brain" : "game-summary",
                "withBrain", withBrain,
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection
        ));
    }

    public Map<String, Function<GameRepository, List<GameSummaryDTO>>> gameSummaryTypes = Map.ofEntries(
        Map.entry("byMap", GameRepository::getGameSummaryByMap),
        Map.entry("byMode", GameRepository::getGameSummaryByMode),
        Map.entry("byOperator", GameRepository::getGameSummaryByOperator),
        Map.entry("byTeam", GameRepository::getGameSummaryByTeam),
        Map.entry("byMapWithBrain", GameRepository::getGameSummaryByMapWithBrain),
        Map.entry("byModeWithBrain", GameRepository::getGameSummaryByModeWithBrain),
        Map.entry("byOperatorWithBrain", GameRepository::getGameSummaryByOperatorWithBrain),
        Map.entry("byTeamWithBrain", GameRepository::getGameSummaryByTeamWithBrain)
    );

    @Get(uri = "/game-summary-by-type/{type}")
    ModelAndView summaryByType(@Nullable Principal principal, @PathVariable String type) {
        Boolean withBrain = type.toLowerCase().contains("withbrain");
        String viewSuffix = NameUtils.hyphenate(type);
        String currentView = "game-summary-" + viewSuffix;
        String viewFriendlyName = WordUtils.capitalizeFully(viewSuffix.replaceAll("-", " "));
        String groupingType = type.toLowerCase().replace("by", "").replace("withbrain", "");

        List<GameSummaryDTO> summaries = gameRepository.getGameSummaryByType(groupingType, withBrain ? 1 : 0);
        GameSummaryDTOCollection summaryCollection = GameSummaryDTOCollection.builder()
                .gameSummaryDTOList(summaries)
                .codLookups(codPublicClientService.getCodLookups())
                .build();

        return new ModelAndView("game-summary-by-type", CollectionUtils.mapOf(
                "currentView", currentView,
                "withBrain", withBrain,
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection,
                "vanguard", CallOfDuty.VANGUARD,
                "grouping", groupingType,
                "type", viewFriendlyName
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