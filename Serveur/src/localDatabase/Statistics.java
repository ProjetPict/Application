package localDatabase;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import dbwConnection.ResultSet;

/**
 * Cette classe permet la gestion des statistiques à sauvegarder
 *
 */
public class Statistics {

	private ArrayList<StatsWordsRow> statsWords;
	private DbConnection dbLink;

	public Statistics(DbConnection db) {
		statsWords = new ArrayList<StatsWordsRow>();
		dbLink = db;
	}


	/**
	 * Permet d'ajouter une nouvelle entrée dans la base de données (nbs = 0 ou 2 paramètres)
	 * @param word Mot a ajouter
	 * @param nbs Paramètres facultatifs (0 ou 2 paramètres)
 	 */
	public void addNewStatsToWord(String word, boolean choose, int ... nbs) {
		try {
			int index = wordAlreadyInStats(word);
			if(index==-1) {
				statsWords.add(new StatsWordsRow(word));
				index = statsWords.size()-1;
			}
			
			statsWords.get(index).addNewProposalServer();
			
			if(choose){
				if(nbs.length!=2)
					throw new Exception();
				statsWords.get(index).addNewProposalAccept(nbs[0],nbs[1]);
			}
		} catch(Exception e) {
			System.out.println("Erreur lors de la modification de statistiques (nombre de paramètres incorrect).");
		}
	}

	/**
	 * Permet de savoir si le mot a déjà été enregistré une fois pour mettre a jour ses stats plutot que l'insérer en doublon
	 * @param word Mot a tester
	 * @return La position du mot dans l'arraylist
	 */
	public int wordAlreadyInStats(String word) {
		boolean found = false;
		int index = 0;
		int pos = -1;
		while(index<statsWords.size() && !found) {
			if(statsWords.get(index).getWord().equals(word)) {
				pos = index;
				found = true;
			} else {
				index++;
			}
		}
		return pos;
	}

	/**
	 * Permet de sauvegarder les statistiques présentent en locale dans la base de données MySQL distante
	 * @return Retourne true si les statistiques sont bien sauvegardées, faux sinon ou si il n'y a rien à enregistrer.
	 */
	public boolean saveStatistics() {
		try {
			String queries = "";
			for(int i=0; i<statsWords.size();i++) {
				ResultSet rs = dbLink.executeQuery("SELECT id FROM words WHERE mot='"+statsWords.get(i).getWord()+"';");
				while(rs.next()) {
					queries+=statsWords.get(i).returnSQLRequest(rs.getString(1));
				}
			}
			if(queries.equals(""))
				return false;
			return dbLink.executeInsertQuery(queries);
		} catch (SQLException e) {
			return false;
		}
	}

	public void resetStatisticsAfterSave() {
		statsWords.clear();
	}

}
