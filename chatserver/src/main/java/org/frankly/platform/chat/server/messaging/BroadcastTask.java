package org.frankly.platform.chat.server.messaging;

import org.frankly.platform.chat.server.ChatServer;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.Session;

/**
 *
 * @author Ahmed
 *
 */
class BroadcastTask implements Runnable {
  private static Logger logger = Logger.getLogger(ChatServer.class.getName());

  private ChatRoomSessionsManager chatRoomSessionsManager = ChatRoomSessionsManager.getInstance();
  private Session currentSession;
  private String broadcastMessage;
  private String room;

  /**
   * The task responsbile to send a broadcast message
   *
   * @param currentSession
   * @param broadcastMessage
   * @param room
   */
  public BroadcastTask(Session currentSession, String broadcastMessage, String room) {
    this.currentSession = currentSession;
    this.broadcastMessage = broadcastMessage;
    this.room = room;
  }


  /**
   * The run method for task(thread) to send messages in async way
   */
  @Override
  public void run() {
    List<Session> memberSessionList = chatRoomSessionsManager.getMemberSessionList(this.room);

    if(memberSessionList == null)
      return;
    
    Iterator<Session> iterator = memberSessionList.iterator();
    while (iterator.hasNext()) {
      Session session = iterator.next();

      // Check if session is already closed then remove it
      if (!session.isOpen()) {
        continue;
      }

      if (session != this.currentSession) {
        try {
          //synchronized around session because API only allows one send 
          synchronized(session){
            session.getBasicRemote().sendText(this.broadcastMessage);
          }
        } catch (Exception e) {
          logger.log(Level.INFO, "Failed to broadcast message " + e.getMessage(), e);
        }
      }
    }
  }
}
