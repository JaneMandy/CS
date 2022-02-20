package org.apache.xml.utils;

import org.apache.xml.res.XMLMessages;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class UnImplNode implements Node, Element, NodeList, Document {
   public void error(String msg) {
      System.out.println("DOM ERROR! class: " + this.getClass().getName());
      throw new RuntimeException(XMLMessages.createXMLMessage(msg, (Object[])null));
   }

   public void error(String msg, Object[] args) {
      System.out.println("DOM ERROR! class: " + this.getClass().getName());
      throw new RuntimeException(XMLMessages.createXMLMessage(msg, args));
   }

   public Node appendChild(Node newChild) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean hasChildNodes() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public short getNodeType() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return 0;
   }

   public Node getParentNode() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public NodeList getChildNodes() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node getFirstChild() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node getLastChild() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node getNextSibling() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public int getLength() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return 0;
   }

   public Node item(int index) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Document getOwnerDocument() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getTagName() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getNodeName() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void normalize() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public NodeList getElementsByTagName(String name) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr setAttributeNode(Attr newAttr) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean hasAttribute(String name) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public boolean hasAttributeNS(String name, String x) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public Attr getAttributeNode(String name) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void removeAttribute(String name) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void setAttribute(String name, String value) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String getAttribute(String name) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean hasAttributes() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr getAttributeNodeNS(String namespaceURI, String localName) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String getAttributeNS(String namespaceURI, String localName) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node getPreviousSibling() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node cloneNode(boolean deep) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getNodeValue() throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setNodeValue(String nodeValue) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void setValue(String value) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public Element getOwnerElement() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean getSpecified() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public NamedNodeMap getAttributes() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node insertBefore(Node newChild, Node refChild) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node removeChild(Node oldChild) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public boolean isSupported(String feature, String version) {
      return false;
   }

   public String getNamespaceURI() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getPrefix() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setPrefix(String prefix) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String getLocalName() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public DocumentType getDoctype() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public DOMImplementation getImplementation() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Element getDocumentElement() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Element createElement(String tagName) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public DocumentFragment createDocumentFragment() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Text createTextNode(String data) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Comment createComment(String data) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public CDATASection createCDATASection(String data) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr createAttribute(String name) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public EntityReference createEntityReference(String name) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node importNode(Node importedNode, boolean deep) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Element getElementById(String elementId) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setData(String data) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String substringData(int offset, int count) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void appendData(String arg) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void insertData(int offset, String arg) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void deleteData(int offset, int count) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public void replaceData(int offset, int count, String arg) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public Text splitText(int offset) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public Node adoptNode(Node source) throws DOMException {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public String getEncoding() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setEncoding(String encoding) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public boolean getStandalone() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public void setStandalone(boolean standalone) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public boolean getStrictErrorChecking() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return false;
   }

   public void setStrictErrorChecking(boolean strictErrorChecking) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }

   public String getVersion() {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
      return null;
   }

   public void setVersion(String version) {
      this.error("ER_FUNCTION_NOT_SUPPORTED");
   }
}
