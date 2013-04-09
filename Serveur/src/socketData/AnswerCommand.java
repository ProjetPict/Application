package socketData;

/**
 * Cette classe h�rite de Command et est utilis� pour envoyer une r�ponse au serveur
 *
 */
public class AnswerCommand extends Command {


	private static final long serialVersionUID = 1L;
	public long nbPixels;

	/**
	 * Constructeur de AnswerCommand
	 * @param answer La r�ponse � envoyer
	 * @param nbPixels Le nombre de pixels du dessin lors de l'envoi de la r�ponse
	 */
	public AnswerCommand(String answer, long nbPixels) {
		command = answer;
		this.nbPixels = nbPixels;
	}
}
