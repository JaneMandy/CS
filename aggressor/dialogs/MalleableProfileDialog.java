package aggressor.dialogs;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import dialog.DialogUtils;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MalleableProfileDialog {
   protected AggressorClient client;

   public MalleableProfileDialog(AggressorClient var1) {
      this.client = var1;
   }

   public void show() {
      JFrame var1 = DialogUtils.dialog("Malleable C2 Profile", 640, 480);
      JTextArea var2 = new JTextArea();
      var2.setTabSize(2);
      var2.append(DataUtils.getProfile(this.client.getData()).getString(".malleableC2_profile"));
      var2.setCaretPosition(0);
      var2.setEditable(false);
      var2.setBackground(new Color(221, 221, 221));
      var1.add(DialogUtils.description("Malleable C2 Profile for TeamServer: " + this.client.getTeamServerAlias(), "text/plain"), "North");
      var1.add(new JScrollPane(var2), "Center");
      var1.setVisible(true);
   }
}
