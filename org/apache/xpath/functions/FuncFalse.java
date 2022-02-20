package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncFalse extends Function {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      return XBoolean.S_FALSE;
   }

   public void fixupVariables(Vector vars, int globalsSize) {
   }
}
