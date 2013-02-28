import java.sql.SQLException;
import java.util.ArrayList;

import dbwConnection.DbwConnection;


public class DbConnection extends Thread {

	private String db_link_url = "http://localhost/DrawVs/core/dbw.php";
	private String db_hostname = "127.0.0.1";
	private String db_username = "root";
	private String db_password = "";
	private String db_basename = "drawvs";
	
	public DbConnection() {
		try {
			DbwConnection conn = new DbwConnection(db_link_url, db_username, db_password, db_hostname, db_basename);
		} catch(SQLException e) {
			System.out.println("> Impossible de se connecter à la base de données ("+e+")");
		}
	}
	
	public boolean tryConnection(String username, String password) {
		return true;
	}
	
	public boolean insertStatistique(ArrayList<String> param) {
		return true;
	} 
	
}
