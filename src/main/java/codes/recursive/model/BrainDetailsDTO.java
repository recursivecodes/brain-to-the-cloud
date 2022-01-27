package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.time.ZonedDateTime;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class BrainDetailsDTO {
    private Long id;
    private Integer attention;
    private Integer meditation;
    private Integer delta;
    private Integer theta;
    private Integer lowAlpha;
    private Integer highAlpha;
    private Integer lowBeta;
    private Integer highBeta;
    private Integer lowGamma;
    private Integer highGamma;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime createdOn;
}
