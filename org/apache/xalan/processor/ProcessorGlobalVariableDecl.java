package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.xml.sax.SAXException;

class ProcessorGlobalVariableDecl extends ProcessorTemplateElem {
   protected void appendAndPush(StylesheetHandler handler, ElemTemplateElement elem) throws SAXException {
      handler.pushElemTemplateElement(elem);
   }

   public void endElement(StylesheetHandler handler, String uri, String localName, String rawName) throws SAXException {
      ElemVariable v = (ElemVariable)handler.getElemTemplateElement();
      handler.getStylesheet().appendChild((ElemTemplateElement)v);
      handler.getStylesheet().setVariable(v);
      super.endElement(handler, uri, localName, rawName);
   }
}
