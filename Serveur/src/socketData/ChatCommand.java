package socketData;

/**
 * Une commande contenant un message de Chat
 *
 */

public class ChatCommand extends Command {

	private static final long serialVersionUID = 1L;
	public String author;

	/**
	 * Constructeur de ChatCommand
	 * @param msg Le message à envoyer
	 * @param author L'auteur du message
	 */
	public ChatCommand(String msg, String author) {
		this.command = msg;
		this.author = author;
	}
}
