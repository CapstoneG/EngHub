package ptit.com.enghub.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ptit.com.enghub.config.RabbitMQConfig;
import ptit.com.enghub.dto.ReminderMessage;

@Component
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendReminder(ReminderMessage msg) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                msg);
    }
}
