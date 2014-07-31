package com.redhat.gpe.training.openshift.dev.hornetq;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DemoRestClient {

	private final static Logger LOGGER = Logger.getLogger(DemoRestClient.class
			.toString());

	public static void main(String[] args) {

		try {
			// read URL from props file
			String url = getDemoUrl("demo.properties");

			LOGGER.info("Connecting to: " + url);

			// send messages in a loop
			for (int i = 1; i <= 10; i++) {
				String body = i + ". Hello World from REST!!";
				sendMessage(url, body);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendMessage(String url, String body) throws Exception {

		// create client
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target(url);

		// send HTTP POST
		LOGGER.info("Sending message: " + body);
		Response postResponse = target.request(MediaType.TEXT_PLAIN_TYPE).post(
				Entity.entity(body, MediaType.TEXT_PLAIN));

		// check the response code
		int status = postResponse.getStatus();
		if (status != 201) {
			throw new RuntimeException("Failed with HTTP error code : "
					+ status);
		}
	}

	private static String getDemoUrl(String fileName) throws IOException {
		Properties props = loadProperties(fileName);

		String url = props.getProperty("rest.url");

		return url;
	}

	private static Properties loadProperties(String fileName)
			throws IOException {
		
		// read properties file from classpath
		Properties properties = new Properties();
		properties.load(DemoRestClient.class.getClassLoader()
				.getResourceAsStream(fileName));

		return properties;
	}
}
