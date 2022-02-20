package org.apache.xalan.templates;

import org.apache.xpath.XPath;

public class WhiteSpaceInfo extends ElemTemplate {
   private boolean m_shouldStripSpace;

   public boolean getShouldStripSpace() {
      return this.m_shouldStripSpace;
   }

   public WhiteSpaceInfo(Stylesheet thisSheet) {
      this.setStylesheet(thisSheet);
   }

   public WhiteSpaceInfo(XPath matchPattern, boolean shouldStripSpace, Stylesheet thisSheet) {
      this.m_shouldStripSpace = shouldStripSpace;
      this.setMatch(matchPattern);
      this.setStylesheet(thisSheet);
   }

   public void recompose(StylesheetRoot root) {
      root.recomposeWhiteSpaceInfo(this);
   }
}
