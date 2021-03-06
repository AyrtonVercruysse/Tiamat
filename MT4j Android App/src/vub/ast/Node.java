package vub.ast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vub.rendering.Comments;

public class Node implements Serializable {
	Node parent;
	Comments comments;
	public String type;
	Vector<Node> children = new Vector<Node>();

	public Node(Node parent) {
		this.parent = parent;

	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean isRoot() {
		if (parent == null) {
			return true;
		} else {
			return false;
		}
	}

	public Comments getComments() {
		return comments;
	};

	public void setComments(Comments comments) {
		this.comments = comments;
	}

	public Vector<Node> getChildren() {
		return children;
	}

	public Node getChild(int i) {
		return children.get(i);
	}

	public void addChild(Node node) {
		children.add(node);
	}
	
	public void setChild(int i, Node newChild){
		children.set(i, newChild);
	}

	public int numberOfChildren() {
		return children.size();
	}

	public void setChild(Node oldChild, Node newChild) {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i) == oldChild) {
				children.set(i, newChild);
				if (oldChild instanceof Placeholder
						&& ((Placeholder) oldChild).everlasting) {
					children.add(new Placeholder(this, "Content", true));
				}
			}
		}
	}

	public String toString() {
		return null;
	}
	
	public void toXML(Element rootElement, Document doc) {
		}
	
	public void Function(Element template){
		return;
	}

	public Object clone() {
		parent = null;
		ObjectOutputStream outStream = null;
		ObjectInputStream inStream = null;
		Object ret = false;
		System.out.println("Copy Called");
		try {
			System.out.println("in de Try1");
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			outStream = new ObjectOutputStream(byteOut);
			// serialize and write obj2DeepCopy to
			// byteOut
			System.out.println("This: " + this);
			System.out.println("in de Try2");
			outStream.writeObject(this);
			System.out.println("in de Try2");
			// always flush your stream
			outStream.flush();
			System.out.println("in de Try3");
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			System.out.println("in de Try4");
			inStream = new ObjectInputStream(byteIn);
			ret = inStream.readObject();
			System.out.println("in de Try5");
			return ret;
		} catch (Exception e) {
			System.out.println("Exception 1");
			e.printStackTrace();
		} finally {
			// always close your streams in finally clauses
			try {
				outStream.close();
				inStream.close();
			} catch (IOException e) {
				System.out.println("Exception 2");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Exception 3");
				return (Node) ret;
			}
		}
		return null;
	}

}


