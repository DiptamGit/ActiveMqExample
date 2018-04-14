package home.diptam.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class SendMsgUsingJNDI {
	
	private static final Logger log = Logger.getLogger(SendMsgUsingJNDI.class);

	public static void main(String[] args) {
		
		SendMsgUsingJNDI.send();
		//SendMsgUsingJNDI.sendMultiMsg();
		//AsyncMsgRcvr.startListenerSvc();
	}

	private static void sendMultiMsg() {
		try {
			InitialContext jndi = new InitialContext();
			ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connectionFactory");
			Connection connection = connectionFactory.createConnection();
			connection.start();
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination) jndi.lookup("MyQueue");
			
			MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            for (int i = 1; i < 7; i++) {
            	log.info("Message sent to Queue successfully-"+i);
            	String msg = "Msg num "+i+"";
            	TextMessage tm =  session.createTextMessage(msg);
            	producer.send(tm);
			}
            
            connection.close();
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void send() {
		
		try {
			InitialContext jndi = new InitialContext();
			ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connectionFactory");
			Connection connection = connectionFactory.createConnection();
			connection.start();
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination) jndi.lookup("MyQueue");
			
			MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            String msg = "1st msg to Test Queue";
            TextMessage tm =  session.createTextMessage(msg);
			producer.send(tm);
			log.info("Message sent successfully");
			
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
