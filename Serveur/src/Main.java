import server.Server;



public class Main {
	
	private static Server server;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			server = new Server();
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
