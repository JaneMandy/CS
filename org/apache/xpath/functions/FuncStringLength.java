package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncStringLength extends FunctionDef1Arg {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      return new XNumber((double)this.getArg0AsString(xctxt).length());
   }
}
