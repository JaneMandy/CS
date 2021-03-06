package org.apache.xalan.processor;

import java.util.Vector;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.WhiteSpaceInfo;

public class WhitespaceInfoPaths extends WhiteSpaceInfo {
   private Vector m_elements;

   public void setElements(Vector elems) {
      this.m_elements = elems;
   }

   Vector getElements() {
      return this.m_elements;
   }

   public void clearElements() {
      this.m_elements = null;
   }

   public WhitespaceInfoPaths(Stylesheet thisSheet) {
      super(thisSheet);
      this.setStylesheet(thisSheet);
   }
}
