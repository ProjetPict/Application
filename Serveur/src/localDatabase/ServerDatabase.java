package localDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import dbwConnection.ResultSet;

/**
 * Cette classe permet de créer une base de données propre au serveur basée sur celle du site
 *
 */
public class ServerDatabase {

	private DbConnection dbLink;
	private HashMap<String, String> servDbUsersLogs;
	private ArrayList<ArrayList<String>> servDbWordsLogs;
	private Statistics servDbStatsLogs;

	/**
	 * Constructeur de la base de données locale au serveur
	 * @param db Lien vers la connexion à la base de données
	 */
	public ServerDatabase(DbConnection db) {
		servDbUsersLogs = new HashMap<String, String>();
		servDbWordsLogs = new ArrayList<ArrayList<String>>();
		dbLink = db;
		servDbStatsLogs = new Statistics(dbLink);
	}

	/**
	 * Recharge (= actualise) la base de données locale
	 */
	public void reloadDatabase() {
		servDbUsersLogs.clear();
		servDbWordsLogs.clear();
		loadDatabase();
	}

	/**
	 * Permet de charger la base de données SQL en local
	 * @return Retourne true si la base est chargée correctement, false sinon
	 */
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

	/**
	 * Effectue une sauvegarde des statistiques de la base de données
	 * @return Retourne true si la base de données est bien sauvegardée, false sinon
	 */
	public boolean saveDatabase() {
		if(servDbStatsLogs.saveStatistics()) {
			servDbStatsLogs.resetStatisticsAfterSave();
			return true;
		}
		return false; 
	}

	/**
	 * Récupération de la liste des utilisateurs/mot de passe
	 * @return Le tableau à double entrée utilisateur/mot de passe
	 */
	public HashMap<String, String> getUsersList() {
		return servDbUsersLogs;
	}


	/**
	 * Retourne la liste des mots d'une difficulté donnée
	 * @param difficulty Difficulté choisie
	 * @return La liste (ArrayList) des mots de la difficulté voulue
	 */
	public ArrayList<String> getWordsList(int difficulty) {
		if(difficulty<1 || difficulty>3)
			return null;
		return servDbWordsLogs.get(difficulty-1);
	}


	/**
	 * Récupération des statistiques enregistrées
	 * @return Le tableau des statistiques
	 */
	public Statistics getStatistics() {
		return servDbStatsLogs;
	}

	/**
	 * Permet de tester le nom d'utilisateur/mot de passe pour connecter l'utilisateur
	 * @param login Nom d'utilisateur a tester
	 * @param password Mot de passe a tester
	 * @return Retourne true si l'utilisateur a rentré les bons identifiants, faux sinon.
	 */
	public boolean testAuthentification(String login, String password){
		boolean res = false;
		if(servDbUsersLogs.containsKey(login)) {
			try {
				String pass = servDbUsersLogs.get(login);
				byte[] hash = MessageDigest.getInstance("MD5").digest(password.getBytes());
				StringBuilder hashString = new StringBuilder();
				for (int i = 0; i < hash.length; i++) {
					String hex = Integer.toHexString(hash[i]);
					if (hex.length() == 1) {
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
