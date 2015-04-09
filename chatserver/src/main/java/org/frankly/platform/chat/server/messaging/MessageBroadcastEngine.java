package org.frankly.platform.chat.server.messaging;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.websocket.Session;

/**
 * 
 * @author Ahmed
 *
 */
public class MessageBroadcastEngine {
	private final Executor broadcastExecutor;
	private final int corePoolSize = 4;
	private final int maximumPoolSize = 1000;

	
	/**
	 * Default Constructor
	 */
	public MessageBroadcastEngine() {
		broadcastExecutor = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize, 100, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
	}
	

	/**
	 * Broadcast a message asynchronously. We use thread pool to run
	 * the threads responsible for publishing messages. 
	 * @param currentSession
	 * @param broadcastMessage
	 * @param room
	 */
	public void broadcast(Session currentSession, String broadcastMessage, String room) {
		broadcastExecutor.execute(new BroadcastTask(currentSession, broadcastMessage, room));
	}

}
	

	
