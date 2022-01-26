package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Map;

@Introspected
public class GameDTO {
    private Long id;
    @JsonIgnore
    private String match;
    @Size(max = 1000)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String notes;
    private Boolean isHighlighted = false;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date createdOn;

    @Creator
    public GameDTO(Long id, String match, String notes, Boolean isHighlighted, Date createdOn) {
        this.id = id;
        this.match = match;
        this.notes = notes;
        this.isHighlighted = isHighlighted;
        this.createdOn = createdOn;
    }

    public GameDTO(Map match) throws JsonProcessingException {
        this.match = new ObjectMapper().writeValueAsString(match);
    }

    public GameDTO(String match) {
        this.match = match;
    }

    public GameDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("match")
    public Match getMatchSerialized() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(match, Match.class);
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String data) {
        this.match = data;
    }

    public Boolean getIsHighlighted() {
        return isHighlighted;
    }

    public void setIsHighlighted(Boolean highlighted) {
        isHighlighted = highlighted;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
