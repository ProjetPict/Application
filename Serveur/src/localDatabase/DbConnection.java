package localDatabase;

import java.sql.SQLException;
import dbwConnection.DbwConnection;
import dbwConnection.ResultSet;

/**
 * Cette classe permet la connexion du serveur avec la base de donn�es ainsi que les requ�tes basiques
 *
 */
public class DbConnection extends Thread {

	private String db_link_url = "http://drawvs.free.fr/core/dbw.php";
	private String db_hostname = "sql.free.fr";
	private String db_username = "drawvs";
	private String db_password = "";
	private String db_basename = "drawvs";
	private DbwConnection db_connection;
	
	public DbConnection() {
	}
	
	/* 
	 * Tente la connexion � la base de donn�es
	 * @return True si la connexion est �tablie, false sinon
	 */
	public boolean connectDatabase() {
		try {
			db_connection = new DbwConnection(db_link_url, db_username, db_password, db_hostname, db_basename);
			return true;
		} catch(SQLException e) {
			return false;
		}
	}
	
	/**
	 * Permet l'execution de la requete (SELECT uniquement) pass�e en param�tres
	 * @param query
	 * @return Le tableau de r�sultat
	 */
	public ResultSet executeQuery(String query) {
		ResultSet rs;
		try {
			rs = db_connection.executeQuery(query);
		} catch (SQLException e) {
			rs = new ResultSet(null);
		}
		return rs;
	}

	/**
	 * Permet l'execution de la requete (INSERT et UPDATE uniquement) pass�e en param�tres
	 * @param query
	 * @return Retourne true si l'execution s'est bien d�roul�e, false sinon
	 */
	public boolean executeInsertQuery(String query) {
		boolean res = true;
		try {
			String[] queries = query.split(";");
			
			for(int i = 0; i < queries.length; i++)
				db_connection.executeQuery(queries[i]+";");
		} catch (SQLException e) {
			res = false;
			e.printStackTrace();
		}
		return res;
	}
	
}
