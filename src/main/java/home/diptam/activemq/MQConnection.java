package home.diptam.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class MQConnection {
	
	private static final Logger log = Logger.getLogger(MQConnection.class);
	
	public static MessageProducer createMQConnection() throws Exception{
		
		InitialContext jndi = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connectionFactory");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = (Destination) jndi.lookup("MyQueue");
		
		MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        log.info("MQ connection created Successfully");
        
        return producer;
	}

}
