package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncContains extends Function2Args {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      String s1 = super.m_arg0.execute(xctxt).str();
      String s2 = super.m_arg1.execute(xctxt).str();
      if (s1.length() == 0 && s2.length() == 0) {
         return XBoolean.S_TRUE;
      } else {
         int index = s1.indexOf(s2);
         return index > -1 ? XBoolean.S_TRUE : XBoolean.S_FALSE;
      }
   }
}
