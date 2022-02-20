package dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class SolidIcon implements Icon {
   private int C;
   private int A;
   private Color B;

   public SolidIcon(Color var1, int var2, int var3) {
      this.C = var2;
      this.A = var3;
      this.B = var1;
   }

   public Color getColor() {
      return this.B;
   }

   public void setColor(Color var1) {
      this.B = var1;
   }

   public int getIconWidth() {
      return this.C;
   }

   public int getIconHeight() {
      return this.A;
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      var2.setColor(this.B);
      var2.fillRect(var3, var4, this.C - 1, this.A - 1);
   }
}
