package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncGenerateId extends FunctionDef1Arg {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      int which = this.getArg0AsNode(xctxt);
      return -1 != which ? new XString("N" + Integer.toHexString(which).toUpperCase()) : XString.EMPTYSTRING;
   }
}
