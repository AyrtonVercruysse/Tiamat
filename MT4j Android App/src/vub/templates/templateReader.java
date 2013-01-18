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
			System.out.println("Templates:" + is);
		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			dbFactory.setValidating(true);
			dbFactory.setIgnoringElementContentWhitespace(true);
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			
			
			Node node = doc.getFirstChild();
			removeWhitespaceNodes((Element) node);
			node = node.getFirstChild().getFirstChild();
			String name = node.getNodeValue();
			String name2 = node.getNodeName();
			Boolean bool = node.hasChildNodes();
			Class node3 = Class.forName(name2);
			Constructor intcon = node3.getConstructors()[0];
			String names[] = { "if:", "then:", "else:" };
			String contents[] = { "predicate", "consequent", "alternative" };
			vub.ast.Node tester = (vub.ast.Node)intcon.newInstance(null, names, contents);
			StartTiamat.functions.add(new Templates("If:Then:Else", tester));
		/*	NodeList nodes = doc.getElementsByTagName("template");
			//NodeList operations = doc.getElementsByTagName("operation");
			System.out.println("In Template reader");

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String type = element.getAttribute("type");
					if (type.equals("function")){
						function(element);
					}else if(type == "definition"){
						definition(element);
					}else if(type == "operation"){
						operation(element);
					}else System.out.println("Wrong Type");
					
					//String type = element.getAttribute("type");
					String name = element.getAttribute("name");
					Element nam = (Element) element.getElementsByTagName("edu.vub.ast.Function").item(0);
					Node node2 = element.getFirstChild();
					//Node tester = node2.getFirstChild();
					boolean bool = node2.hasChildNodes();
					//String name2 = nam.getAttribute("type");
					
					name = element.getNodeName();
					String name2 ;//= nam.getTagName();
					name2 = node2.getNodeName();*/
					
					
					//Class<?> c = Class.forName(name2);
					//Element naom = (Element) nam.getElementsByTagName("name").item(0);
					//name2 = naom.getTextContent();
					//Node args = element.getElementsByTagName("args").item(0);
					//String lol = element.getFirstChild().getPrefix();
					
					//	System.out.println("Temp" + test);
					//String[] names = getValues("name", element);
					//String[] content = getValues("content", element);
					//vub.ast.Node node1 = new vub.ast.Function(null, names, content);
					//Templates template =  new Templates(title, node1);
					//StartTiamat.functions.add(template);
					System.out.println("Templates"+ name +name2+ bool+tester);
					
					//} 
			//}
		} catch (Exception ex) {
			System.out.println("TemplatesError");
			ex.printStackTrace();
		}
	}
	 /* StartTiamat.functions.add(IfThenElse());
	 public Templates IfThenElse() {
	 	String names[] = { "if:", "then:", "else:" };
	 	String contents[] = { "predicate", "consequent", "alternative" };
	 	vub.ast.Node node = new vub.ast.Function(null, names, contents);
	 	return new Templates("If:Then:Else", node);
	 }
	 */
	private static void function(Element e){
		Element Element = (Element) e.getElementsByTagName("edu.vub.ast.Function").item(0);
		String name = e.getElementsByTagName("name").item(0).getTextContent();
		NodeList arguments = e.getElementsByTagName("edu.vub.ast.PlaceHolder");
		int length = arguments.getLength();
		String contents[] = new String[length];
		String names[] = new String[length];
		for (int i = 0; i < length; i++){
			Node node = arguments.item(i);
			names[i] = node.getTextContent();
			contents[i] = ((Element)node).getAttribute("hint");
		}
		vub.ast.Node node = new vub.ast.Function(null, names, contents);
		StartTiamat.functions.add(new Templates(name, node));
	}
	
	private static void definition(Element e){
		
	}
	
	private static void operation(Element e){
		
	}
	
	private static String[] getValues(String tag, Element element) {

		int length = element.getElementsByTagName(tag).getLength();
		String[] values = new String[length];
		for (int i = 0; i < length; i++) {
			NodeList nodes = element.getElementsByTagName(tag).item(i)
					.getChildNodes();
			Node node = (Node) nodes.item(0);
			values[i] = node.getNodeValue();
		}
		return values;
	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0)
				.getChildNodes();
		Node node = (Node) nodes.item(0);

		return node.getNodeValue();
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
