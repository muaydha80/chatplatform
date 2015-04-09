package org.frankly.platform.chat.server.messaging;

import java.io.IOException;
import java.util.List;

import javax.websocket.Session;

/**
 * 
 * @author Ahmed
 *
 */
class BroadcastTask implements Runnable{
	private ChatRoomSessionsManager chatRoomSessionsManager = 
			ChatRoomSessionsManager.getInstance();
	private Session currentSession;
	private String broadcastMessage;
	private String room;
	
	/**
	 * The task responsbile to send a broadcast message
	 * @param currentSession
	 * @param broadcastMessage
	 * @param room
	 */
	public BroadcastTask(Session currentSession, String broadcastMessage, String room){
		this.currentSession = currentSession;
		this.broadcastMessage = broadcastMessage;
		this.room = room;
	}
	

	/**
	 * The run method for task(thread) to send messages in async way
	 */
	public void run(){
		List<Session> memberSessionList = chatRoomSessionsManager.getMemberSessionList(this.room);
		for(Session session: memberSessionList){
			if(session != this.currentSession){
				try {
					session.getBasicRemote().sendText(this.broadcastMessage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}