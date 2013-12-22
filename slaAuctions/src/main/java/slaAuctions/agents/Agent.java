package slaAuctions.agents;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.Template;

public abstract class Agent implements Runnable {
	protected Template template;
	protected ApplicationContext context;

	public Agent(ApplicationContext context, Template template) {
		this.context = context;
		this.template = template;
	}
}
