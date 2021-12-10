package codes.recursive.controller;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller()
@SuppressWarnings("rawtypes")
public class DefaultController {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultController.class);


    public DefaultController() {
    }

    @Get()
    ModelAndView matchAnalysis() {
        return new ModelAndView("match-analysis", CollectionUtils.mapOf("currentView", "match-analysis"));
    }

    @Get(uri ="/live")
    ModelAndView live() {
        return new ModelAndView("live", CollectionUtils.mapOf("currentView", "live"));
    }

}