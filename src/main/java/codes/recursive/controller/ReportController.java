package codes.recursive.controller;

import codes.recursive.model.RangeSummaryDTO;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller("/reports")
@Secured(SecurityRule.IS_ANONYMOUS)
@SuppressWarnings({"rawtypes", "unused", "unchecked", "Duplicates"})
public class ReportController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);
    private final GameRepository gameRepository;

    public ReportController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
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
                "attentionSummary", attentionCollection.getRangeSummaryDTOList(),
                "meditationSummary", meditationCollection.getRangeSummaryDTOList(),
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
                "timeMovingSummary", summaryCollection.getRangeSummaryDTOList(),
                "timeMovingWithBrainSummary", brainSummaryCollection.getRangeSummaryDTOList(),
                "summaryCollection", summaryCollection,
                "brainSummaryCollection", brainSummaryCollection
        ));
    }

}