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
import org.springframework.integration.router.PayloadTypeRouter;
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
			ac.setAccountType("saving");
			if(msgsend.equalsIgnoreCase("saving")) {
				sender.send(ac);
			}else {
				sender.send("hello");
			}
			
		}
		scanner.close();
	}
	
	@Bean
	public MessageChannel messagechannel() {
		return new DirectChannel();
	}
	
	@Router(inputChannel="messagechannel")
	@Bean
	public PayloadTypeRouter router() {
		PayloadTypeRouter payloadrout = new PayloadTypeRouter();
		payloadrout.setChannelMapping(Account.class.getName(), "savingChannel");
		payloadrout.setDefaultOutputChannel(defaultOutChannel());
		return payloadrout;
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

