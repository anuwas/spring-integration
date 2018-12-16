package com.anu.springintegration;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.HeaderValueMessageProcessor;
import org.springframework.integration.transformer.support.StaticHeaderValueMessageProcessor;
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
	//this is input channel
	@Bean
	public MessageChannel messagechannel() {
		return new DirectChannel();
	}
	
	//this is output channel
	@Bean
	public MessageChannel messageoutchannel() {
		return new DirectChannel();
	}
		
	// Header enricher using to modify the header of the message
	@Transformer(inputChannel="messagechannel",outputChannel="messageoutchannel")
	@Bean
	public HeaderEnricher headerenricher() {
		Map<String,HeaderValueMessageProcessor<?>> mp = new HashMap<>();
		mp.put("key1",new StaticHeaderValueMessageProcessor<String>("firstHeader"));
		mp.put("key2",new StaticHeaderValueMessageProcessor<String>("SecondHeader"));
		
		
		HeaderEnricher headerEnricher = new HeaderEnricher(mp);
		return headerEnricher;
	}
	
	//any customization needs to do inside the service activator
	@Bean
	@ServiceActivator(inputChannel="messageoutchannel")
	public MessageHandler messageHandler() {
		return  (Message<?> message)->System.out.print(message);
	}

}

