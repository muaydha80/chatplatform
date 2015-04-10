package org.frankly.platform.chat.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

public class ChatServer {
  private static Logger logger = Logger.getLogger(ChatServer.class.getName());

  /**
   * The entry point method to start a server at specific port. It uses Jetty container
   *
   * @param port
   */
  public void startChatServer(int port) {
    // Set it to IPv4
    System.setProperty("java.net.preferIPv4Stack", "true");
    Server server = new Server(port);

    try {
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      // Initialize the container
      ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);
      // Add the endpoint
      container.addEndpoint(ChatServerEndPoint.class);

      server.start();
      server.join();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Sever Failed due to: " + e.getMessage(), e);
    }
  }
}
