package clustering.model;

public class KeyWordAttribute {
	private String name;
	public final static String TYPE = "NUMERIC";
	
	/*
	 * @param name    String, which is both name of this attribute and the key word(s) it represents
	 */
	public KeyWordAttribute(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}