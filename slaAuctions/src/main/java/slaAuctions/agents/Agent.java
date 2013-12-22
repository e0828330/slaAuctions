package slaAuctions.agents;

import slaAuctions.entities.Template;

public abstract class Agent implements Runnable {
	protected Template template;

	public Agent(Template template) {
		this.template = template;
	}
}
