package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Keywords;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class FuncExtFunctionAvailable extends FunctionOneArg {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      String fullName = super.m_arg0.execute(xctxt).str();
      int indexOfNSSep = fullName.indexOf(58);
      String prefix;
      String namespace;
      String methName;
      if (indexOfNSSep < 0) {
         prefix = "";
         namespace = "http://www.w3.org/1999/XSL/Transform";
         methName = fullName;
      } else {
         prefix = fullName.substring(0, indexOfNSSep);
         namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
         if (null == namespace) {
            return XBoolean.S_FALSE;
         }

         methName = fullName.substring(indexOfNSSep + 1);
      }

      if (namespace.equals("http://www.w3.org/1999/XSL/Transform")) {
         try {
            return Keywords.functionAvailable(methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
         } catch (Exception var8) {
            return XBoolean.S_FALSE;
         }
      } else {
         ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
         return extProvider.functionAvailable(namespace, methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
      }
   }
}
