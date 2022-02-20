package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncBoolean extends FunctionOneArg {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      return super.m_arg0.execute(xctxt).bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
   }
}
