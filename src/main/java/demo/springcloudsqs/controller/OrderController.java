package demo.springcloudsqs.controller;

import demo.springcloudsqs.model.Event;
import demo.springcloudsqs.model.EventData;
import demo.springcloudsqs.model.EventType;
import demo.springcloudsqs.producer.SimpleMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class OrderController {

  private final AtomicInteger atomicInteger = new AtomicInteger();
  @Autowired SimpleMessageProducer simpleMessageProducer;

  @GetMapping(value = "/create-order", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> createOrder() {
    Event event = createOrderEvent();
    simpleMessageProducer.publish(event);
    return ResponseEntity.ok().body("Published message on SNS");
  }

  private Event createOrderEvent() {
    return Event.builder()
        .eventId(UUID.randomUUID().toString())
        .occurredAt(Instant.now().toString())
        .version(String.valueOf(atomicInteger.getAndIncrement()))
        .data(
            EventData.builder()
                .eventType(EventType.ORDER_CREATED)
                .orderId(UUID.randomUUID().toString())
                .owner("SampleProducer")
                .build())
        .build();
  }
}
