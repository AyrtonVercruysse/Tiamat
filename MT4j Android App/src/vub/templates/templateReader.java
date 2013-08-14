package vub.templates;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.res.AssetManager;

import vub.ast.Function;
import vub.tiamat.StartTiamat;

/*
 * StartTiamat.functions.add(IfThenElse());
public Templates IfThenElse() {
	String names[] = { "if:", "then:", "else:" };
	String contents[] = { "predicate", "consequent", "alternative" };
	vub.ast.Node node = new vub.ast.Function(null, names, contents);
	return new Templates("If:Then:Else", node);
}
*/

public class templateReader{

	public static void templateReader(AssetManager assetManager) {
		try {
			
			InputStream is = assetManager.open("templates.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			dbFactory.setValidating(true);
			dbFactory.setIgnoringElementContentWhitespace(true);
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			Node file = doc.getFirstChild(); //Templates
			removeWhitespaceNodes((Element) file);
			NodeList templates = file.getChildNodes(); //Template
			for (int i = 0; i < templates.getLength(); i++){
				Element template = (Element) templates.item(i); //Template
				String name =  template.getAttribute("name");
				template = (Element) template.getFirstChild(); //Type
				String type = template.getNodeName();
				Class argumentsTypes = Class.forName(type); // e.g. Function
				
				Constructor argumentConstructor = argumentsTypes.getConstructor(Element.class);
				vub.ast.Node func = (vub.ast.Node)argumentConstructor.newInstance(template); // Node
				System.out.println("Temp" + type);
				if(type.equals("vub.ast.Function")){
					StartTiamat.functions.add(new Templates(name, func));
				}else if(type.equals("vub.ast.Operation")){
					StartTiamat.operations.add(new Templates(name, func));
				} else{
					StartTiamat.definitions.add(new Templates(name, func));
				}
				
			}
		} catch (Exception ex) {
			System.out.println("TemplatesError");
			ex.printStackTrace();
		}
	}

	public static void removeWhitespaceNodes(Element e) {
		NodeList children = e.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
		Node child = children.item(i);
		if (child instanceof Text && ((Text) child).getData().trim().length() == 0) {
		e.removeChild(child);
		}
		else if (child instanceof Element) {
		removeWhitespaceNodes((Element) child);
		}
		}
		}
}
