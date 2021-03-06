package AST;

import java.io.Serializable;

import AST.Node;

/**
 * The Begin AST node.
 * 
 * @author Ayrton Vercruysse
 * 
 */
public class Begin extends Node implements Serializable{
	/**
	 * Initializes a begin node
	 * 
	 * @param parent
	 *            The parent node.
	 */
	public Begin(Node parent) {
		super(parent);
		Node child = new Placeholder(this, "content", true);
		children.add((Node) child);
	}

	/**
	 * Gets the content of the begin.
	 * 
	 * @return The content of the begin node.
	 */
	public Node getContent() {
		return children.get(0);
	}

	/**
	 * Sets the new content of the begin.
	 * 
	 * @param newContent
	 *            Sets the content of the begin to newContent.
	 */
	public void setContent(Node newContent) {
		children.set(0, newContent);
	}

	@Override
	public String toString() {
		String string = "{";
		for (int i = 0; i < children.size(); i++) {
			string = string + children.get(i).toString() + ";";
		}
		string = string + "}";
		return string;

	}
}
