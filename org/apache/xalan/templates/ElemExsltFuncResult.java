package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

public class ElemExsltFuncResult extends ElemVariable {
   public void execute(TransformerImpl transformer) throws TransformerException {
      XPathContext context = transformer.getXPathContext();
      if (TransformerImpl.S_DEBUG) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      if (transformer.currentFuncResultSeen()) {
         throw new TransformerException("An EXSLT function cannot set more than one result!");
      } else {
         int sourceNode = context.getCurrentNode();
         XObject var = this.getValue(transformer, sourceNode);
         transformer.popCurrentFuncResult();
         transformer.pushCurrentFuncResult(var);
         if (TransformerImpl.S_DEBUG) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

      }
   }

   public int getXSLToken() {
      return 89;
   }

   public String getNodeName() {
      return "result";
   }
}
