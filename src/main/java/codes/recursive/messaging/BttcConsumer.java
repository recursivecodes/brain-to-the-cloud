package codes.recursive.messaging;

import codes.recursive.model.Brain;
import codes.recursive.repository.BrainRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Property;
import io.micronaut.mqtt.annotation.MqttSubscriber;
import io.micronaut.mqtt.annotation.Topic;
import io.micronaut.websocket.WebSocketBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ivs.IvsClient;

import java.util.Map;

@MqttSubscriber
public class BttcConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(BttcConsumer.class);
    private final BrainRepository brainRepository;
    private final WebSocketBroadcaster broadcaster;
    private final IvsClient ivsClient;
    private final String channelArn;

    public BttcConsumer(
            BrainRepository brainRepository,
            WebSocketBroadcaster broadcaster,
            @Property(name = "aws.accessKeyId") String accessKeyId,
            @Property(name = "aws.secretKey") String secretKey,
            @Property(name = "aws.region") String region,
            @Property(name = "codes.recursive.ivs.channel-arn") String channelArn) {
        this.brainRepository = brainRepository;
        this.broadcaster = broadcaster;
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretKey);
        this.ivsClient = IvsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .build();
        this.channelArn = channelArn;
    }

    @Topic("bttc/brain")
    public void receiveBrain(Map<String, Object> data) throws JsonProcessingException {
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
