package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPath;

public class ElemVariablePsuedo extends ElemVariable {
   XUnresolvedVariableSimple m_lazyVar;

   public void setSelect(XPath v) {
      super.setSelect(v);
      this.m_lazyVar = new XUnresolvedVariableSimple(this);
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      transformer.getXPathContext().getVarStack().setLocalVariable(super.m_index, this.m_lazyVar);
   }
}
