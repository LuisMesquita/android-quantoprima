package br.com.mobiclub.quantoprima.database.common;

public class DBColumn {

	private String name;
	private String modifiers;
	
	public DBColumn (String name, String modifiers) {
		this.name = name;
		this.modifiers = modifiers;
	}
	
	public String getName() {
		return name;
	}
	
	public String getModifiers() {
		return modifiers;
	}
}
