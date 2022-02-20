package org.apache.xml.dtm.ref.dom2dtm;

import org.apache.xml.dtm.DTMException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOM2DTMdefaultNamespaceDeclarationNode implements Attr {
   final String NOT_SUPPORTED_ERR = "Unsupported operation on pseudonode";
   Element pseudoparent;
   String prefix;
   String uri;
   String nodename;
   int handle;

   DOM2DTMdefaultNamespaceDeclarationNode(Element pseudoparent, String prefix, String uri, int handle) {
      this.pseudoparent = pseudoparent;
      this.prefix = prefix;
      this.uri = uri;
      this.handle = handle;
      this.nodename = "xmlns:" + prefix;
   }

   public String getNodeName() {
      return this.nodename;
   }

   public String getName() {
      return this.nodename;
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2000/xmlns/";
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getLocalName() {
      return this.prefix;
   }

   public String getNodeValue() {
      return this.uri;
   }

   public String getValue() {
      return this.uri;
   }

   public Element getOwnerElement() {
      return this.pseudoparent;
   }

   public boolean isSupported(String feature, String version) {
      return false;
   }

   public boolean hasChildNodes() {
      return false;
   }

   public boolean hasAttributes() {
      return false;
   }

   public Node getParentNode() {
      return null;
   }

   public Node getFirstChild() {
      return null;
   }

   public Node getLastChild() {
      return null;
   }

   public Node getPreviousSibling() {
      return null;
   }

   public Node getNextSibling() {
      return null;
   }

   public boolean getSpecified() {
      return false;
   }

   public void normalize() {
   }

   public NodeList getChildNodes() {
      return null;
   }

   public NamedNodeMap getAttributes() {
      return null;
   }

   public short getNodeType() {
      return 2;
   }

   public void setNodeValue(String value) {
      throw new DTMException("Unsupported operation on pseudonode");
   }

   public void setValue(String value) {
      throw new DTMException("Unsupported operation on pseudonode");
   }

   public void setPrefix(String value) {
      throw new DTMException("Unsupported operation on pseudonode");
   }

   public Node insertBefore(Node a, Node b) {
      throw new DTMException("Unsupported operation on pseudonode");
   }

   public Node replaceChild(Node a, Node b) {
      throw new DTMException("Unsupported operation on pseudonode");
   }

   public Node appendChild(Node a) {
      throw new DTMException("Unsupported operation on pseudonode");
   }

   public Node removeChild(Node a) {
      throw new DTMException("Unsupported operation on pseudonode");
   }

   public Document getOwnerDocument() {
      return this.pseudoparent.getOwnerDocument();
   }

   public Node cloneNode(boolean deep) {
      throw new DTMException("Unsupported operation on pseudonode");
   }

   public int getHandleOfNode() {
      return this.handle;
   }
}
