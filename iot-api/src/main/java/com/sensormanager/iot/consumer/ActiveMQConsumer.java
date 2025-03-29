package com.sensormanager.iot.consumer;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQConsumer {

	private static final String BROKER_URL = "tcp://186.64.120.248:61617";
	private static final String QUEUE_NAME = "p1-g5";
	
	public static void main(String[] args) {
		Connection connection = null;
		Session session = null;
		try {
			// Crear la fábrica de conexión
			ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
			// Crear la conexión
			connection = factory.createConnection();
			connection.start();
			// Crear la sesión
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);		
			// Crear el destino (cola)
			Destination destination = session.createQueue(QUEUE_NAME);
			// Crear el consumidor
			MessageConsumer consumer = session.createConsumer(destination);
		
			System.out.println("Esperando mensajes en la cola: " + QUEUE_NAME);
		
			// Recibir mensajes de forma continua
			while (true) {
				Message message = consumer.receive();
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;		
					System.out.println("Mensaje recibido: " + textMessage.getText());
				} else {
					System.out.println("Mensaje no reconocido: " + message);
				}
			}	
		} catch (Exception e) {
			System.err.println("Error al recibir mensajes: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (session != null) session.close();
				if (connection != null) connection.close();
			} catch (Exception e) {
				System.err.println("Error al cerrar recursos: " + e.getMessage());		
			}
		}
	}
}
