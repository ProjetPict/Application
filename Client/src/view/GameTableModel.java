package view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import socketData.GameInfo;
import socketData.GameList;

public class GameTableModel extends AbstractTableModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<GameInfo> gl;
	private String[] columns;
	
	public GameTableModel(GameList gl)
	{
		this.gl = gl.games;
		columns = new String[]{"Nom", "Joueurs", "Mot de passe", "Status"};
	}

	@Override
	public int getRowCount() {
		
		return gl.size();
	}

	@Override
	public int getColumnCount() {
		
		return columns.length;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch (columnIndex) {
		case 0:
			return gl.get(rowIndex).name;
		case 1:
			return gl.get(rowIndex).nbPlayers +"/"+gl.get(rowIndex).maxPlayers;
		case 2:
			if(gl.get(rowIndex).password)
				return "Oui";
			else
				return "Non";
		case 3:
			if(gl.get(rowIndex).started)
				return "En cours";
			else
				return "En attente";
		default:
			throw new IllegalArgumentException();
		}
	}

}
