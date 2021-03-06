package AST;

import java.io.Serializable;

/**
 * The Block AST node.
 * 
 * @author Ayrton Vercruysse
 * 
 */
public class Block extends Node implements Serializable{
	int numberOfParameters;

	/**
	 * Initializes a Block node.
	 * 
	 * @param parent
	 *            The parent of the node.
	 * @param numberOfParameters
	 *            The number of parameters the block contains.
	 */

	public Block(Node parent, int numberOfParameters) {
		super(parent);
		for (int i = 0; i < numberOfParameters; i++) {
			children.add(new Placeholder(this, "Par" + Integer.toString(i),
					false));
		}
		Node content = new Placeholder(this, "Content", true);
		this.numberOfParameters = numberOfParameters;
		children.add(content);
	}

	/**
	 * Gets the content of the block.
	 * 
	 * @return Returns the content of the block.
	 */
	public Node getContent() {
		return children.get(numberOfParameters);
	}

	/**
	 * Changes the content of a block.
	 * 
	 * @param newContent
	 *            The newContent to which content has to be set.
	 */
	public void setContent(Node newContent) {
		children.set(numberOfParameters, newContent);
	}

	/**
	 * Gives the number of paramets of the block.
	 * 
	 * @return Return the number of parameters of the block.
	 */
	public int getNumberOfParameters() {
		return numberOfParameters;
	}

	@Override
	public String toString() {
		String string;
		string = "|";
		for (int i = 0; i < children.size(); i++) {
			string = string + children.get(i).toString() + ",";
		}
		string = string + "|";
		return string;
	}

}
