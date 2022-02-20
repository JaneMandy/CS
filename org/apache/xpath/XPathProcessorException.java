package org.apache.xpath;

public class XPathProcessorException extends XPathException {
   public XPathProcessorException(String message) {
      super(message);
   }

   public XPathProcessorException(String message, Exception e) {
      super(message, e);
   }
}
