package localDatabase;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Cette classe permet la gestion des statistiques à sauvegarder
 * @author Matthieu
 *
 */
public class Statistiques {

	private ArrayList<StatsWordsRow> statsWords;
	
	public Statistiques() {
		statsWords = new ArrayList<StatsWordsRow>();
	}
	
	public void addWordRow(String word) {
		statsWords.add(new StatsWordsRow(word));
	}
	
	public void addNewStatsToWord(String word, boolean choose, int ... nbs) {
		try {
			boolean found = false;
			int index = 0;
			while(index<statsWords.size() && !found) {
				if(statsWords.get(index).getWord().equals(word)) {
					statsWords.get(index).addNewProposalServer();
					if(choose) {
						statsWords.get(index).addNewProposalAccept();
						if(nbs.length!=2)
							throw new Exception();
						statsWords.get(index).addFoundPercent(nbs[0],nbs[1]);
					}
					found = true;
				} else {
					index++;
				}
			}
		} catch(Exception e) {
			System.out.println("Erreur lors de la modification de statistiques (nombre de paramètres incorrect).");
		}
	}
	
	public void resetStatistiquesAfterSave() {
	}
	
}
