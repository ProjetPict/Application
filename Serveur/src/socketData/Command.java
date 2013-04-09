package socketData;

import java.io.Serializable;

/**
 * Cette classe sert à envoyer des String au serveur, elle est principalement
 * utilisée comme super-classe pour les autres commandes. Elle est sérialisable pour le
 * transfert au serveur, donc le code doit être identique sur le client et le serveur.
 *
 */
public class Command implements Serializable {

	private static final long serialVersionUID = 1L;
	public String command;

	protected Command() {
		command = "";
	}

	/**
	 * Constructeur de Command
	 * @param command La commande à envoyer
	 */
	public Command(String command) {
		this.command = command;
	}
}
