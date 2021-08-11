package edu.emmerson.camel.quarkus.rmq.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * docker run -d --hostname rabbitmqserver --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
 * 
 * @author emmersonmiranda
 *
 */
@ApplicationScoped
@Named(ProducerConnectionFactoryService.BEAN_NAME)
public class ProducerConnectionFactoryService extends com.rabbitmq.client.ConnectionFactory {

    public static final String BEAN_NAME = "producerConnectionFactoryService";

    @Inject
    private ConfigReader configReader = new ConfigReader();;

    public ProducerConnectionFactoryService() {
        super();
        super.setHost(configReader.getRabbitHost());
        super.setPort(configReader.getRabbitPort());
        super.setUsername(configReader.getRabbitUsername());
        super.setPassword(configReader.getRabbitPassword());
        super.setVirtualHost(configReader.getRabbitVirtualHost());
    }

}
