package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Creator;
import io.micronaut.data.annotation.DateCreated;

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

    @Creator
    public Brain(
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
            @JsonProperty("highGamma") Integer highGamma,
            @JsonProperty("isDistracted") Boolean isDistracted) {
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
               this.isDistracted = isDistracted;
    }

    public Brain() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Integer getAttention() {
        return attention;
    }

    public void setAttention(Integer attention) {
        this.attention = attention;
    }

    public Integer getMeditation() {
        return meditation;
    }

    public void setMeditation(Integer meditation) {
        this.meditation = meditation;
    }

    public Integer getDelta() {
        return delta;
    }

    public void setDelta(Integer delta) {
        this.delta = delta;
    }

    public Integer getTheta() {
        return theta;
    }

    public void setTheta(Integer theta) {
        this.theta = theta;
    }

    public Integer getLowAlpha() {
        return lowAlpha;
    }

    public void setLowAlpha(Integer lowAlpha) {
        this.lowAlpha = lowAlpha;
    }

    public Integer getHighAlpha() {
        return highAlpha;
    }

    public void setHighAlpha(Integer highAlpha) {
        this.highAlpha = highAlpha;
    }

    public Integer getLowBeta() {
        return lowBeta;
    }

    public void setLowBeta(Integer lowBeta) {
        this.lowBeta = lowBeta;
    }

    public Integer getHighBeta() {
        return highBeta;
    }

    public void setHighBeta(Integer highBeta) {
        this.highBeta = highBeta;
    }

    public Integer getLowGamma() {
        return lowGamma;
    }

    public void setLowGamma(Integer lowGamma) {
        this.lowGamma = lowGamma;
    }

    public Integer getHighGamma() {
        return highGamma;
    }

    public void setHighGamma(Integer highGamma) {
        this.highGamma = highGamma;
    }

    public Boolean getIsDistracted() {
        return isDistracted;
    }

    public void setIsDistracted(Boolean distracted) {
        isDistracted = distracted;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
