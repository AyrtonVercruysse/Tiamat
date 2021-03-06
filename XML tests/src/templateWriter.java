import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class templateWriter {
    public static void templateWriter(String[] args) throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("src/config2.xml");
        Element root = document.getDocumentElement();

        
        // Root Element
        Element rootElement = document.getDocumentElement();

        
        Collection<Function> svr = new ArrayList<Function>();
        svr.add(new Function());

        for (Function i : svr) {
            // server elements
            Element server = document.createElement("server");
            rootElement.appendChild(server);

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(i.getName()));
            server.appendChild(name);
            
            Element name2 = document.createElement("name");
            name2.appendChild(document.createTextNode(i.getName()));
            server.appendChild(name2);

            Element port = document.createElement("port");
            port.appendChild(document.createTextNode(Integer.toString(i.getPort())));
            server.appendChild(port);

            root.appendChild(server);
        }

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult("src/config2.xml");
        transformer.transform(source, result);
    }

    public static class Function {
        public String getName() { return "foo"; }
        public Integer getPort() { return 12345; }
    }
}