package slaAuctions.agents;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.Template;

public abstract class Agent implements Runnable {
	protected Template template;
	protected ApplicationContext context;
	protected String id;

	public Agent(ApplicationContext context, Template template, String id) {
		this.context = context;
		this.template = template;
		this.id = id;
	}
}
