package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.math.BigInteger;

@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
    @JsonProperty("mostKilled") public String mostKilled;
    @JsonProperty("nemesis") public String nemesis;
    @JsonProperty("operator") public String operator;
    @JsonProperty("operatorExecution") public String operatorExecution;
    @JsonProperty("operatorSkinId") public BigInteger operatorSkinId;
    @JsonProperty("platform") public String platform;
    @JsonProperty("team") public String team;

    @JsonCreator
    public Player(@JsonProperty("mostKilled")  String mostKilled,
                    @JsonProperty("nemesis")  String nemesis,
                    @JsonProperty("operator")  String operator,
                    @JsonProperty("operatorExecution")  String operatorExecution,
                    @JsonProperty("operatorSkinId")  BigInteger operatorSkinId,
                    @JsonProperty("platform")  String platform,
                    @JsonProperty("team")  String team) {
        this.mostKilled = mostKilled;
        this.nemesis = nemesis;
        this.operator = operator;
        this.operatorExecution = operatorExecution;
        this.operatorSkinId = operatorSkinId;
        this.platform = platform;
        this.team = team;
    }
}
