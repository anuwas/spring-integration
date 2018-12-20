package com.anu.springintegration;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.router.ExpressionEvaluatingRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.core.DestinationResolutionException;
import org.springframework.messaging.core.DestinationResolver;



@SpringBootApplication
public class SpringIntegrationApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SpringIntegrationApplication.class, args);
		ConfigurableApplicationContext ctx = SpringApplication.run(SpringIntegrationApplication.class, args);
		Sender sender = ctx.getBean(Sender.class);
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNext()) {
			String msgsend = scanner.next();
			Account ac = new Account();
			ac.setAccountNo("123456");
			ac.setAccountType(msgsend);
			sender.send(ac);
		}
		scanner.close();
	}
	
	@Bean
	public MessageChannel messagechannel() {
		return new DirectChannel();
	}
	
	@Router(inputChannel="messagechannel")
	@Bean
	public ExpressionEvaluatingRouter router() {
		ExpressionEvaluatingRouter evr = new ExpressionEvaluatingRouter("payload.accountType") ;
		evr.setChannelResolver(name->name.equalsIgnoreCase("saving")?savingChannel():defaultOutChannel());
		/*
		evr.setChannelResolver(new DestinationResolver<MessageChannel>() {

			@Override
			public MessageChannel resolveDestination(String name) throws DestinationResolutionException {
				return null;
			}
			
		});
		*/
		return evr;
		
	}
	
	@Bean
	public MessageChannel savingChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel defaultOutChannel() {
		return new DirectChannel();
	}
	//any customization needs to do inside the service activator
	@Bean
	@ServiceActivator(inputChannel="savingChannel")
	public MessageHandler savingChannelHandler() {
		return  (Message<?> message)->System.out.print("Saving Channel "+message);
	}
	
	@Bean
	@ServiceActivator(inputChannel="defaultOutChannel")
	public MessageHandler defaultChannelHandler() {
		return  message->System.out.print("Default Channel "+message);
	}

}

