package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMDOMException;
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

public class DTMNodeProxy implements Node, Document, Text, Element, Attr, ProcessingInstruction, Comment, DocumentFragment {
   public DTM dtm;
   int node;
   static final DOMImplementation implementation = new DTMNodeProxy.DTMNodeProxyImplementation();

   public DTMNodeProxy(DTM dtm, int node) {
      this.dtm = dtm;
      this.node = node;
   }

   public final DTM getDTM() {
      return this.dtm;
   }

   public final int getDTMNodeNumber() {
      return this.node;
   }

   public final boolean equals(Node node) {
      try {
         DTMNodeProxy dtmp = (DTMNodeProxy)node;
         return dtmp.node == this.node && dtmp.dtm == this.dtm;
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public final boolean equals(Object node) {
      try {
         return this.equals((Node)node);
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public final boolean sameNodeAs(Node other) {
      if (!(other instanceof DTMNodeProxy)) {
         return false;
      } else {
         DTMNodeProxy that = (DTMNodeProxy)other;
         return this.dtm == that.dtm && this.node == that.node;
      }
   }

   public final String getNodeName() {
      return this.dtm.getNodeName(this.node);
   }

   public final String getTarget() {
      return this.dtm.getNodeName(this.node);
   }

   public final String getLocalName() {
      return this.dtm.getLocalName(this.node);
   }

   public final String getPrefix() {
      return this.dtm.getPrefix(this.node);
   }

   public final void setPrefix(String prefix) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final String getNamespaceURI() {
      return this.dtm.getNamespaceURI(this.node);
   }

   public final boolean supports(String feature, String version) {
      return implementation.hasFeature(feature, version);
   }

   public final boolean isSupported(String feature, String version) {
      return implementation.hasFeature(feature, version);
   }

   public final String getNodeValue() throws DOMException {
      return this.dtm.getNodeValue(this.node);
   }

   public final String getStringValue() throws DOMException {
      return this.dtm.getStringValue(this.node).toString();
   }

   public final void setNodeValue(String nodeValue) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final short getNodeType() {
      return this.dtm.getNodeType(this.node);
   }

   public final Node getParentNode() {
      if (this.getNodeType() == 2) {
         return null;
      } else {
         int newnode = this.dtm.getParent(this.node);
         return newnode == -1 ? null : this.dtm.getNode(newnode);
      }
   }

   public final Node getOwnerNode() {
      int newnode = this.dtm.getParent(this.node);
      return newnode == -1 ? null : this.dtm.getNode(newnode);
   }

   public final NodeList getChildNodes() {
      return new DTMChildIterNodeList(this.dtm, this.node);
   }

   public final Node getFirstChild() {
      int newnode = this.dtm.getFirstChild(this.node);
      return newnode == -1 ? null : this.dtm.getNode(newnode);
   }

   public final Node getLastChild() {
      int newnode = this.dtm.getLastChild(this.node);
      return newnode == -1 ? null : this.dtm.getNode(newnode);
   }

   public final Node getPreviousSibling() {
      int newnode = this.dtm.getPreviousSibling(this.node);
      return newnode == -1 ? null : this.dtm.getNode(newnode);
   }

   public final Node getNextSibling() {
      if (this.dtm.getNodeType(this.node) == 2) {
         return null;
      } else {
         int newnode = this.dtm.getNextSibling(this.node);
         return newnode == -1 ? null : this.dtm.getNode(newnode);
      }
   }

   public final NamedNodeMap getAttributes() {
      return new DTMNamedNodeMap(this.dtm, this.node);
   }

   public boolean hasAttribute(String name) {
      return -1 != this.dtm.getAttributeNode(this.node, (String)null, name);
   }

   public boolean hasAttributeNS(String name, String x) {
      return -1 != this.dtm.getAttributeNode(this.node, x, name);
   }

   public final Document getOwnerDocument() {
      return (Document)this.dtm.getNode(this.dtm.getOwnerDocument(this.node));
   }

   public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final Node removeChild(Node oldChild) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final Node appendChild(Node newChild) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final boolean hasChildNodes() {
      return -1 != this.dtm.getFirstChild(this.node);
   }

   public final Node cloneNode(boolean deep) {
      throw new DTMDOMException((short)9);
   }

   public final DocumentType getDoctype() {
      return null;
   }

   public final DOMImplementation getImplementation() {
      return implementation;
   }

   public final Element getDocumentElement() {
      int dochandle = this.dtm.getDocument();
      int elementhandle = -1;

      for(int kidhandle = this.dtm.getFirstChild(dochandle); kidhandle != -1; kidhandle = this.dtm.getNextSibling(kidhandle)) {
         switch(this.dtm.getNodeType(kidhandle)) {
         case 1:
            if (elementhandle != -1) {
               elementhandle = -1;
               kidhandle = this.dtm.getLastChild(dochandle);
            } else {
               elementhandle = kidhandle;
            }
            break;
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 9:
         default:
            elementhandle = -1;
            kidhandle = this.dtm.getLastChild(dochandle);
         case 7:
         case 8:
         case 10:
         }
      }

      if (elementhandle == -1) {
         throw new DTMDOMException((short)9);
      } else {
         return (Element)this.dtm.getNode(elementhandle);
      }
   }

   public final Element createElement(String tagName) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final DocumentFragment createDocumentFragment() {
      throw new DTMDOMException((short)9);
   }

   public final Text createTextNode(String data) {
      throw new DTMDOMException((short)9);
   }

   public final Comment createComment(String data) {
      throw new DTMDOMException((short)9);
   }

   public final CDATASection createCDATASection(String data) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr createAttribute(String name) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final EntityReference createEntityReference(String name) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final NodeList getElementsByTagName(String tagname) {
      throw new DTMDOMException((short)9);
   }

   public final Node importNode(Node importedNode, boolean deep) throws DOMException {
      throw new DTMDOMException((short)7);
   }

   public final Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
      throw new DTMDOMException((short)9);
   }

   public final Element getElementById(String elementId) {
      throw new DTMDOMException((short)9);
   }

   public final Text splitText(int offset) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final String getData() throws DOMException {
      return this.dtm.getNodeValue(this.node);
   }

   public final void setData(String data) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final int getLength() {
      return this.dtm.getNodeValue(this.node).length();
   }

   public final String substringData(int offset, int count) throws DOMException {
      return this.getData().substring(offset, offset + count);
   }

   public final void appendData(String arg) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void insertData(int offset, String arg) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void deleteData(int offset, int count) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void replaceData(int offset, int count, String arg) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final String getTagName() {
      return this.dtm.getNodeName(this.node);
   }

   public final String getAttribute(String name) {
      DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
      Node node = map.getNamedItem(name);
      return null == node ? null : node.getNodeValue();
   }

   public final void setAttribute(String name, String value) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void removeAttribute(String name) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr getAttributeNode(String name) {
      DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
      return (Attr)map.getNamedItem(name);
   }

   public final Attr setAttributeNode(Attr newAttr) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr removeAttributeNode(Attr oldAttr) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public boolean hasAttributes() {
      return -1 != this.dtm.getFirstAttribute(this.node);
   }

   public final void normalize() {
      throw new DTMDOMException((short)9);
   }

   public final String getAttributeNS(String namespaceURI, String localName) {
      DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
      Node node = map.getNamedItemNS(namespaceURI, localName);
      return null == node ? null : node.getNodeValue();
   }

   public final void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final Attr getAttributeNodeNS(String namespaceURI, String localName) {
      throw new DTMDOMException((short)9);
   }

   public final Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public final String getName() {
      return this.dtm.getNodeName(this.node);
   }

   public final boolean getSpecified() {
      return true;
   }

   public final String getValue() {
      return this.dtm.getNodeValue(this.node);
   }

   public final void setValue(String value) {
      throw new DTMDOMException((short)9);
   }

   public final Element getOwnerElement() {
      if (this.getNodeType() != 2) {
         return null;
      } else {
         int newnode = this.dtm.getParent(this.node);
         return newnode == -1 ? null : (Element)this.dtm.getNode(newnode);
      }
   }

   public Node adoptNode(Node source) throws DOMException {
      throw new DTMDOMException((short)9);
   }

   public String getEncoding() {
      throw new DTMDOMException((short)9);
   }

   public void setEncoding(String encoding) {
      throw new DTMDOMException((short)9);
   }

   public boolean getStandalone() {
      throw new DTMDOMException((short)9);
   }

   public void setStandalone(boolean standalone) {
      throw new DTMDOMException((short)9);
   }

   public boolean getStrictErrorChecking() {
      throw new DTMDOMException((short)9);
   }

   public void setStrictErrorChecking(boolean strictErrorChecking) {
      throw new DTMDOMException((short)9);
   }

   public String getVersion() {
      throw new DTMDOMException((short)9);
   }

   public void setVersion(String version) {
      throw new DTMDOMException((short)9);
   }

   static class DTMNodeProxyImplementation implements DOMImplementation {
      public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) {
         throw new DTMDOMException((short)9);
      }

      public Document createDocument(String namespaceURI, String qualfiedName, DocumentType doctype) {
         throw new DTMDOMException((short)9);
      }

      public boolean hasFeature(String feature, String version) {
         return ("CORE".equals(feature.toUpperCase()) || "XML".equals(feature.toUpperCase())) && ("1.0".equals(version) || "2.0".equals(version));
      }
   }
}
