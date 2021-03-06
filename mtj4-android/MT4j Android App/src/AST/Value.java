package AST;

import java.io.Serializable;

/**
 * The Value AST node.
 * 
 * @author Ayrton Vercruysse
 * 
 */
public class Value extends Node implements Serializable{
	String name;
	String comments;

	/**
	 * Initializes the node.
	 * 
	 * @param parent
	 *            The parent of the node.
	 * @param name
	 *            The name of the value.
	 */
	public Value(Node parent, String name) {
		super(parent);
		this.name = name;
		type = "Value";
	}

	/**
	 * Gives the name of value.
	 * 
	 * @return Returns the name of the value.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Changes the name of the value
	 * 
	 * @param newName
	 *            The new name of the value.
	 */
	public void setName(String newName) {
		name = newName;
	}
	
	
	@Override
	public String toString() {
		return name;
	}
}
