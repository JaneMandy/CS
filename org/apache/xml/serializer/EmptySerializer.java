package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class EmptySerializer implements SerializationHandler {
   protected static final String ERR = "EmptySerializer method not over-ridden";

   private static void throwUnimplementedException() {
   }

   public ContentHandler asContentHandler() throws IOException {
      throwUnimplementedException();
      return null;
   }

   public void setContentHandler(ContentHandler ch) {
      throwUnimplementedException();
   }

   public void close() {
      throwUnimplementedException();
   }

   public Properties getOutputFormat() {
      throwUnimplementedException();
      return null;
   }

   public OutputStream getOutputStream() {
      throwUnimplementedException();
      return null;
   }

   public Writer getWriter() {
      throwUnimplementedException();
      return null;
   }

   public boolean reset() {
      throwUnimplementedException();
      return false;
   }

   public void serialize(Node node) throws IOException {
      throwUnimplementedException();
   }

   public void setCdataSectionElements(Vector URI_and_localNames) {
      throwUnimplementedException();
   }

   public boolean setEscaping(boolean escape) throws SAXException {
      throwUnimplementedException();
      return false;
   }

   public void setIndent(boolean indent) {
      throwUnimplementedException();
   }

   public void setIndentAmount(int spaces) {
      throwUnimplementedException();
   }

   public void setOutputFormat(Properties format) {
      throwUnimplementedException();
   }

   public void setOutputStream(OutputStream output) {
      throwUnimplementedException();
   }

   public void setVersion(String version) {
      throwUnimplementedException();
   }

   public void setWriter(Writer writer) {
      throwUnimplementedException();
   }

   public void setTransformer(Transformer transformer) {
      throwUnimplementedException();
   }

   public Transformer getTransformer() {
      throwUnimplementedException();
      return null;
   }

   public void flushPending() throws SAXException {
      throwUnimplementedException();
   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value) throws SAXException {
      throwUnimplementedException();
   }

   public void addAttributes(Attributes atts) throws SAXException {
      throwUnimplementedException();
   }

   public void addAttribute(String name, String value) {
      throwUnimplementedException();
   }

   public void characters(String chars) throws SAXException {
      throwUnimplementedException();
   }

   public void endElement(String elemName) throws SAXException {
      throwUnimplementedException();
   }

   public void startDocument() throws SAXException {
      throwUnimplementedException();
   }

   public void startElement(String uri, String localName, String qName) throws SAXException {
      throwUnimplementedException();
   }

   public void startElement(String qName) throws SAXException {
      throwUnimplementedException();
   }

   public void namespaceAfterStartElement(String uri, String prefix) throws SAXException {
      throwUnimplementedException();
   }

   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
      throwUnimplementedException();
      return false;
   }

   public void entityReference(String entityName) throws SAXException {
      throwUnimplementedException();
   }

   public NamespaceMappings getNamespaceMappings() {
      throwUnimplementedException();
      return null;
   }

   public String getPrefix(String uri) {
      throwUnimplementedException();
      return null;
   }

   public String getNamespaceURI(String name, boolean isElement) {
      throwUnimplementedException();
      return null;
   }

   public String getNamespaceURIFromPrefix(String prefix) {
      throwUnimplementedException();
      return null;
   }

   public void setDocumentLocator(Locator arg0) {
      throwUnimplementedException();
   }

   public void endDocument() throws SAXException {
      throwUnimplementedException();
   }

   public void startPrefixMapping(String arg0, String arg1) throws SAXException {
      throwUnimplementedException();
   }

   public void endPrefixMapping(String arg0) throws SAXException {
      throwUnimplementedException();
   }

   public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
      throwUnimplementedException();
   }

   public void endElement(String arg0, String arg1, String arg2) throws SAXException {
      throwUnimplementedException();
   }

   public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
      throwUnimplementedException();
   }

   public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
      throwUnimplementedException();
   }

   public void processingInstruction(String arg0, String arg1) throws SAXException {
      throwUnimplementedException();
   }

   public void skippedEntity(String arg0) throws SAXException {
      throwUnimplementedException();
   }

   public void comment(String comment) throws SAXException {
      throwUnimplementedException();
   }

   public void startDTD(String arg0, String arg1, String arg2) throws SAXException {
      throwUnimplementedException();
   }

   public void endDTD() throws SAXException {
      throwUnimplementedException();
   }

   public void startEntity(String arg0) throws SAXException {
      throwUnimplementedException();
   }

   public void endEntity(String arg0) throws SAXException {
      throwUnimplementedException();
   }

   public void startCDATA() throws SAXException {
      throwUnimplementedException();
   }

   public void endCDATA() throws SAXException {
      throwUnimplementedException();
   }

   public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
      throwUnimplementedException();
   }

   public String getDoctypePublic() {
      throwUnimplementedException();
      return null;
   }

   public String getDoctypeSystem() {
      throwUnimplementedException();
      return null;
   }

   public String getEncoding() {
      throwUnimplementedException();
      return null;
   }

   public boolean getIndent() {
      throwUnimplementedException();
      return false;
   }

   public int getIndentAmount() {
      throwUnimplementedException();
      return 0;
   }

   public String getMediaType() {
      throwUnimplementedException();
      return null;
   }

   public boolean getOmitXMLDeclaration() {
      throwUnimplementedException();
      return false;
   }

   public String getStandalone() {
      throwUnimplementedException();
      return null;
   }

   public String getVersion() {
      throwUnimplementedException();
      return null;
   }

   public void setCdataSectionElements(Hashtable h) throws Exception {
      throwUnimplementedException();
   }

   public void setDoctype(String system, String pub) {
      throwUnimplementedException();
   }

   public void setDoctypePublic(String doctype) {
      throwUnimplementedException();
   }

   public void setDoctypeSystem(String doctype) {
      throwUnimplementedException();
   }

   public void setEncoding(String encoding) {
      throwUnimplementedException();
   }

   public void setMediaType(String mediatype) {
      throwUnimplementedException();
   }

   public void setOmitXMLDeclaration(boolean b) {
      throwUnimplementedException();
   }

   public void setStandalone(String standalone) {
      throwUnimplementedException();
   }

   public void elementDecl(String arg0, String arg1) throws SAXException {
      throwUnimplementedException();
   }

   public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4) throws SAXException {
      throwUnimplementedException();
   }

   public void internalEntityDecl(String arg0, String arg1) throws SAXException {
      throwUnimplementedException();
   }

   public void externalEntityDecl(String arg0, String arg1, String arg2) throws SAXException {
      throwUnimplementedException();
   }

   public void warning(SAXParseException arg0) throws SAXException {
      throwUnimplementedException();
   }

   public void error(SAXParseException arg0) throws SAXException {
      throwUnimplementedException();
   }

   public void fatalError(SAXParseException arg0) throws SAXException {
      throwUnimplementedException();
   }

   public DOMSerializer asDOMSerializer() throws IOException {
      throwUnimplementedException();
      return null;
   }

   public void setNamespaceMappings(NamespaceMappings mappings) {
      throwUnimplementedException();
   }

   public void setSourceLocator(SourceLocator locator) {
      throwUnimplementedException();
   }

   public void addUniqueAttribute(String name, String value, int flags) throws SAXException {
      throwUnimplementedException();
   }
}
