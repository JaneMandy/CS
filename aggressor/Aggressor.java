package aggressor;

import aggressor.dialogs.ConnectDialog;
import aggressor.ui.UseSynthetica;
import common.Authorization;
import common.License;
import common.Requirements;
import common.Starter;
import sleep.parser.ParserConfig;

public class Aggressor extends Starter {
   public static final String VERSION = "4.4 (20210801) " + (License.isTrial() ? "Trial" : "Licensed");
   public static final String VERSION_SHORT = "4.4";
   private static MultiFrame B = null;

   public static MultiFrame getFrame() {
      return B;
   }

   public static void main(String[] var0) {
      Aggressor var1 = new Aggressor();
      var1.A(var0);
   }

   private final void A(String[] var1) {
      ParserConfig.installEscapeConstant('c', "\u0003");
      ParserConfig.installEscapeConstant('U', "\u001f");
      ParserConfig.installEscapeConstant('o', "\u000f");
      (new UseSynthetica()).setup();
      Requirements.checkGUI();
      License.checkLicenseGUI(new Authorization());
      B = new MultiFrame();
      super.initializeStarter(this.getClass());
      (new ConnectDialog(B)).show();
   }
}
