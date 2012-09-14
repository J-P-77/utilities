package utillib.utilities;

import utillib.arrays.ResizingArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 
 * @author Dalton Dell
 */
public class XMLReader {
	private Document _Document = null;

	public boolean parse(String filepath) {
		return parse(new File(filepath));
	}

	public boolean parse(File file) {
		try {
			return parse(new FileInputStream(file));
		} catch(Exception e) {}

		return false;
	}

	public boolean parse(InputStream istream) {
		try {
			if(_Document == null) {
				final DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
				final DocumentBuilder DB = DBF.newDocumentBuilder();

				_Document = DB.parse(istream);

				return true;
			}
		} catch(Exception e) {}

		return false;
	}

	public Node getFirstNode() {
		return _Document.getDocumentElement();
	}

	public Node[] getNodes(Node node, String name) {
		final ResizingArray<Node> NODES = new ResizingArray<Node>();

		for(int X = 0; X < node.getChildNodes().getLength(); X++) {
			if(node.getChildNodes().item(X).getNodeName().equals(name)) {
				NODES.put(node.getChildNodes().item(X));
			}
		}

		return (NODES.length() == 0 ? null : NODES.toArray(new Node[NODES.length()]));
	}

	public Node getNode(Node node, String name) {
		for(int X = 0; X < node.getChildNodes().getLength(); X++) {
			if(node.getChildNodes().item(X).getNodeName().equals(name)) {
				return node.getChildNodes().item(X);
			}
		}

		return null;
	}

	public Node getAttribute(Node node, String name) {
		return node.getAttributes().getNamedItem(name);
	}
/*
    public static void main(String[] args) {
        final XMLReader READER = new XMLReader();

//        READER.parse("C:\\Documents and Settings\\Dalton Dell\\Desktop\\test.xml");
        
//        final Node[] EMPLOYEES = READER.getNodes(READER.getFirstNode(), "employee");
//
//        for(int X = 0; X < EMPLOYEES.length; X++) {
//            final Node FIRST = READER.getNode(EMPLOYEES[X], "firstname");
//            final Node LAST = READER.getNode(EMPLOYEES[X], "lastname");
//
//            System.out.println("employee " + X + ": " + FIRST.getTextContent() + ' ' + LAST.getTextContent());
//        }

        READER.parse("C:\\Documents and Settings\\Dalton Dell\\Desktop\\windowsweeklyrss.xml");

        System.out.println(READER.getFirstNode().getNodeName());
        System.out.println(READER.getAttribute(READER.getFirstNode(), "xmlns:atom").getTextContent());

        final Node CHANNEL = READER.getNode(READER.getFirstNode(), "channel");
        final Node[] ITEMS = READER.getNodes(CHANNEL, "item");

        for(int X = 0; X < ITEMS.length; X++) {
            final Node TITLE = READER.getNode(ITEMS[X], "title");
            final Node LINK = READER.getNode(ITEMS[X], "link");

            System.out.println("Item " + X + ": " + TITLE.getTextContent() + ' ' + LINK.getTextContent());
        }
    }*/
}
