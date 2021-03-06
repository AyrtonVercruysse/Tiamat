import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class templateReader {

	public static void main(String args[]) {
		try {

			File templates = new File("src/templates.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(templates);
			doc.getDocumentElement().normalize();

			System.out.println("root of xml file "
					+ doc.getDocumentElement().getNodeName());
			NodeList nodes = doc.getElementsByTagName("function");
			NodeList operations = doc.getElementsByTagName("operation");
			System.out.println("==========================");

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;

					System.out.println("Function Title: "   + showContents(getValues("title", element)));
					System.out.println("Function Name: "    + showContents(getValues("name", element)));
					System.out.println("Function content: "	+ showContents(getValues("content", element)));
				}
			}
		} catch (Exception ex) {
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
	
	private static String showContents(String[] text){
		String res = "";
		for(int i = 0; i < text.length; i++){
			res = res + " " + text[i];
		}
		return res;
	}
}
