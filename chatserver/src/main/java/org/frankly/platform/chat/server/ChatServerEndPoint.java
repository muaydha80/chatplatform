package org.frankly.platform.chat.server;


import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.frankly.platform.chat.server.messaging.ChatRoomSessionsManager;
import org.frankly.platform.chat.server.messaging.MessageBroadcastEngine;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author muaydha
 *
 */
@ServerEndpoint(value = "/{room}")
public class ChatServerEndPoint {

	private static Logger logger = Logger.getLogger(ChatServerEndPoint.class.getName());
	private MessageBroadcastEngine broadcastManager = new MessageBroadcastEngine();
	private ChatRoomSessionsManager chatRoomSessionsManager = ChatRoomSessionsManager
			.getInstance();

	/**
	 * This method is called to open websocket session
	 * @param session
	 * @param room
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("room") String room) {
		session.getUserProperties().put("room", room);
		String message = "Member joined: "
				+ session.getUserProperties()
						.get("javax.websocket.endpoint.remoteAddress")
						.toString();
		logger.log(Level.INFO, message);
		publishMessage(session, message, room);
		chatRoomSessionsManager.addMemberToRoom(session, room);
	}

	/**
	 * This method is called when server receives a message
	 * @param message
	 * @param session
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		String room = (String) session.getUserProperties().get("room");
		logger.log(Level.INFO, "Received message: " + message);

		publishMessage(session, message, room);

	}

	/**
	 * This method is called when websocket is closed
	 * @param session
	 * @param room
	 */
	@OnClose
	public void onClose(Session session, @PathParam("room") String room) {
		chatRoomSessionsManager.removeMemberFromRoom(session, room);
		String message = "Member left: "
				+ session.getUserProperties()
						.get("javax.websocket.endpoint.remoteAddress")
						.toString();
		logger.log(Level.INFO, message);
		publishMessage(session, message, room);

	}

	/**
	 * Publish Message to other websockets
	 * @param session
	 * @param message
	 * @param room
	 */
	private void publishMessage(Session session, String message, String room) {
		broadcastManager.broadcast(session, message, room);
	}

}
