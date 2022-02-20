package aggressor.ui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class UseNimbus extends UseLookAndFeel {
   public void setup() {
      try {
         LookAndFeelInfo[] var1 = UIManager.getInstalledLookAndFeels();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            LookAndFeelInfo var4 = var1[var3];
            if ("Nimbus".equals(var4.getName())) {
               UIManager.setLookAndFeel(var4.getClassName());
               break;
            }
         }
      } catch (Exception var5) {
      }

   }
}
