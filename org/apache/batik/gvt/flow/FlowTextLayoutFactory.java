package org.apache.batik.gvt.flow;

import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import org.apache.batik.gvt.text.TextLayoutFactory;
import org.apache.batik.gvt.text.TextSpanLayout;

public class FlowTextLayoutFactory implements TextLayoutFactory {
   public TextSpanLayout createTextLayout(AttributedCharacterIterator var1, int[] var2, Point2D var3, FontRenderContext var4) {
      return new FlowGlyphLayout(var1, var2, var3, var4);
   }
}
