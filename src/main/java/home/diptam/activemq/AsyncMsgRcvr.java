package home.diptam.activemq;

import java.beans.ExceptionListener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class AsyncMsgRcvr implements MessageListener,ExceptionListener{

	private static QueueConnection queueConnection;
	
	private static Logger log = Logger.getLogger(AsyncMsgRcvr.class);
	
	public static void main(String[] args) {
		
		//Purpose of this class to listen to MQ queue, and run as soon as msg arrives at Queue
		try {
			init();
			AsyncMsgRcvr.get();
		} catch (Exception e) {
			log.info("Error Occured : "+e);
		}
		
	}
	
	public static void startListenerSvc() {
		try {
			init();
			AsyncMsgRcvr.get();
		} catch (Exception e) {
			log.info("Error Occured : "+e);
		}
	}

	private static void init() throws Exception{
		InitialContext jndi = new InitialContext();
		QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndi.lookup("connectionFactory");
		Queue queue = (Queue) jndi.lookup("MyQueue");
		queueConnection = queueConnectionFactory.createQueueConnection();
		QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		QueueReceiver queueReceiver = queueSession.createReceiver(queue);
		log.info("MQ connection created Successfully");
		
		//Set up listener 
		AsyncMsgRcvr amr = new AsyncMsgRcvr();
		queueReceiver.setMessageListener(amr);
	}

	private static void get() throws Exception {
		queueConnection.start();
		log.info("MQ listener started-->>");
		Thread.sleep(30000);
		queueConnection.close();
		log.info("MQ listener Stopped-->>");
	}

	public void exceptionThrown(Exception e) {
		
		log.error("Error Occured from JMS Exception handler : "+e);
	}

	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			log.info("Msg received: " + msg.getText());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
