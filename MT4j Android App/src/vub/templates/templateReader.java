package vub.templates;
import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.res.AssetManager;

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

	public static void templateReader() {
		AssetManager assetManager = getAssets();
		InputStream templates = null;
		try {

			System.out.println("In Template reader");
			templates = assetManager.open("templates.xml");
			//File templates = new File(inputStream);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.parse(templates);
			System.out.println("In Template reader");
			doc.getDocumentElement().normalize();

			NodeList nodes = doc.getElementsByTagName("function");
			NodeList operations = doc.getElementsByTagName("operation");


			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String title = getValue("title", element);
					String[] names = getValues("name", element);
					String[] content = getValues("content", element);
					vub.ast.Node node1 = new vub.ast.Function(null, names, content);
					Templates template =  new Templates(title, node1);
					StartTiamat.functions.add(template);
					System.out.println("Templates" + title);
					
					}
			}
		} catch (Exception ex) {
			System.out.println("TemplatesError");
			ex.printStackTrace();
		}
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
}
