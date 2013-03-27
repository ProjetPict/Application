package localDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import dbwConnection.DbwConnection;
import dbwConnection.ResultSet;

/**
 * Cette classe permet la connexion du serveur avec la base de données ainsi que les requêtes basiques
 * @author Matthieu
 *
 */
public class DbConnection extends Thread {

	private String db_link_url = "http://drawvs.free.fr/core/dbw.php";
	private String db_hostname = "sql.free.fr";
	private String db_username = "drawvs";
	private String db_password = "shazbotvgs";
	private String db_basename = "drawvs";
	private DbwConnection db_connection;
	
	public DbConnection() {
		
	}
	
	public boolean connectDatabase() {
		try {
			db_connection = new DbwConnection(db_link_url, db_username, db_password, db_hostname, db_basename);
			return true;
		} catch(SQLException e) {
			return false;
		}
	}
	
	public boolean tryConnection(String username, String password) {
		return true;
	}
	
	public ResultSet executeQuery(String query) {
		ResultSet rs;
		try {
			rs = db_connection.executeQuery(query);
		} catch (SQLException e) {
			rs = new ResultSet(null);
		}
		return rs;
	}
	
}
