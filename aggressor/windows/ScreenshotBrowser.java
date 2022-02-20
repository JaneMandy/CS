package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.TranscriptEditor;
import common.AObject;
import common.AdjustData;
import common.Helper;
import common.Screenshot;
import common.ScriptUtils;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import ui.ATextField;
import ui.DataBrowser;
import ui.DataSelectionListener;
import ui.TablePopup;
import ui.ZoomableImage;

public class ScreenshotBrowser extends AObject implements AdjustData, DataSelectionListener, TablePopup {
   protected AggressorClient client = null;
   protected DataBrowser browser = null;
   protected ZoomableImage viewer = null;
   protected JTextField title = null;
   protected TranscriptEditor editor = new TranscriptEditor();

   public ScreenshotBrowser(AggressorClient var1) {
      this.client = var1;
      var1.getData().populateAndSubscribe("accents", this);
      Helper var2 = new Helper();
      if (!var2.startHelper(this.getClass())) {
         System.exit(0);
      }

   }

   public ActionListener cleanup() {
      return this.client.getData().unsubOnClose("screenshots, accents", this);
   }

   public JComponent getContent() {
      LinkedList var1 = this.client.getData().populateAndSubscribe("screenshots", this);
      this.title = new ATextField("", 20);
      this.title.setEditable(false);
      this.viewer = new ZoomableImage();
      JPanel var2 = new JPanel();
      var2.setLayout(new BorderLayout());
      var2.add(DialogUtils.pad(this.title, 3, 3, 3, 3), "North");
      var2.add(new JScrollPane(this.viewer), "Center");
      this.browser = DataBrowser.getScreenshotDataBrowser("object", var2, var1);
      this.browser.addDataSelectionListener(this);
      this.browser.getTable().setPopupMenu(this);
      DialogUtils.setupDateRenderer(this.browser.getTable(), "when");
      this.editor.setTable(this.browser.getTable(), this.browser.getModel());
      return this.browser;
   }

   public void selected(Object var1) {
      if (var1 != null) {
         this.title.setText(((Screenshot)var1).getWindowTitle());
         this.viewer.setIcon(((Screenshot)var1).getImage());
      } else {
         this.title.setText("");
         this.viewer.setIcon((Icon)null);
      }

   }

   public Map format(String var1, Object var2) {
      if (this.editor.processTranscriptFormat(var1, var2)) {
         return null;
      } else {
         Screenshot var3 = (Screenshot)var2;
         if (this.editor.isRemoved(var3)) {
            return null;
         } else {
            Map var4 = var3.toMap();
            var4.put("object", var3);
            return this.editor.decorate(var3, var4);
         }
      }
   }

   public void result(String var1, Object var2) {
      if (!this.editor.processTranscriptResult(var1, var2)) {
         if (this.browser != null) {
            Map var3 = this.format(var1, var2);
            if (var3 != null) {
               this.browser.addEntry(var3);
            }

         }
      }
   }

   public void showPopup(MouseEvent var1) {
      Map var2 = this.browser.getSelectedRow();
      if (var2 != null) {
         Stack var3 = new Stack();
         var3.push(ScriptUtils.convertAll(var2));
         this.client.getScriptEngine().getMenuBuilder().installMenu(var1, "screenshots", var3);
      }

   }
}
