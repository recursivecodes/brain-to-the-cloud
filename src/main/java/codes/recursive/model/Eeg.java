package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

/**
 * Delta (1-3Hz): sleep
 * Theta (4-7Hz): relaxed, meditative
 * Low Alpha (8-9Hz): eyes closed, relaxed
 * High Alpha (10-12Hz)
 * Low Beta (13-17Hz): alert, focused
 * High Beta (18-30Hz)
 * Low Gamma (31-40Hz): multi-sensory processing
 * High Gamma (41-50Hz)
 */

@Introspected
public class Eeg {
    @JsonProperty("signalStrength") public Integer signalStrength;
    @JsonProperty("attention") public Integer attention;
    @JsonProperty("meditation") public Integer meditation;
    @JsonProperty("delta") public Integer delta;
    @JsonProperty("theta") public Integer theta;
    @JsonProperty("lowAlpha") public Integer lowAlpha;
    @JsonProperty("highAlpha") public Integer highAlpha;
    @JsonProperty("lowBeta") public Integer lowBeta;
    @JsonProperty("highBeta") public Integer highBeta;
    @JsonProperty("lowGamma") public Integer lowGamma;
    @JsonProperty("highGamma") public Integer highGamma;

    public Eeg(
            @JsonProperty("signalStrength") Integer signalStrength,
            @JsonProperty("attention") Integer attention,
            @JsonProperty("meditation") Integer meditation,
            @JsonProperty("delta") Integer delta,
            @JsonProperty("theta") Integer theta,
            @JsonProperty("lowAlpha") Integer lowAlpha,
            @JsonProperty("highAlpha") Integer highAlpha,
            @JsonProperty("lowBeta") Integer lowBeta,
            @JsonProperty("highBeta") Integer highBeta,
            @JsonProperty("lowGamma") Integer lowGamma,
            @JsonProperty("highGamma") Integer highGamma) {
               this.signalStrength = signalStrength;
               this.attention = attention;
               this.meditation = meditation;
               this.delta = delta;
               this.theta = theta;
               this.lowAlpha = lowAlpha;
               this.highAlpha = highAlpha;
               this.lowBeta = lowBeta;
               this.highBeta = highBeta;
               this.lowGamma = lowGamma;
               this.highGamma = highGamma;
    }

}
