package org.apache.xml.utils;

import org.xml.sax.SAXException;

public class StopParseException extends SAXException {
   StopParseException() {
      super("Stylesheet PIs found, stop the parse");
   }
}
