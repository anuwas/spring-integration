package com.anu.springintegration;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.router.RecipientListRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;



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
	public RecipientListRouter recipientListRouter() {
		RecipientListRouter rlr = new RecipientListRouter();
		/*
		rlr.addRecipient("savingChannel", new MessageSelector() {

			@Override
			public boolean accept(Message<?> message) {
				return false;
			}
		});
		*/
		//using lambda
		rlr.addRecipient("savingChannel", (message)->message.getPayload().toString().equalsIgnoreCase("saving"));
		rlr.setDefaultOutputChannel(deafultOutChannel());
		return rlr;
		
	}
	
	@Bean
	public MessageChannel savingChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel deafultOutChannel() {
		return new DirectChannel();
	}
	
	//any customization needs to do inside the service activator
	

	@Bean
	@ServiceActivator(inputChannel="savingChannel")
	public MessageHandler savingChannelHandler() {
		return  (Message<?> message)->System.out.print("Saving Channel "+message);
	}
	
	@Bean
	@ServiceActivator(inputChannel="deafultOutChannel")
	public MessageHandler deafultOutChannelHandler() {
		return  (Message<?> message)->System.out.print("default channel"+message);
	}
	

}

