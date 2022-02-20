package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncStartsWith extends Function2Args {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      return super.m_arg0.execute(xctxt).xstr().startsWith(super.m_arg1.execute(xctxt).xstr()) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
   }
}
