package com.lmzz.listener;

public class Event {

	private Thief thief;

	public Event() {
	}

	public Event(Thief thief) {
		this.thief = thief;
	}

	public Thief getThief() {
		return thief;
	}

	public void setThief(Thief thief) {
		this.thief = thief;
	}
}