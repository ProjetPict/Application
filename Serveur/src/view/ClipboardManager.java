package view;

import java.awt.datatransfer.*;
import java.awt.*;
import java.io.*;

/**
 * Gère les fonctions copier/coller
 * @author Matthieu
 *
 */
public class ClipboardManager implements ClipboardOwner {
	
	public ClipboardManager() {
		
	}

	public void setClipboardContents(String s) {
		StringSelection ss = new StringSelection(s);
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(ss, this);
	}

	public String getClipboardContents() {
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
			if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String text = (String)t.getTransferData(DataFlavor.stringFlavor);
				return text;
			}
	    }
	    catch (UnsupportedFlavorException e) {
	    	System.out.println(e);
	    }
	    catch (IOException e) {
	    	System.out.println(e);
	    }
	    return null;
	}

	@Override
	public void lostOwnership(Clipboard clipboard,Transferable contents) {}
}
