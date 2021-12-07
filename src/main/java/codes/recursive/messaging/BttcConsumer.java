package codes.recursive.messaging;

import codes.recursive.model.Brain;
import codes.recursive.repository.BrainRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.mqtt.annotation.MqttSubscriber;
import io.micronaut.mqtt.annotation.Topic;
import io.micronaut.websocket.WebSocketBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@MqttSubscriber
public class BttcConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(BttcConsumer.class);
    private final BrainRepository brainRepository;
    private final WebSocketBroadcaster broadcaster;

    public BttcConsumer(
            BrainRepository brainRepository,
            WebSocketBroadcaster broadcaster) {
        this.brainRepository = brainRepository;
        this.broadcaster = broadcaster;
    }

    @Topic("bttc/brain")
    public void receiveBrain(Map<String, Object> data) {
        LOG.info("Persisting brain data...");
        ObjectMapper mapper = new ObjectMapper();
        Brain brain = mapper.convertValue(data, Brain.class);
        brainRepository.saveAsync(brain);
        LOG.info("Brain data persisted!");
    }

    @Topic("bttc/demo")
    public void receiveDemo(Map<String, Object> data) {
        LOG.info("Persisting brain data for demo...");
        ObjectMapper mapper = new ObjectMapper();
        Brain brain = mapper.convertValue(data, Brain.class);
        broadcaster.broadcastSync(brain);
        LOG.info("Brain data broadcast!");
    }
}
