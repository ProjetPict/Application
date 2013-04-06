package localDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import dbwConnection.ResultSet;

/**
 * Cette classe permet de créer une base de données propre au serveur basée sur celle du site
 * @author Matthieu
 *
 */
public class ServerDatabase {
	
	private DbConnection dbLink;
	private HashMap<String, String> servDbUsersLogs;
	private ArrayList<ArrayList<String>> servDbWordsLogs;
	private Statistiques servDbStatsLogs;
	
	public ServerDatabase(DbConnection db) {
		servDbUsersLogs = new HashMap<String, String>();
		servDbWordsLogs = new ArrayList<ArrayList<String>>();
		servDbStatsLogs = new Statistiques();
		dbLink = db;
	}
	
	public boolean loadDatabase() {
		try {
			ResultSet rs = dbLink.executeQuery("SELECT login,password FROM users;");
			while(rs.next()) {
				servDbUsersLogs.put(rs.getString(1), rs.getString(2));
			}
			for(int i=1;i<4;i++) {
				ResultSet rs2 = dbLink.executeQuery("SELECT mot FROM words WHERE difficulte='"+i+"';");
				servDbWordsLogs.add(new ArrayList<String>());
				while(rs2.next()) {
					
					servDbWordsLogs.get(i-1).add(rs2.getString(1));
				}
			}
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
	
	public boolean testAuthentification(String login, String password){
		
		boolean res = false;
		
		if(servDbUsersLogs.containsKey(login))
		{
			try {
				String pass = servDbUsersLogs.get(login);

				byte[] hash = MessageDigest.getInstance("MD5").digest(password.getBytes());
				StringBuilder hashString = new StringBuilder();
				for (int i = 0; i < hash.length; i++)
				{
				    String hex = Integer.toHexString(hash[i]);
				    if (hex.length() == 1)
				    {
				        hashString.append('0');
				        hashString.append(hex.charAt(hex.length() - 1));
				    }
				    else
				        hashString.append(hex.substring(hex.length() - 2));
				}
				
				if(hashString.toString().equals(pass))
					res = true;
				
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//TODO renvoyer la vraie valeur
		return true;
	}
	
}
