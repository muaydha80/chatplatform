package org.frankly.platform.chat.server;


import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.frankly.platform.chat.server.messaging.ChatRoomSessionsManager;
import org.frankly.platform.chat.server.messaging.MessageBroadcastEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *Server Endpoint
 * @author muaydha 
 *
 */
@ServerEndpoint(value = "/{room}")
public class ChatServerEndPoint {

  private static Logger logger = Logger.getLogger(ChatServerEndPoint.class.getName());
  private MessageBroadcastEngine broadcastManager = new MessageBroadcastEngine();
  private ChatRoomSessionsManager chatRoomSessionsManager = ChatRoomSessionsManager.getInstance();

  /**
   * This method is called to open websocket session
   *
   * @param session
   * @param room
   */
  @OnOpen
  public void onOpen(Session session, @PathParam("room") String room) {
    session.getUserProperties().put("room", room);
    String message = getClientAddress(session) + " joined the party!";
    logger.log(Level.INFO, message);
    publishMessage(session, message, room);
    chatRoomSessionsManager.addMemberToRoom(session, room);
  }

  /**
   * This method is called when server receives a message
   *
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
   * This method is called when server receives a binary message
   *
   * @param message
   * @param session
   */
  @OnMessage
  public void onMessage(InputStream message, Session session) {
    logger.log(Level.INFO, "Received Binary message. Closing client: "+ getClientAddress(session) );
    try {
      session.close();
    } catch (IOException e) {
      logger.log(Level.INFO, "Failed to close session " + e.getMessage(), e);
    }

  }
  
  /**
   * This method is called when websocket is closed
   *
   * @param session
   * @param room
   */
  @OnClose
  public void onClose(Session session, @PathParam("room") String room) {
    chatRoomSessionsManager.removeMemberFromRoom(session, room);
    String message = getClientAddress(session) + " passed out...";
    logger.log(Level.INFO, message);
    publishMessage(session, message, room);

  }

  /**
   * Get client address
   *
   * @param session
   * @return client Address
   */
  public String getClientAddress(Session session) {
    return session.getUserProperties().get("javax.websocket.endpoint.remoteAddress").toString();
  }

  /**
   * Publish Message to other websockets
   *
   * @param session
   * @param message
   * @param room
   */
  private void publishMessage(Session session, String message, String room) {
    broadcastManager.broadcast(session, message, room);
  }

}
