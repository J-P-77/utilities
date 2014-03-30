/*
The MIT License (MIT)

Copyright (c) 2014 Justin Palinkas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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
