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
			return gl.get(rowIndex).password;
		case 3:
			return gl.get(rowIndex).started;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
			case 1:
				return String.class;
			case 2:
			case 3:
				return Boolean.class;
	
			default:
				return Object.class;
		}
	}


}
