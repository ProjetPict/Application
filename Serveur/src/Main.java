import server.Server;



public class Main {
	
	private static Server server;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		server = new Server();
		server.start();
	}
}
