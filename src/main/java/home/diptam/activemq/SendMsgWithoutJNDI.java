package home.diptam.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class SendMsgWithoutJNDI {
	
	private static final Logger log = Logger.getLogger(SendMsgUsingJNDI.class);
	
    public static void main( String[] args ){
    	/**
		 * JNDI Details
		 * java.naming.factory.initial : org.apache.activemq.jndi.ActiveMQInitialContextFactory
		 * java.naming.provider.url : tcp://hostname:61616
		 * topic.MyTopic : example.MyTopic
		 * Admin Url : http://127.0.0.1:8161/admin/queues.jsp (admin : admin)
		**/
    	
        SendMsgWithoutJNDI.sendMsg();
        SendMsgWithoutJNDI.getMsg();
    }

	private static void sendMsg() {
		
		//This method creates MQ connection and sends a sample msg to Queue
		//This example is not using any JNDI
		
		try {
			//create connection to MQ
			ActiveMQConnectionFactory mqConnectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			Connection con = mqConnectionFactory.createConnection();
			con.start();
			
			//create a session (This is created for a active session which will be used throughout out your program)
			Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			
			//create a local Queue
			//If Queue with same name already exists it will make connection to that queue
			Destination destination = session.createQueue("TestQueue");
			
			
			// Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
           
            //Create a wrapper of Text msg from a String
            String msg = "1st msg to Test Queue";
            TextMessage tm =  session.createTextMessage(msg);
            
            //send msg
			producer.send(tm);
			log.info("Msg Sent Successfully");
			
			//Close session and Connection, Should be in finally block
			session.close();
			con.close();
			
		} catch (Exception e) {
		   log.info("Error occured : "+e);
		}
		
	}
	
	private static void getMsg() {
	
		
		
	}
}
