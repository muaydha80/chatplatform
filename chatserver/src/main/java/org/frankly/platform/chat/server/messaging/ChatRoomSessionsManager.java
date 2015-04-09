package org.frankly.platform.chat.server.messaging;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;

/**
 * Chat Rooms Session Manager class. It uses a map of room to list of websocket sessions
 * @author Ahmed
 *
 */
public class ChatRoomSessionsManager {
	private final static ChatRoomSessionsManager chatRoomSessionsManager = 
			new ChatRoomSessionsManager();
	private Map<String, List<Session>> roomsMemberSessionListMap = 
			new ConcurrentHashMap<String, List<Session>>();
	
	private ChatRoomSessionsManager(){}
	
	/**
	 * Get an instance
	 * @return chatRoomSessionsManager
	 */
	public static ChatRoomSessionsManager getInstance(){
		return chatRoomSessionsManager;
	}
	
	/**
	 * Add a member to a room
	 * @param session
	 * @param room
	 */
	public void addMemberToRoom(Session session, String room){
		List<Session> memberSessionList = roomsMemberSessionListMap.get(room);
		if(memberSessionList == null) {
			memberSessionList = new LinkedList<Session>();
			roomsMemberSessionListMap.put(room, memberSessionList);
		}
		memberSessionList.add(session);
	}
	
	/**
	 * Remove a member from the list of members in a room
	 * @param session
	 * @param room
	 */
	public void removeMemberFromRoom(Session session, String room){
		List<Session> memberSessionList = roomsMemberSessionListMap.get(room);
		memberSessionList.remove(session);
	}
	
	/**
	 * Get a room's members list
	 * @param room
	 * @return list of sessions in a room
	 */
	public List<Session> getMemberSessionList(String room){
		return roomsMemberSessionListMap.get(room);
	}
	
}
