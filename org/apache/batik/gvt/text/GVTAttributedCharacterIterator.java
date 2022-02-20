package org.apache.batik.gvt.text;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;
import java.util.Set;

public interface GVTAttributedCharacterIterator extends AttributedCharacterIterator {
   void setString(String var1);

   void setString(AttributedString var1);

   void setAttributeArray(GVTAttributedCharacterIterator.TextAttribute var1, Object[] var2, int var3, int var4);

   Set getAllAttributeKeys();

   Object getAttribute(Attribute var1);

   Map getAttributes();

   int getRunLimit();

   int getRunLimit(Attribute var1);

   int getRunLimit(Set var1);

   int getRunStart();

   int getRunStart(Attribute var1);

   int getRunStart(Set var1);

   Object clone();

   char current();

   char first();

   int getBeginIndex();

   int getEndIndex();

   int getIndex();

   char last();

   char next();

   char previous();

   char setIndex(int var1);

   public interface AttributeFilter {
      AttributedCharacterIterator mutateAttributes(AttributedCharacterIterator var1);
   }

   public static class TextAttribute extends Attribute {
      public static final GVTAttributedCharacterIterator.TextAttribute FLOW_PARAGRAPH = new GVTAttributedCharacterIterator.TextAttribute("FLOW_PARAGRAPH");
      public static final GVTAttributedCharacterIterator.TextAttribute FLOW_EMPTY_PARAGRAPH = new GVTAttributedCharacterIterator.TextAttribute("FLOW_EMPTY_PARAGRAPH");
      public static final GVTAttributedCharacterIterator.TextAttribute FLOW_LINE_BREAK = new GVTAttributedCharacterIterator.TextAttribute("FLOW_LINE_BREAK");
      public static final GVTAttributedCharacterIterator.TextAttribute FLOW_REGIONS = new GVTAttributedCharacterIterator.TextAttribute("FLOW_REGIONS");
      public static final GVTAttributedCharacterIterator.TextAttribute LINE_HEIGHT = new GVTAttributedCharacterIterator.TextAttribute("LINE_HEIGHT");
      public static final GVTAttributedCharacterIterator.TextAttribute PREFORMATTED = new GVTAttributedCharacterIterator.TextAttribute("PREFORMATTED");
      public static final GVTAttributedCharacterIterator.TextAttribute TEXT_COMPOUND_DELIMITER = new GVTAttributedCharacterIterator.TextAttribute("TEXT_COMPOUND_DELIMITER");
      public static final GVTAttributedCharacterIterator.TextAttribute TEXT_COMPOUND_ID = new GVTAttributedCharacterIterator.TextAttribute("TEXT_COMPOUND_ID");
      public static final GVTAttributedCharacterIterator.TextAttribute ANCHOR_TYPE = new GVTAttributedCharacterIterator.TextAttribute("ANCHOR_TYPE");
      public static final GVTAttributedCharacterIterator.TextAttribute EXPLICIT_LAYOUT = new GVTAttributedCharacterIterator.TextAttribute("EXPLICIT_LAYOUT");
      public static final GVTAttributedCharacterIterator.TextAttribute X = new GVTAttributedCharacterIterator.TextAttribute("X");
      public static final GVTAttributedCharacterIterator.TextAttribute Y = new GVTAttributedCharacterIterator.TextAttribute("Y");
      public static final GVTAttributedCharacterIterator.TextAttribute DX = new GVTAttributedCharacterIterator.TextAttribute("DX");
      public static final GVTAttributedCharacterIterator.TextAttribute DY = new GVTAttributedCharacterIterator.TextAttribute("DY");
      public static final GVTAttributedCharacterIterator.TextAttribute ROTATION = new GVTAttributedCharacterIterator.TextAttribute("ROTATION");
      public static final GVTAttributedCharacterIterator.TextAttribute PAINT_INFO = new GVTAttributedCharacterIterator.TextAttribute("PAINT_INFO");
      public static final GVTAttributedCharacterIterator.TextAttribute BBOX_WIDTH = new GVTAttributedCharacterIterator.TextAttribute("BBOX_WIDTH");
      public static final GVTAttributedCharacterIterator.TextAttribute LENGTH_ADJUST = new GVTAttributedCharacterIterator.TextAttribute("LENGTH_ADJUST");
      public static final GVTAttributedCharacterIterator.TextAttribute CUSTOM_SPACING = new GVTAttributedCharacterIterator.TextAttribute("CUSTOM_SPACING");
      public static final GVTAttributedCharacterIterator.TextAttribute KERNING = new GVTAttributedCharacterIterator.TextAttribute("KERNING");
      public static final GVTAttributedCharacterIterator.TextAttribute LETTER_SPACING = new GVTAttributedCharacterIterator.TextAttribute("LETTER_SPACING");
      public static final GVTAttributedCharacterIterator.TextAttribute WORD_SPACING = new GVTAttributedCharacterIterator.TextAttribute("WORD_SPACING");
      public static final GVTAttributedCharacterIterator.TextAttribute TEXTPATH = new GVTAttributedCharacterIterator.TextAttribute("TEXTPATH");
      public static final GVTAttributedCharacterIterator.TextAttribute FONT_VARIANT = new GVTAttributedCharacterIterator.TextAttribute("FONT_VARIANT");
      public static final GVTAttributedCharacterIterator.TextAttribute BASELINE_SHIFT = new GVTAttributedCharacterIterator.TextAttribute("BASELINE_SHIFT");
      public static final GVTAttributedCharacterIterator.TextAttribute WRITING_MODE = new GVTAttributedCharacterIterator.TextAttribute("WRITING_MODE");
      public static final GVTAttributedCharacterIterator.TextAttribute VERTICAL_ORIENTATION = new GVTAttributedCharacterIterator.TextAttribute("VERTICAL_ORIENTATION");
      public static final GVTAttributedCharacterIterator.TextAttribute VERTICAL_ORIENTATION_ANGLE = new GVTAttributedCharacterIterator.TextAttribute("VERTICAL_ORIENTATION_ANGLE");
      public static final GVTAttributedCharacterIterator.TextAttribute HORIZONTAL_ORIENTATION_ANGLE = new GVTAttributedCharacterIterator.TextAttribute("HORIZONTAL_ORIENTATION_ANGLE");
      public static final GVTAttributedCharacterIterator.TextAttribute GVT_FONT_FAMILIES = new GVTAttributedCharacterIterator.TextAttribute("GVT_FONT_FAMILIES");
      public static final GVTAttributedCharacterIterator.TextAttribute GVT_FONTS = new GVTAttributedCharacterIterator.TextAttribute("GVT_FONTS");
      public static final GVTAttributedCharacterIterator.TextAttribute GVT_FONT = new GVTAttributedCharacterIterator.TextAttribute("GVT_FONT");
      public static final GVTAttributedCharacterIterator.TextAttribute ALT_GLYPH_HANDLER = new GVTAttributedCharacterIterator.TextAttribute("ALT_GLYPH_HANDLER");
      public static final GVTAttributedCharacterIterator.TextAttribute BIDI_LEVEL = new GVTAttributedCharacterIterator.TextAttribute("BIDI_LEVEL");
      public static final GVTAttributedCharacterIterator.TextAttribute CHAR_INDEX = new GVTAttributedCharacterIterator.TextAttribute("CHAR_INDEX");
      public static final GVTAttributedCharacterIterator.TextAttribute ARABIC_FORM = new GVTAttributedCharacterIterator.TextAttribute("ARABIC_FORM");
      public static final Integer WRITING_MODE_LTR = new Integer(1);
      public static final Integer WRITING_MODE_RTL = new Integer(2);
      public static final Integer WRITING_MODE_TTB = new Integer(3);
      public static final Integer ORIENTATION_ANGLE = new Integer(1);
      public static final Integer ORIENTATION_AUTO = new Integer(2);
      public static final Integer SMALL_CAPS = new Integer(16);
      public static final Integer UNDERLINE_ON;
      public static final Boolean OVERLINE_ON;
      public static final Boolean STRIKETHROUGH_ON;
      public static final Integer ADJUST_SPACING;
      public static final Integer ADJUST_ALL;
      public static final Integer ARABIC_NONE;
      public static final Integer ARABIC_ISOLATED;
      public static final Integer ARABIC_TERMINAL;
      public static final Integer ARABIC_INITIAL;
      public static final Integer ARABIC_MEDIAL;

      public TextAttribute(String var1) {
         super(var1);
      }

      static {
         UNDERLINE_ON = java.awt.font.TextAttribute.UNDERLINE_ON;
         OVERLINE_ON = Boolean.TRUE;
         STRIKETHROUGH_ON = java.awt.font.TextAttribute.STRIKETHROUGH_ON;
         ADJUST_SPACING = new Integer(0);
         ADJUST_ALL = new Integer(1);
         ARABIC_NONE = new Integer(0);
         ARABIC_ISOLATED = new Integer(1);
         ARABIC_TERMINAL = new Integer(2);
         ARABIC_INITIAL = new Integer(3);
         ARABIC_MEDIAL = new Integer(4);
      }
   }
}
