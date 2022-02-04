package codes.recursive.controller;

import codes.recursive.client.CodPublicClient;
import codes.recursive.model.CallOfDuty;
import codes.recursive.model.GameSummaryDTO;
import codes.recursive.model.GameSummaryDTOCollection;
import codes.recursive.model.RangeSummaryDTOCollection;
import codes.recursive.repository.BrainSessionRepository;
import codes.recursive.repository.GameRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.naming.NameUtils;
import io.micronaut.core.util.CollectionUtils;
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
    private final CodPublicClient codPublicClient;
    private final BrainSessionRepository brainSessionRepository;

    public ReportController(
            GameRepository gameRepository,
            CodPublicClient codPublicClient,
            BrainSessionRepository brainSessionRepository) {
        this.gameRepository = gameRepository;
        this.codPublicClient = codPublicClient;
        this.brainSessionRepository = brainSessionRepository;
    }

    @Get(uri = "/attention-meditation")
    ModelAndView timeMovingReport(@Nullable Principal principal) {
        RangeSummaryDTOCollection attentionCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getAttentionSummary()).build();
        RangeSummaryDTOCollection meditationCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getMeditationSummary()).build();

        return new ModelAndView("attention-meditation-report", CollectionUtils.mapOf(
                "currentView", "attention-meditation",
                "isLoggedIn", principal != null,
                "attentionCollection", attentionCollection,
                "meditationCollection", meditationCollection
        ));
    }

    @Get(uri = "/time-moving")
    ModelAndView attentionMeditationSummary(@Nullable Principal principal) {
        RangeSummaryDTOCollection summaryCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getTimeMovingSummary()).build();
        RangeSummaryDTOCollection brainSummaryCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getTimeMovingWithBrainDataSummary()).build();

        return new ModelAndView("time-moving-report", CollectionUtils.mapOf(
                "currentView", "time-moving",
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection,
                "brainSummaryCollection", brainSummaryCollection
        ));
    }

    @Get(uri = "/game-summary")
    ModelAndView overallSummary(@Nullable Principal principal) {
        GameSummaryDTOCollection summaryCollection = GameSummaryDTOCollection.builder()
                .gameSummaryDTOList(gameRepository.getGameSummary())
                .build();

        return new ModelAndView("game-summary", CollectionUtils.mapOf(
                "currentView", "game-summary",
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

        GameSummaryDTOCollection summaryCollection = GameSummaryDTOCollection.builder()
                .gameSummaryDTOList(gameSummaryTypes.get(type).apply(gameRepository))
                .codLookups(codPublicClient.getLookupValues())
                .build();

        return new ModelAndView("game-summary-by-type", CollectionUtils.mapOf(
                "currentView", currentView,
                "withBrain", withBrain,
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection,
                "vanguard", CallOfDuty.VANGUARD,
                "grouping", type.toLowerCase().replace("by", "").replace("withbrain", ""),
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