package codes.recursive.controller;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.security.Principal;
import java.util.Map;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/login")
public class AuthController {

    @Get("/auth")
    @View("auth")
    public Map auth(@Nullable Principal principal) {
        return CollectionUtils.mapOf(
                "currentView", "auth",
                "isLoggedIn", principal != null
        );
    }

    @Get("/authFailed")
    @View("auth")
    public Map authFailed(@Nullable Principal principal) {
        return CollectionUtils.mapOf(
                "currentView", "auth",
                "isLoggedIn", principal != null,
                "errors", true
        );
    }
}
