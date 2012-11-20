package vub.ast;

import java.io.Serializable;

import vub.ast.Node;

public class Comment extends Node implements Serializable {
	public Comment(Node parent, Node item) {
		super(parent);
		addChild(item);
	}

	public Node getContent() {
		return getChild(0);
	}

	public void setContent(Node newContent) {
		setChild(0, newContent);
	}

	@Override
	public String toString() {
		String string = " ";
		return string;

	}
}
