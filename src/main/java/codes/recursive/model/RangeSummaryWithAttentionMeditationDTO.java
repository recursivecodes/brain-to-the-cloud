package codes.recursive.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
public class RangeSummaryWithAttentionMeditationDTO extends RangeSummaryDTO {
    private Double avgAttention;
    private Double avgMeditation;
}
