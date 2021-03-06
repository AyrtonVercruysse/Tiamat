package AST;

public class Definition extends Node{

	public Definition(Node parent) {
		super(parent);
		Node name = new Placeholder(this, "Function Name", false);
		Node content = new Placeholder(this, "content", false);
		children.add(name);
		children.add(content);
	}
	
	public Node getName(){
		return children.get(0);
	}
	public void setName(Node newName){
		children.set(0, newName);
	}
	public Node getContent(){
		return children.get(1);
	}
	public void setContent(Node newContent){
		children.set(1, newContent);
	}
	@Override
	public String toString(){
		String string = "def " + children.get(0).toString() + " := " + children.get(1).toString();
		return string;
	}

}
