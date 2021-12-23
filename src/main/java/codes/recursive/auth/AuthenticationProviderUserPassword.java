package codes.recursive.auth;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.inject.Singleton;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private final String username;
    private final String password;

    public AuthenticationProviderUserPassword(
            @Property(name = "codes.recursive.app-user") String username,
            @Property(name = "codes.recursive.app-password") String password
    ) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            if (authenticationRequest.getIdentity().equals(username) &&
                    authenticationRequest.getSecret().equals(password)) {
                emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity()));
                emitter.complete();
            } else {
                emitter.error(AuthenticationResponse.exception());
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }
}
