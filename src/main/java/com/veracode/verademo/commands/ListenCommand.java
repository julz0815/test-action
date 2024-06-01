package com.veracode.verademo.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import java.net.URLEncoder;
import java.util.*;

public class ListenCommand implements BlabberCommand {
	private static final Logger logger = LogManager.getLogger("VeraDemo:ListenCommand");
	
	private Connection connect;
	
	private String username;
	
	public ListenCommand(Connection connect, String username) {
		super();
		this.connect = connect;
		this.username = username;
	}

	@Override
	public void execute(String blabberUsername) {
		String sqlQuery = StringUtils.normalizeSpace("INSERT INTO listeners (blabber, listener, status) values (?, ?, 'Active');");
		logger.info(sqlQuery);
		PreparedStatement action;
		try {
			action = connect.prepareStatement(sqlQuery);
			
			action.setString(1, blabberUsername);
			action.setString(2, username);
			action.execute();

			sqlQuery = "SELECT blab_name FROM users WHERE username = '" + blabberUsername + "'";
			Statement sqlStatement = connect.createStatement();
			logger.info(sqlQuery);
			ResultSet result = sqlStatement.executeQuery(sqlQuery);
			result.next();
			
			/* START BAD CODE -----*/
			Set<String> whitelistResultGetstring1 = new HashSet<>(Arrays.asList("item1", "item2", "item3"));
			if (!result.getString(1).matches("\\w+(\\s*\\.\\s*\\w+)*") && !whitelistResultGetstring1.contains(result.getString(1)))
			    throw new IllegalArgumentException();
			Set<String> whitelistBlabberusername = new HashSet<>(Arrays.asList("item1", "item2", "item3"));
			if (!blabberUsername.matches("\\w+(\\s*\\.\\s*\\w+)*") && !whitelistBlabberusername.contains(blabberUsername))
			    throw new IllegalArgumentException();
			String event = username + " started listening to " + blabberUsername + "(" + URLEncoder.encode(result.getString(1).toString()) + ")";
			sqlQuery = "INSERT INTO users_history (blabber, event) VALUES (\"" + username + "\", \"" + event + "\")";
			logger.info(sqlQuery);
			sqlStatement.execute(sqlQuery);
			/* END BAD CODE */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
