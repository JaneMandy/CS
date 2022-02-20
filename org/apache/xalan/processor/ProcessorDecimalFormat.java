package org.apache.xalan.processor;

import org.apache.xalan.templates.DecimalFormatProperties;
import org.apache.xalan.templates.ElemTemplateElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class ProcessorDecimalFormat extends XSLTElementProcessor {
   public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      DecimalFormatProperties dfp = new DecimalFormatProperties(handler.nextUid());
      dfp.setDOMBackPointer(handler.getOriginatingNode());
      dfp.setLocaterInfo(handler.getLocator());
      this.setPropertiesFromAttributes(handler, rawName, attributes, dfp);
      handler.getStylesheet().setDecimalFormat(dfp);
      handler.getStylesheet().appendChild((ElemTemplateElement)dfp);
   }
}
