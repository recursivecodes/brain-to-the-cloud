package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.data.annotation.DateCreated;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

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

@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Brain {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY) private Long id;
    @JsonProperty("signalStrength") private Integer signalStrength;
    @JsonProperty("attention") private Integer attention;
    @JsonProperty("meditation") private Integer meditation;
    @JsonProperty("delta") private Integer delta;
    @JsonProperty("theta") private Integer theta;
    @JsonProperty("lowAlpha") private Integer lowAlpha;
    @JsonProperty("highAlpha") private Integer highAlpha;
    @JsonProperty("lowBeta") private Integer lowBeta;
    @JsonProperty("highBeta") private Integer highBeta;
    @JsonProperty("lowGamma") private Integer lowGamma;
    @JsonProperty("highGamma") private Integer highGamma;
    @JsonProperty("isDistracted") private Boolean isDistracted = false;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @DateCreated private Date createdOn;
}
