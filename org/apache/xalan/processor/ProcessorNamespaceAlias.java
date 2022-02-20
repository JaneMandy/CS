package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.NamespaceAlias;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class ProcessorNamespaceAlias extends XSLTElementProcessor {
   public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      NamespaceAlias na = new NamespaceAlias(handler.nextUid());
      this.setPropertiesFromAttributes(handler, rawName, attributes, na);
      String prefix = na.getStylesheetPrefix();
      if (prefix.equals("#default")) {
         prefix = "";
         na.setStylesheetPrefix(prefix);
      }

      String stylesheetNS = handler.getNamespaceForPrefix(prefix);
      na.setStylesheetNamespace(stylesheetNS);
      prefix = na.getResultPrefix();
      if (prefix.equals("#default")) {
         prefix = "";
         na.setResultPrefix(prefix);
      }

      String resultNS = handler.getNamespaceForPrefix(prefix);
      na.setResultNamespace(resultNS);
      handler.getStylesheet().setNamespaceAlias(na);
      handler.getStylesheet().appendChild((ElemTemplateElement)na);
   }
}
