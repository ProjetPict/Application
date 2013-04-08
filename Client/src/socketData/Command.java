package socketData;

import java.io.Serializable;

/**
 * Cette classe sert � envoyer des String au serveur, elle est principalement
 * utilis�e comme super-classe pour les autres commandes. Elle est s�rialisable pour le
 * transfert au serveur, donc le code doit �tre identique sur le client et le serveur.
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
	 * @param command La commande � envoyer
	 */
	public Command(String command) {
		this.command = command;
	}
}
