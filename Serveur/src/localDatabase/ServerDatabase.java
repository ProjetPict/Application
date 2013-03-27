package localDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
	private ArrayList<ArrayList<String>> servDbWordsLogs;
	private ArrayList<Statistiques> servDbStatsLogs;
	
	public ServerDatabase(DbConnection db) {
		servDbUsersLogs = new HashMap<String, String>();
		servDbWordsLogs = new ArrayList<ArrayList<String>>();
		servDbStatsLogs = new ArrayList<Statistiques>();
		dbLink = db;
	}
	
	public boolean loadDatabase() {
		try {
			ResultSet rs = dbLink.executeQuery("SELECT login,password FROM users;");
			while(rs.next()) {
				servDbUsersLogs.put(rs.getString(1), rs.getString(2));
				/*Set mapSet = (Set) servDbUsersLogs.entrySet();
                Iterator mapIterator = mapSet.iterator();
                while (mapIterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                        String keyValue = (String) mapEntry.getKey();
                        String value = (String) mapEntry.getValue();
                        System.out.println("Login : " + keyValue + " / Mot de passe : " + value);
                }*/
			}
			for(int i=1;i<4;i++) {
				ResultSet rs2 = dbLink.executeQuery("SELECT mot FROM words WHERE difficulte='"+i+"';");
				servDbWordsLogs.add(new ArrayList<String>());
				while(rs2.next()) {
					servDbWordsLogs.get(i-1).add(rs2.getString(1));
				}
			}
			/*for(int j=0; j<servDbWordsLogs.size();j++) {
				System.out.println("Difficulté : "+(j+1));
				for(int k=0; k<servDbWordsLogs.get(j).size();k++)
					System.out.println("Mot : "+servDbWordsLogs.get(j).get(k));
			}*/
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public void saveDatabase() {
		
	}
	
	public HashMap<String, String> getUsersList() {
		return servDbUsersLogs;
	}
	
	public ArrayList<String> getWordsList(int difficulty) {
		if(difficulty<1 || difficulty>3)
			return null;
		return servDbWordsLogs.get(difficulty-1);
	}
	
}
