package socketData;


/**
 * Cette classe sert � envoyer des mots � faire choisir (c�t� serveur) 
 * ou � envoyer le mot choisi (c�t� client)
 *
 */
public class WordCommand extends Command {

	private static final long serialVersionUID = 1L;

	public String word1;
	public String word2;
	public String word3;
	public int difficulty;

	/**
	 * Constructeur de WordCommand
	 * @param word1 Le premier mot � faire deviner
	 * @param word2 Le deuxi�me mot � faire deviner
	 * @param word3 Le troisi�me mot � faire deviner
	 * @param difficulty La difficult� de ces mots
	 */
	public WordCommand(String word1, String word2, String word3, int difficulty) {
		this.command = "";
		this.word1 = word1;
		this.word2 = word2;
		this.word3 = word3;
		this.difficulty = difficulty;
	}
}
