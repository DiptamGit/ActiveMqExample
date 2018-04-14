package home.diptam.activemq;

import java.beans.ExceptionListener;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class GetMsgUsingJNDI implements ExceptionListener{

	private static Logger log = Logger.getLogger(GetMsgUsingJNDI.class);
	
	public static void main(String[] args) {
		
		try {
			GetMsgUsingJNDI.get();
		} catch (Exception e) {
			log.info("Error occured : "+e);
		}
	}

	private static void get() throws Exception{
		InitialContext jndi = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connectionFactory");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = (Destination) jndi.lookup("MyQueue");
		MessageConsumer consumer = session.createConsumer(destination);
		
		
		while(true) {
			Message message = consumer.receive(2000);
			
			if (message instanceof TextMessage) {
	            TextMessage textMessage = (TextMessage) message;
	            String text = textMessage.getText();
	            log.info("Message Received from Queue : " + text);
	        } else {
	        	log.info("No More Message left in Queue");
	        	connection.close();
	        	break;
	        }
		}
		
	}

	public void exceptionThrown(Exception e) {
		log.info(e);
		
	}

}
