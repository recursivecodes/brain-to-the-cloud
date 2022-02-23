package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.model.DataType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Map;

@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Game {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @TypeDef(type = DataType.JSON)
    @JsonIgnore
    private String match;
    @Size(max = 1000)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String notes;
    private Boolean isHighlighted = false;
    private Boolean isDistracted = false;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @DateCreated
    private Date createdOn;

    public Game(Map match) throws JsonProcessingException {
        this.match = new ObjectMapper().writeValueAsString(match);
    }

    public Game(String match) {
        this.match = match;
    }

    @JsonProperty("match")
    public Match getMatchSerialized() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(match, Match.class);
    }
}
