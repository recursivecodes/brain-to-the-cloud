package codes.recursive.messaging;

import codes.recursive.model.Brain;
import codes.recursive.repository.BrainRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.mqtt.annotation.MqttSubscriber;
import io.micronaut.mqtt.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@MqttSubscriber
public class BrainConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(BrainConsumer.class);
    private final BrainRepository brainRepository;

    public BrainConsumer(BrainRepository brainRepository) {
        this.brainRepository = brainRepository;
    }

    @Topic("bttc/brain")
    public void receive(Map<String, Object> data) throws JsonProcessingException {
        LOG.info("Persisting brain data...");
        Brain brain = new Brain(data);
        brainRepository.saveAsync(brain);
        LOG.info("Brain data persisted!");
    }

}
