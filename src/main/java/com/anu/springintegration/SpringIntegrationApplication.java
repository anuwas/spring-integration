package com.anu.springintegration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;



@SpringBootApplication
public class SpringIntegrationApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SpringIntegrationApplication.class, args);
		ConfigurableApplicationContext ctx = SpringApplication.run(SpringIntegrationApplication.class, args);
		Sender sender = ctx.getBean(Sender.class);
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNext()) {
			String msgsend = scanner.next();
			sender.send(msgsend);
		}
		scanner.close();
	}
	
	@Bean
	public MessageChannel messagechannel() {
		return new DirectChannel();
	}
	
	@Router(inputChannel="messagechannel")
	@Bean
	public AbstractMessageRouter msgRouter()
	{
		return new AbstractMessageRouter() {

			@Override
			protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
				Collection<MessageChannel> collectionMgsChannel = new ArrayList<>();
				if(message.getPayload().toString().equalsIgnoreCase("event")) {
					collectionMgsChannel.add(eventchannel());
				}else {
					collectionMgsChannel.add(defaultchannel());
				}
				return collectionMgsChannel;
			}
			
		};
		
	}
	
	@Bean
	public MessageChannel eventchannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel defaultchannel() {
		return new DirectChannel();
	}
	
	//any customization needs to do inside the service activator
	@Bean
	@ServiceActivator(inputChannel="eventchannel")
	public MessageHandler messageHandler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				System.out.println("Event Channel "+message);
				
			}
			
		};
	}
	
	@Bean
	@ServiceActivator(inputChannel="defaultchannel")
	public MessageHandler defaultHandler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				System.out.println("Deafult Channel "+message);
				
			}
			
		};
	}

}

