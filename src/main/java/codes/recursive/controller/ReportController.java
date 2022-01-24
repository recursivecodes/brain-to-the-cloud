package codes.recursive.controller;

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

@Controller("/reports")
@Secured(SecurityRule.IS_ANONYMOUS)
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class ReportController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);
    private final GameRepository gameRepository;

    public ReportController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Get(uri ="/attention-meditation")
    ModelAndView timeMovingReport(@Nullable Principal principal) {
        return new ModelAndView("attention-meditation-report", CollectionUtils.mapOf(
                "currentView", "attention-meditation-report",
                "isLoggedIn", principal != null,
                "attentionSummary", gameRepository.getAttentionSummary(),
                "meditationSummary", gameRepository.getMeditationSummary()
        ));
    }

    @Get(uri ="/time-moving")
    ModelAndView attentionMeditationSummary(@Nullable Principal principal) {
        return new ModelAndView("time-moving-report", CollectionUtils.mapOf(
                "currentView", "time-moving-report",
                "isLoggedIn", principal != null,
                "timeMovingSummary", gameRepository.getTimeMovingSummary(),
                "timeMovingWithBrainSummary", gameRepository.getTimeMovingWithBrainDataSummary()
        ));
    }

}