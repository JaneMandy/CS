package console;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Properties;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Colors {
   public static final char bold = '\u0002';
   public static final char underline = '\u001f';
   public static final char color = '\u0003';
   public static final char cancel = '\u000f';
   public static final char reverse = '\u0016';
   protected boolean showcolors = true;
   protected Color[] colorTable = new Color[16];
   private StyledDocument A = new DefaultStyledDocument();

   public static String color(String var0, String var1) {
      return "\u0003" + var1 + var0;
   }

   public static String underline(String var0) {
      return "\u001f" + var0 + "\u000f";
   }

   public Colors(Properties var1) {
      this.colorTable[0] = Color.white;
      this.colorTable[1] = new Color(0, 0, 0);
      this.colorTable[2] = Color.decode("#3465A4");
      this.colorTable[3] = Color.decode("#4E9A06");
      this.colorTable[4] = Color.decode("#EF2929");
      this.colorTable[5] = Color.decode("#CC0000");
      this.colorTable[6] = Color.decode("#75507B");
      this.colorTable[7] = Color.decode("#C4A000");
      this.colorTable[8] = Color.decode("#FCE94F");
      this.colorTable[9] = Color.decode("#8AE234");
      this.colorTable[10] = Color.decode("#06989A");
      this.colorTable[11] = Color.decode("#34E2E2");
      this.colorTable[12] = Color.decode("#729FCF");
      this.colorTable[13] = Color.decode("#AD7FA8");
      this.colorTable[14] = Color.decode("#808080");
      this.colorTable[15] = Color.lightGray;

      for(int var2 = 0; var2 < 16; ++var2) {
         String var3 = var1.getProperty("console.color_" + var2 + ".color", (String)null);
         if (var3 != null) {
            this.colorTable[var2] = Color.decode(var3);
         }
      }

      this.showcolors = "true".equals(var1.getProperty("console.show_colors.boolean", "true"));
   }

   public String strip(String var1) {
      Colors._A var2 = this.A(var1);
      return this.A(var2);
   }

   private String A(Colors._A var1) {
      StringBuffer var2;
      for(var2 = new StringBuffer(128); var1 != null; var1 = var1.B) {
         var2.append(var1.C);
      }

      return var2.toString();
   }

   private void A(StyledDocument var1, Colors._A var2) {
      for(; var2 != null; var2 = var2.B) {
         try {
            if (var2.C.length() > 0) {
               var1.insertString(var1.getLength(), var2.C.toString(), var2.A);
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   public void append(StyledDocument var1, String var2) {
      if (var2.length() > 262144) {
         var2 = var2.substring(var2.length() - 262144, var2.length());
      }

      Colors._A var3 = this.A(var2);
      this.A(var1, var3);
      if (var1.getLength() > 262144) {
         try {
            var1.remove(0, var1.getLength() - 262144 + 131072);
         } catch (BadLocationException var5) {
         }
      }

   }

   public void append(JTextPane var1, String var2) {
      StyledDocument var3 = var1.getStyledDocument();
      if (this.showcolors) {
         var1.setDocument(this.A);
         this.append(var3, var2);
         var1.setDocument(var3);
      } else {
         Colors._A var4 = this.A(var2);
         this.A(var3, this.A(this.A(var4)));
      }

   }

   public void set(JTextPane var1, String var2) {
      Colors._A var3 = this.A(var2);
      if (!this.A(var3).equals(var1.getText())) {
         DefaultStyledDocument var4 = new DefaultStyledDocument();
         if (this.showcolors) {
            this.A(var4, var3);
         } else {
            this.A(var4, this.A(this.A(var3)));
         }

         var1.setDocument(var4);
         var1.setSize(new Dimension(1000, var1.getSize().height));
      }

   }

   public void setNoHack(JTextPane var1, String var2) {
      Colors._A var3 = this.A(var2);
      if (!this.A(var3).equals(var1.getText())) {
         DefaultStyledDocument var4 = new DefaultStyledDocument();
         if (this.showcolors) {
            this.A(var4, var3);
         } else {
            this.A(var4, this.A(this.A(var3)));
         }

         var1.setDocument(var4);
      }

   }

   private Colors._A A(String var1) {
      Colors._A var2 = new Colors._A();
      if (var1 == null) {
         return var2;
      } else {
         char[] var4 = var1.toCharArray();

         for(int var7 = 0; var7 < var4.length; ++var7) {
            switch(var4[var7]) {
            case '\u0002':
               var2.A();
               StyleConstants.setBold(var2.B.A, !StyleConstants.isBold(var2.A));
               var2 = var2.B;
               break;
            case '\u0003':
               var2.A();
               if (var7 + 1 < var4.length && (var4[var7 + 1] >= '0' && var4[var7 + 1] <= '9' || var4[var7 + 1] >= 'A' && var4[var7 + 1] <= 'F')) {
                  int var8 = Integer.parseInt(var4[var7 + 1].makeConcatWithConstants<invokedynamic>(var4[var7 + 1]), 16);
                  StyleConstants.setForeground(var2.B.A, this.colorTable[var8]);
                  ++var7;
               }

               var2 = var2.B;
               break;
            case '\n':
               var2.A();
               var2 = var2.B;
               var2.A = new SimpleAttributeSet();
               var2.C.append(var4[var7]);
               break;
            case '\u000f':
               var2.A();
               var2 = var2.B;
               var2.A = new SimpleAttributeSet();
               break;
            case '\u001f':
               var2.A();
               StyleConstants.setUnderline(var2.B.A, !StyleConstants.isUnderline(var2.A));
               var2 = var2.B;
               break;
            default:
               var2.C.append(var4[var7]);
            }
         }

         return var2;
      }
   }

   private static final class _A {
      protected SimpleAttributeSet A;
      protected StringBuffer C;
      protected Colors._A B;

      private _A() {
         this.A = new SimpleAttributeSet();
         this.C = new StringBuffer(32);
         this.B = null;
      }

      public void A() {
         this.B = new Colors._A();
         this.B.A = (SimpleAttributeSet)this.A.clone();
      }

      _A(Object var1) {
         this();
      }
   }
}
