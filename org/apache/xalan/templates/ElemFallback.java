package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;

public class ElemFallback extends ElemTemplateElement {
   public int getXSLToken() {
      return 57;
   }

   public String getNodeName() {
      return "fallback";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
   }

   public void executeFallback(TransformerImpl transformer) throws TransformerException {
      int parentElemType = super.m_parentNode.getXSLToken();
      if (79 != parentElemType && -1 != parentElemType) {
         System.out.println("Error!  parent of xsl:fallback must be an extension or unknown element!");
      } else {
         if (TransformerImpl.S_DEBUG) {
            transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
         }

         transformer.executeChildTemplates(this, true);
         if (TransformerImpl.S_DEBUG) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }
      }

   }
}
