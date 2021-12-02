package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.model.DataType;

import javax.persistence.*;
import java.sql.Date;
import java.util.Map;

@Entity
public class Brain {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @TypeDef(type = DataType.JSON)
    private String data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @DateCreated
    private Date createdOn;

    @Creator
    public Brain(Integer id, String data, @Nullable Date createdOn) {
        this.id = id;
        this.data = data;
        this.createdOn = createdOn;
    }

    public Brain(Map reading) throws JsonProcessingException {
        this.data = new ObjectMapper().writeValueAsString(reading);
    }

    public Brain(String reading) {
        this.data = reading;
    }

    public Brain() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map getReadingAsMap() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, Map.class);
    }

    public Eeg getEeg() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, Eeg.class);
    }

    public String getData() {
        return data;
    }

    public void setData(String reading) {
        this.data = reading;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
