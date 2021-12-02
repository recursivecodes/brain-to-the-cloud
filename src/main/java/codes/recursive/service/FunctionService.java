package codes.recursive.service;

import com.oracle.bmc.functions.FunctionsInvokeClient;
import com.oracle.bmc.functions.requests.InvokeFunctionRequest;
import com.oracle.bmc.functions.responses.InvokeFunctionResponse;
import com.oracle.bmc.util.StreamUtils;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.Introspected;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
@Introspected
public class FunctionService {
    private static final Logger LOG = LoggerFactory.getLogger(FunctionService.class);
    
    private final FunctionsInvokeClient functionsInvokeClient;

    public FunctionService(
            @Property(name = "codes.recursive.functions.recent-matches-invoke-endpoint") String invokeEndpoint,
            FunctionsInvokeClient functionsInvokeClient) {
        this.functionsInvokeClient = functionsInvokeClient;
        this.functionsInvokeClient.setEndpoint(invokeEndpoint);
        LOG.info("Invoke Endpoint: {}", invokeEndpoint);
    }

    public String invokeFunction(String functionId, String payload) throws IOException {
        /* create invoke function request */
        InvokeFunctionRequest recentMatchesRequest = InvokeFunctionRequest.builder()
                .functionId(functionId)
                .invokeFunctionBody(
                        StreamUtils.createByteArrayInputStream(payload.getBytes())
                )
                .build();
        /* invoke the function and parse the response */
        InvokeFunctionResponse invokeFunctionResponse = functionsInvokeClient.invokeFunction(recentMatchesRequest);
        return IOUtils.toString(invokeFunctionResponse.getInputStream(), StandardCharsets.UTF_8);
    }
}
