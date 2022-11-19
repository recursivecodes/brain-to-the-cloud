package codes.recursive.task;

import codes.recursive.client.CodAuthClient;
import codes.recursive.model.CallOfDuty;
import codes.recursive.model.Game;
import codes.recursive.model.Match;
import codes.recursive.repository.AbstractGameRepository;
import codes.recursive.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
@Requires(notEnv="test")
@SuppressWarnings("unchecked")
public class RecentGames {
    private static final Logger LOG = LoggerFactory.getLogger(RecentGames.class);
    private final String gamerTag;
    private final GameRepository gameRepository;
    private final AbstractGameRepository abstractGameRepository;
    private final CodAuthClient codAuthClient;

    public RecentGames(
            @Property(name = "codes.recursive.xbox-gamertag") String gamerTag,
            GameRepository gameRepository,
            AbstractGameRepository abstractGameRepository,
            CodAuthClient codAuthClient) {
        this.gamerTag = gamerTag;
        this.gameRepository = gameRepository;
        this.abstractGameRepository = abstractGameRepository;
        this.codAuthClient = codAuthClient;
    }

    @Scheduled(fixedDelay = "1h")
    public void getAllRecentMatchData() {
        getRecentMatchData(CallOfDuty.VANGUARD);
        getRecentMatchData(CallOfDuty.MODERN_WARFARE_II);
    }

    public void getRecentMatchData(String codGame) {
        LOG.info("Running getRecentMatchData for: {}", codGame);

        /* generate the payload */
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Map<String, Object> matchDetails = codAuthClient.getMatchDetails(codGame, CallOfDuty.XBL, gamerTag);
        Map<String, Object> recentMatches = (Map<String, Object>) matchDetails.get("data");

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
                                game.setGame(codGame);
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
}
