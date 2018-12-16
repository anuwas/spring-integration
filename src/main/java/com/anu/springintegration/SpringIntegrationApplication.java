package com.anu.springintegration;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.integration.transformer.MethodInvokingTransformer;
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
	
	// creating new output channel
	@Bean
	public MessageChannel messageOutputchannel() {
		return new DirectChannel();
	}
	
	// this is using to transform the message
	@Transformer(inputChannel="messagechannel")
	@Bean
	public MessageHandler transformer() {
		MethodInvokingTransformer invokingTransformer = new MethodInvokingTransformer(methodconverter(), "convert");
		MessageTransformingHandler  transformHandler = new MessageTransformingHandler(invokingTransformer);
		//setting message to an output channel
		//No output channel is here , so creating a new one
		transformHandler.setOutputChannel(messageOutputchannel());
		return transformHandler;
	}
	
	// creating bean for the converter class
	@Bean
	public MethodConverter methodconverter() {
		return new MethodConverter();
	}
	
	//any customization needs to do inside the service activator
	@Bean
	@ServiceActivator(inputChannel="messageOutputchannel")
	public MessageHandler messageHandler() {
		return  (Message<?> message)->System.out.print(message);
	}

}

