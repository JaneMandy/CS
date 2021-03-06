package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class FuncFloor extends FunctionOneArg {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      return new XNumber(Math.floor(super.m_arg0.execute(xctxt).num()));
   }
}
