package vub.tiamat;

/**
 * The templates class.
 * 
 * @author Ayrton Vercruysse
 * 
 */
public class Templates {
	String names;
	AST.Node function;

	/**
	 * Initializes a template.
	 * 
	 * @param name
	 *            The name of the template.
	 * @param function
	 *            The function for what the template is made.
	 */
	public Templates(String name, AST.Node function) {
		names = name;
		this.function = function;

	}

	/**
	 * Gives the name of the Template.
	 * 
	 * @return Returns the name of the template.
	 */
	public String getName() {
		return names;
	}

	/**
	 * Gives the function of this template.
	 * 
	 * @return Returns the function of this template.
	 */
	public AST.Node getFunction() {
		return function;
	}
}
