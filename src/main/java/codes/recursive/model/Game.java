package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.model.DataType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Map;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @TypeDef(type = DataType.JSON)
    private String match;
    private Boolean isHighlighted = false;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @DateCreated
    private Date createdOn;

    @Creator
    public Game(Long id, String match, Boolean isHighlighted, @Nullable Date createdOn) {
        this.id = id;
        this.match = match;
        this.isHighlighted = isHighlighted;
        this.createdOn = createdOn;
    }

    public Game(Map match) throws JsonProcessingException {
        this.match = new ObjectMapper().writeValueAsString(match);
    }

    public Game(String match) {
        this.match = match;
    }

    public Game() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
