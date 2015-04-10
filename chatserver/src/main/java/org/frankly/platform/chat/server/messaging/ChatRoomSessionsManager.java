package org.frankly.platform.chat.server.messaging;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

/**
 * Chat Rooms Session Manager class. It uses a map of room to list of websocket sessions
 *
 * @author Ahmed
 *
 */
public class ChatRoomSessionsManager {
  private final static ChatRoomSessionsManager chatRoomSessionsManager =
      new ChatRoomSessionsManager();
  private Map<String, Map<String, Session>> roomsMemberSessionListMap =
      new ConcurrentHashMap<String, Map<String, Session>>();

  
  private ChatRoomSessionsManager() {
    
  }

  /**
   * Get an instance
   *
   * @return chatRoomSessionsManager
   */
  public static ChatRoomSessionsManager getInstance() {
    return chatRoomSessionsManager;
  }

  /**
   * Add a member to a room
   *
   * @param session
   * @param room
   */
  public void addMemberToRoom(Session session, String room) {
    Map<String, Session> memberSessionsMap = roomsMemberSessionListMap.get(room);
    if (memberSessionsMap == null) {
    	memberSessionsMap = new ConcurrentHashMap<String, Session>();
        roomsMemberSessionListMap.put(room, memberSessionsMap);
    }

    memberSessionsMap.put(session.getId(), session);
  }

  /**
   * Remove a member from the list of members in a room
   *
   * @param session
   * @param room
   */
  public void removeMemberFromRoom(Session session, String room) {
    Map<String, Session> memberSessionsMap = roomsMemberSessionListMap.get(room);
    memberSessionsMap.remove(session.getId());
 
  }

  /**
   * Get a room's members list
   *
   * @param room
   * @return list of sessions in a room
   */
  public Collection<Session> getMemberSessionCollection(String room) {
    if(!roomsMemberSessionListMap.containsKey(room))
    	return null;
    return roomsMemberSessionListMap.get(room).values();
  }

}
