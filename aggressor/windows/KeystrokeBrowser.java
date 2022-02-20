package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.TranscriptEditor;
import common.AObject;
import common.AdjustData;
import common.CommonUtils;
import common.Helper;
import common.Keystrokes;
import common.ScriptUtils;
import console.Colors;
import console.Display;
import dialog.DialogUtils;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import ui.DataBrowser;
import ui.DataSelectionListener;
import ui.TablePopup;

public class KeystrokeBrowser extends AObject implements AdjustData, DataSelectionListener, TablePopup {
   protected AggressorClient client = null;
   protected DataBrowser browser = null;
   protected Display content = null;
   protected Map sessions = new HashMap();
   protected Colors colors = new Colors(new Properties());
   protected TranscriptEditor editor = new TranscriptEditor();

   public KeystrokeBrowser(AggressorClient var1) {
      this.client = var1;
      var1.getData().populateAndSubscribe("accents", this);
      Helper var2 = new Helper();
      if (!var2.startHelper(this.getClass())) {
         System.exit(0);
      }

   }

   public ActionListener cleanup() {
      return this.client.getData().unsubOnClose("keystrokes, accents", this);
   }

   public JComponent getContent() {
      this.client.getData().populateAndSubscribe("keystrokes", this);
      LinkedList var1 = new LinkedList(this.sessions.values());
      this.content = new Display(new Properties());
      this.browser = DataBrowser.getKeystrokeDataBrowser("document", this.content, var1);
      this.browser.addDataSelectionListener(this);
      this.browser.getTable().setPopupMenu(this);
      DialogUtils.setupDateRenderer(this.browser.getTable(), "when");
      this.editor.setTable(this.browser.getTable(), this.browser.getModel());
      return this.browser;
   }

   public void selected(Object var1) {
      if (var1 != null) {
         StyledDocument var2 = (StyledDocument)var1;
         this.content.swap(var2);
         this.content.getConsole().setCaretPosition(var2.getLength());
      } else {
         this.content.clear();
      }

   }

   public Map format(String var1, Object var2) {
      if (this.editor.processTranscriptResult(var1, var2)) {
         return null;
      } else {
         final Keystrokes var3 = (Keystrokes)var2;
         if (this.editor.isRemoved(var3)) {
            this.sessions.remove(var3.ID());
            return null;
         } else {
            Map var4;
            if (!this.sessions.containsKey(var3.ID())) {
               var4 = var3.toMap();
               var4.put("document", new DefaultStyledDocument());
               this.sessions.put(var3.ID(), var4);
            }

            var4 = (Map)this.sessions.get(var3.ID());
            final StyledDocument var5 = (StyledDocument)var4.get("document");
            CommonUtils.runSafe(new Runnable() {
               public void run() {
                  KeystrokeBrowser.this.colors.append(var5, var3.getKeystrokes());
                  if (KeystrokeBrowser.this.content != null && var5 == KeystrokeBrowser.this.content.getConsole().getDocument()) {
                     KeystrokeBrowser.this.content.getConsole().scrollRectToVisible(new Rectangle(0, KeystrokeBrowser.this.content.getConsole().getHeight() + 1, 1, 1));
                  }

               }
            });
            var4.putAll(var3.toMap());
            return this.editor.decorate((Keystrokes)var2, var4);
         }
      }
   }

   public void result(String var1, Object var2) {
      if (!this.editor.processTranscriptResult(var1, var2)) {
         this.format(var1, var2);
         if (this.browser != null) {
            this.browser.setTable(this.sessions.values());
         }
      }

   }

   public void showPopup(MouseEvent var1) {
      Map var2 = this.browser.getSelectedRow();
      if (var2 != null) {
         Stack var3 = new Stack();
         var3.push(ScriptUtils.convertAll(var2));
         this.client.getScriptEngine().getMenuBuilder().installMenu(var1, "keystrokes", var3);
      }

   }
}
