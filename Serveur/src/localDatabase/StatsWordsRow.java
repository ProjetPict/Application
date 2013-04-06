package localDatabase;

public class StatsWordsRow {

	private String word;
	private int proposalServer;
	private int proposalAccepted;
	private int proposalPlayed;
	private int proposalFound;
	private double proposalAcceptedPercent;
	private double proposalFoundPercent;
	
	public StatsWordsRow(String w) {
		word = w;
		proposalServer = 0;
		proposalAccepted = 0;
		proposalPlayed = 0;
		proposalFound = 0;
		proposalAcceptedPercent = 0.0;
		proposalFoundPercent = 0.0;
	}
	
	public void addNewProposalServer() {
		proposalServer++;
		proposalAcceptedPercent = proposalAccepted / proposalServer * 100;
	}
	
	public void addNewProposalAccept() {
		proposalAccepted++;
		proposalAcceptedPercent = proposalAccepted / proposalServer * 100;
	}
	
	public void addFoundPercent(int nbPlayers, int nbFound) {
		proposalPlayed += nbPlayers;
		proposalFound += nbFound;
		proposalFoundPercent = proposalFound / proposalPlayed * 100;
	}
	
	public String getWord() {
		return word;
	}
	
	public void resetStatsWord() {
		proposalServer = 0;
		proposalAccepted = 0;
		proposalPlayed = 0;
		proposalFound = 0;
		proposalAcceptedPercent = 0.0;
		proposalFoundPercent = 0.0;
	}
	
}
