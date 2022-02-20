package org.apache.xml.dtm;

import org.w3c.dom.DOMException;

public class DTMDOMException extends DOMException {
   public DTMDOMException(short code, String message) {
      super(code, message);
   }

   public DTMDOMException(short code) {
      super(code, "");
   }
}
