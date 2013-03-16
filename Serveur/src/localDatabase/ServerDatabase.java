package localDatabase;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import server.Statistiques;


/**
 * Cette classe permet de créer une base de données propre au serveur basée sur celle du site
 * @author Matthieu
 *
 */
public class ServerDatabase {
	
	private static Map<String, String> servDbUsersLogs;
	private ArrayList<Statistiques> servDbStatsLogs;
	
	public ServerDatabase() {
		servDbUsersLogs = new Hashtable<String, String>();
		servDbStatsLogs = new ArrayList<Statistiques>();
	}
	
	public void loadDatabase() {
		
	}
	
	public void saveDatabase() {
		
	}
	
	public void saveStatistiques() {
		
	}
	
}
