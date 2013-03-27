package localDatabase;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import dbwConnection.ResultSet;

import server.Statistiques;


/**
 * Cette classe permet de créer une base de données propre au serveur basée sur celle du site
 * @author Matthieu
 *
 */
public class ServerDatabase {
	
	private DbConnection dbLink;
	private HashMap<String, String> servDbUsersLogs;
	private ArrayList<Statistiques> servDbStatsLogs;
	
	public ServerDatabase(DbConnection db) {
		servDbUsersLogs = new HashMap<String, String>();
		servDbStatsLogs = new ArrayList<Statistiques>();
		dbLink = db;
	}
	
	public boolean loadDatabase() {
		ResultSet rs = dbLink.executeQuery("SELECT login,password FROM users;");
		try {
			while(rs.next()) {
				servDbUsersLogs.put(rs.getString(1), rs.getString(2));
				Set mapSet = (Set) servDbUsersLogs.entrySet();
                //Create iterator on Set 
                Iterator mapIterator = mapSet.iterator();
                while (mapIterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                        String keyValue = (String) mapEntry.getKey();
                        String value = (String) mapEntry.getValue();
                        System.out.println("Login : " + keyValue + " / Mot de passe : " + value);
                }
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public void saveDatabase() {
		
	}
	
	public void saveStatistiques() {
		
	}
	
}
