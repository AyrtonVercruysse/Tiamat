package vub.ast;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vub.templates.Templates;
import vub.tiamat.StartTiamat;

public class Function extends Node implements Serializable {
	String[] names;
	
	public Function(Element template) {
		super(null);
		try {
			String name = template.getElementsByTagName("name").item(0).getTextContent();
			NodeList args = template.getElementsByTagName("args").item(0).getChildNodes();
			System.out.println("Templ: " + name);
			int nrOfArgs = args.getLength();
			String names[] = new String[nrOfArgs];
			vub.ast.Node contents[] = new vub.ast.Node[nrOfArgs];
			for (int j = 0; j < nrOfArgs; j++) {
				Element argument = (Element) args.item(j);
				names[j] = argument.getAttribute("name");
				String argumentName = argument.getNodeName();
				Class argumentsTypes;
				argumentsTypes = Class.forName(argumentName);
				Constructor argumentConstructor = argumentsTypes.getConstructor(Element.class);
				vub.ast.Node aerg = (vub.ast.Node)argumentConstructor.newInstance(argument);
				contents[j] = aerg;

			}
			vub.ast.Node func = new Function(null, names, contents);
			StartTiamat.functions.add(new Templates(name, func));
		} catch (Exception ex) {
			System.out.println("TemplatesError");
			ex.printStackTrace();
		}
	}

	public Function(Node parent, String[] names, Node[] contents) {
		super(parent);
		int numberOfChildren = names.length;
		this.names = names;
		Node child;
		for (int i = 0; i < numberOfChildren; i++) {
			child = contents[i];
			addChild(child);
		}
	}

	public Function(Node parent, String[] names, String[] contents) {
		super(parent);
		int numberOfChildren = names.length;
		this.names = names;
		Node child;
		for (int i = 0; i < numberOfChildren; i++) {
			child = new Placeholder(this, contents[i], false);
			addChild(child);
		}
	}

	public String[] getNames() {
		return names;
	}

	@Override
	public String toString() {
		String string = "";
		for (int i = 0; i < names.length; i++) {
			string = string + names[i] + " " + getChild(i).toString() + " ";
		}
		return string;
	}
}
