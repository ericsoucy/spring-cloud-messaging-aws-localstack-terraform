package demo.springcloudsqs.consumer;

import demo.springcloudsqs.model.Event;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import static demo.springcloudsqs.config.AWSConfigConstants.ORDER_QUEUE;

@Slf4j
@Controller
public class SimpleMessageConsumer implements MessageConsumer {
  /*
   * We only need @SqsListener annotation that automatically makes our consume method get the messages
   * from the queue.
   * We also add @NotificationMessage annotation to have the correct deserialization
   * and extract our Event from the SNS message
   * because the SNS message is wrapped inside a Message object
   */
  @Override
  @SqsListener(value = ORDER_QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void consume(@NotificationMessage Event event) {
    if (event != null) {
      log.info("Received order event for consumer 1: " + event);
    }
  }
}
