import java.util.ArrayList;


public class DbConnection extends Thread {

	private String db_hostname = "127.0.0.1";
	private String db_username = "root";
	private String db_password = "";
	private String db_basename = "drawvs.free.fr";
	private String db_portnumb = "20";
	
	public DbConnection() {
		
	}
	
	public boolean tryConnection(String username, String password) {
		return true;
	}
	
	public boolean insertStatistique(ArrayList<String> param) {
		return true;
	}
	
}
