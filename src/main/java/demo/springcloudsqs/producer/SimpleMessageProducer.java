package demo.springcloudsqs.producer;

import demo.springcloudsqs.model.Event;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static demo.springcloudsqs.config.AWSConfigConstants.ORDER_CREATED_TOPIC;

@Slf4j
@Service
public class SimpleMessageProducer {

  @Autowired private NotificationMessagingTemplate notificationMessagingTemplate;

  /*
   * We use the NotificationMessagingTemplate created in the config above.
   * It gives us several methods to send messages on an SNS topic.
   * We will use the default convertAndSend() method
   * that takes care of converting our object to a Message
   * using the MessageConverter that we provide in the SqsConfig class.
   * */

  public void publish(Event event) {
    notificationMessagingTemplate.convertAndSend(ORDER_CREATED_TOPIC, event);
  }
}
