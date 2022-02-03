package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class BrainSummaryDTO {
    private Long id;
    private String name;
    private String notes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime startedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime endedAt;
    private BigDecimal avgAttention;
    private BigDecimal avgMeditation;
    private BigDecimal avgDelta;
    private BigDecimal avgTheta;
    private BigDecimal avgLowAlpha;
    private BigDecimal avgHighAlpha;
    private BigDecimal avgLowBeta;
    private BigDecimal avgHighBeta;
    private BigDecimal avgLowGamma;
    private BigDecimal avgHighGamma;

    public double getLog(BigDecimal val) {
        return Math.log(val.doubleValue());
    }

    public String getStartedAtLocal() {
        ZonedDateTime atLocal = startedAt.withZoneSameInstant(ZoneId.of("America/New_York"));
        return atLocal.format(DateTimeFormatter.ofPattern( "MM/dd/yy hh:mm a z" ));
    }

    public String getEndedAtAtLocal() {
        ZonedDateTime atLocal = endedAt.withZoneSameInstant(ZoneId.of("America/New_York"));
        return atLocal.format(DateTimeFormatter.ofPattern( "MM/dd/yy hh:mm a z" ));
    }
}
