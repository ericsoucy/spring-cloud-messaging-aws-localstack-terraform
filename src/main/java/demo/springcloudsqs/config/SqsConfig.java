package demo.springcloudsqs.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.support.NotificationMessageArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.util.List;

import static demo.springcloudsqs.config.AWSConfigConstants.*;


@Configuration
public class SqsConfig {

    @Bean
    public AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(ENDPOINT, CA_CENTRAL_1);
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync(final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    /*
    * Setup QueueMessageHandlerFactory so it can convert incoming messages from
    * SQS as String to the actual object we want,
    * in this case, Event, using a MessageConverter
    * */
    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(MessageConverter messageConverter) {
        var factory = new QueueMessageHandlerFactory();
        factory.setArgumentResolvers(List.of(new NotificationMessageArgumentResolver(messageConverter)));
        return factory;
    }

    /*
    * The converter will take care of converting the String message to the actual Event object.
    * */
    @Bean
    protected MessageConverter messageConverter(ObjectMapper objectMapper) {
        var converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setSerializedPayloadClass(String.class);
        converter.setStrictContentTypeMatch(false);
        return converter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
