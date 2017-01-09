package com.andersonfonseka.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ProgressMonitor extends Thread implements Runnable {

	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	
	private List<ProgressNotifier> list = new ArrayList<ProgressNotifier>();
	
	private boolean started;
	
	
	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isStarted() {
		return started;
	}

	public void addProgressNotifier(ProgressNotifier notifier){
		this.list.add(notifier);
	}
	
	public void addMessage(Message message){
		queue.add(message);
	}
	
	public void run() {
	
			while(true){
				
				Message message = null;
				
				try {
					message = queue.take();
					
					for (ProgressNotifier progressNotifier : list) {
						progressNotifier.updateDisplay(message);
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}

}
