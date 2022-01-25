package codes.recursive.controller;

import codes.recursive.client.CodPublicClient;
import codes.recursive.model.CallOfDuty;
import codes.recursive.model.GameSummaryDTOCollection;
import codes.recursive.model.RangeSummaryDTOCollection;
import codes.recursive.repository.GameRepository;
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
import java.util.Map;

@Controller("/reports")
@Secured(SecurityRule.IS_ANONYMOUS)
@SuppressWarnings({"rawtypes", "unused", "unchecked", "Duplicates"})
public class ReportController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);
    private final GameRepository gameRepository;
    private final CodPublicClient codPublicClient;

    public ReportController(GameRepository gameRepository, CodPublicClient codPublicClient) {
        this.gameRepository = gameRepository;
        this.codPublicClient = codPublicClient;
    }

    @Get(uri = "/attention-meditation")
    ModelAndView timeMovingReport(@Nullable Principal principal) {
        /*
        gameRepository.getAttentionSummary()
            .stream()
            .map(m -> m.kdRatio)
            .max(Double::compare)
         */
        RangeSummaryDTOCollection attentionCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getAttentionSummary()).build();
        RangeSummaryDTOCollection meditationCollection = RangeSummaryDTOCollection.builder().rangeSummaryDTOList(gameRepository.getMeditationSummary()).build();

        return new ModelAndView("attention-meditation-report", CollectionUtils.mapOf(
                "currentView", "attention-meditation-report",
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
                "currentView", "time-moving-report",
                "isLoggedIn", principal != null,
                "summaryCollection", summaryCollection,
                "brainSummaryCollection", brainSummaryCollection
        ));
    }

    @Get(uri = "/game-summary")
    ModelAndView overallSummary(@Nullable Principal principal) {
        Map<String, Object> lookupValues = codPublicClient.getLookupValues();
        GameSummaryDTOCollection summaryCollection = GameSummaryDTOCollection.builder()
                .gameSummaryDTOList(gameRepository.getGameSummary())
                .codLookups(lookupValues)
                .build();
        GameSummaryDTOCollection summaryByMapCollection = GameSummaryDTOCollection.builder()
                .gameSummaryDTOList(gameRepository.getGameSummaryByMap())
                .codLookups(lookupValues)
                .build();

        return new ModelAndView("game-summary", CollectionUtils.mapOf(
                "currentView", "time-moving-report",
                "isLoggedIn", principal != null,
                "summary", summaryCollection.getGameSummaryDTOList(),
                "summaryByMap", summaryByMapCollection.getGameSummaryDTOList(),
                "summaryCollection", summaryCollection,
                "summaryByMapCollection", summaryByMapCollection,
                "vanguard", CallOfDuty.VANGUARD
        ));
    }

}