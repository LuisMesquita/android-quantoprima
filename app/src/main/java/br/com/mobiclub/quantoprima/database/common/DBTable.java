package br.com.mobiclub.quantoprima.database.common;

public class DBTable {

	private String name;
	private DBColumn[] columns;
	private String[] columnsName;
	private int columnsCount;
	
	private String createCommandString;
	private String dropCommandString;

	public DBTable(String name,  DBColumn[] columns) {
		this.name = name;
		this.columns = columns;
		
		if (columns != null)  {
			this.columnsCount = columns.length; 
		} else {
			this.columnsCount = 0;
		}
		
		this.columnsName = this.buildColumnsNames();
		 
		this.createCommandString = this.buildCreateCommandString();
		this.dropCommandString = this.buildDropCommandString();
	}
	
	public String getName() {
		return name;
	}
	
/*	public int getColumnsCount() {
		return columnsCount;
	}
	
	public DBColumn[] getColumns() {
		return columns;
	}
	
	public String getColumnName(int index) {
		String name = null;
		
		try {
			name = columns[index].getName();
		} catch(ArrayIndexOutOfBoundsException e) {}

		return name;
	}*/
	
	public String getCreateCommandString() {
		return this.createCommandString;
	}
	
	public String getDropCommandString() {
		return this.dropCommandString;
	}
	
	public String[] getColumnsName() {
		return columnsName;
	}
	
	private String buildCreateCommandString() {
		String creation = "CREATE TABLE " + this.name + "("; 

		if (columnsCount > 0) {
			DBColumn columnTemp = columns[0];
			
			creation = creation + columnTemp.getName() + " " + columnTemp.getModifiers();
		
			for (int i = 1; i < columnsCount; i++) {
				columnTemp = columns[i];
				creation = creation + "," +  columnTemp.getName() + " " + columnTemp.getModifiers();
			}
			
			creation = creation + ");"; 
		}
			
		return creation;	
	}
	
	private String buildDropCommandString() {
		return "DROP TABLE IF EXISTS " + this.name;
	}
	
	private String[] buildColumnsNames() {
		String[] returnValue = new String[columnsCount];
		
		DBColumn columnTemp = null;
		for (int i = 0; i < columnsCount; i++) {
			columnTemp = columns[i];
			returnValue[i] = columnTemp.getName();
		}
		
		return returnValue;
	}
}
