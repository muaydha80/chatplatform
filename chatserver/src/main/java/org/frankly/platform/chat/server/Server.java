package org.frankly.platform.chat.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class Server {
	private final static int DEFAULT_PORT = 9001;
	private static Logger logger = Logger.getLogger(Server.class.getName());
	

	/**
	 * 
	 * 
	 */
	public static void main(String[] args) {
		try {
			int port = parseCommandLine(args);
			//Create chat server instance and start it
			ChatServer chatServer = new ChatServer();
			chatServer.startChatServer(port);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Parse Command Options and return port if passed
	 * @param cmdOptions
	 * @return port
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static int parseCommandLine(String[] cmdOptions) throws Exception {
		int port = DEFAULT_PORT;
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption(OptionBuilder.withArgName("<portValue>").hasArgs(2)
				.withValueSeparator().withDescription("Set server port value")
				.create("port"));

		try {
			CommandLine cmd = parser.parse(options, cmdOptions);
			String portStr = cmd.getOptionValue("port");
			if(portStr != null)
				port = Integer.parseInt(portStr);
		} catch (Exception e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "chatserver", options );
			throw e;
		}
		return port;
	}
}
