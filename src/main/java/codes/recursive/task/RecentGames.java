package codes.recursive.task;

import codes.recursive.model.Game;
import codes.recursive.model.Match;
import codes.recursive.repository.AbstractGameRepository;
import codes.recursive.repository.GameRepository;
import codes.recursive.service.FunctionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
@Requires(notEnv="test")
public class RecentGames {
    private static final Logger LOG = LoggerFactory.getLogger(RecentGames.class);

    private final String recentMatchesFnOcid;
    private final String gamerTag;
    private final FunctionService functionService;
    private final GameRepository gameRepository;
    private final AbstractGameRepository abstractGameRepository;
    private String activisionToken = "ODI5NjQ2NDoxNjM5NDI5NDQ2MDMzOjg5ZmRjNGU3YzM2YmI5MDA3MmZhZmRjZWQ1ZmM3YWM1";

    public RecentGames(
            @Property(name = "codes.recursive.functions.recent-matches-ocid") String recentMatchesFnOcid,
            @Property(name = "codes.recursive.xbox-gamertag") String gamerTag,
            FunctionService functionService,
            GameRepository gameRepository,
            AbstractGameRepository abstractGameRepository) {
        this.recentMatchesFnOcid = recentMatchesFnOcid;
        this.gamerTag = gamerTag;
        this.functionService = functionService;
        this.gameRepository = gameRepository;
        this.abstractGameRepository = abstractGameRepository;
    }

    public void setActivisionToken(String activisionToken) {
        this.activisionToken = activisionToken;
    }

    @Scheduled(fixedDelay = "1h")
    public void getRecentMatchData() throws IOException {
        if(activisionToken != null) {
            LOG.info("Running getRecentMatchData");
            LOG.info("Token: {}", activisionToken);
            LOG.info("OCID: {}", recentMatchesFnOcid);

            /* when using Java 11+, force TLS 1.2 */
            System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

            /* generate the payload */
            Map<String, String> payloadMap = new HashMap<>();
            payloadMap.put("gamertag", gamerTag);
            payloadMap.put("token", activisionToken);
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            String payload = mapper.writeValueAsString(payloadMap);
            /* invoke the serverless function */
            String recentMatchesResponse = functionService.invokeFunction(recentMatchesFnOcid, payload);

            /* deserialize the result */
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
            HashMap<String, Object> recentMatches = mapper.readValue(recentMatchesResponse, typeRef);

            /* loop over list of matches and persist each match if necessary */
            if(recentMatches.containsKey("matches")) {
                LOG.info("Persisting match info...");
                /* serialize JSON list of matches to a List<Match> */
                TypeReference<List<Match>> matchListTypeRef = new TypeReference<>() {};
                List<Match> matchList = mapper.convertValue(recentMatches.get("matches"), matchListTypeRef);
                matchList
                        .forEach(match -> {
                            LOG.info("Processing game with matchID: {}", match.matchID);
                            Boolean matchExists = abstractGameRepository.matchExists(match.matchID);
                            if(!matchExists) {
                                LOG.info("Game does not exist...");
                                String matchJson;
                                try {
                                    matchJson = mapper.writeValueAsString(match);
                                    Game game = new Game(matchJson);
                                    LOG.info("Persisting game with matchID: {}", match.matchID);
                                    gameRepository.saveAsync(game);
                                    LOG.info("Game persisted!");
                                }
                                catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                LOG.info("Game exists, skipping persistence!");
                            }
                        });
            }
            else {
                LOG.warn("API result did not contain any match info.");
            }
        }
        else {
            LOG.info("No token was set. Not running.");
        }
    }
}
