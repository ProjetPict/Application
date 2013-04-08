package view;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import model.GameObserver;
import model.Model;

/**
 * Classe de démarrage : instancie le modèle et la vue
 *
 */
public class Main {

	private static Model model;
	private static View view;
	private static String host;
	private static String fileHostAddress;
	public static String player;
	public static ResourceBundle texts;
	public static final int SCREEN_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	public static double gameWidth;
	public static double gameHeight;
	public static double ratioX;
	public static double ratioY;
	public static double offsetX;
	public static double offsetY;
	public static Properties settingsProp;
	
	public static void main(String[] argc) {
		boolean launchOk = false;
		boolean unique = isUnique();
		
		if(unique) {
			gameWidth = (int)(Main.SCREEN_WIDTH * 0.8);
			gameHeight = (int)(Main.SCREEN_HEIGHT * 0.8);
			ratioX = gameWidth/1024.0;
			ratioY = gameHeight/768.0;
			offsetX = (gameWidth - 1024)/2;
			texts = ResourceBundle.getBundle("TextBundle", Locale.getDefault());
			//Get the host from the property file
			settingsProp = new Properties();
			try {
				settingsProp.load(new FileInputStream("files/settings.conf"));
				fileHostAddress = settingsProp.getProperty("fileHostAddress");
				readFromUrl(fileHostAddress);
				model = new Model(host);
				view = new View();
				view.setPanel("Login", false);
				launchOk = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!unique || !launchOk) {
			JOptionPane.showMessageDialog(null, texts.getString("instance_error"), texts.getString("launch_error"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public static Model getModel() {
		return model;
	}
	
	public static View getView() {
		return view;
	}
	
	public static GameObserver getGameObserver() {
		return model.getGameObserver();
	}
	
	public static boolean isUnique() {
	    boolean unique;
	    try {
	        unique = new FileOutputStream("files/lock.ini").getChannel().tryLock() != null;
	    } catch(IOException ie) {
	        unique = false;
	    }
	    
	    //TODO remplacer par unique
	    return true;
	}
	
	public static void readFromUrl(String s) {
		try {
			InputStream ips = new URL(s).openStream();
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				host = ligne;
			}
			br.close(); 
		} catch(Exception e) {}
	}
}
