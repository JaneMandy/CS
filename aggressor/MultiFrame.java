package aggressor;

import common.CommonUtils;
import dialog.DialogUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class MultiFrame extends JFrame implements KeyEventDispatcher {
   protected JToolBar toolbar;
   protected JPanel content;
   protected CardLayout cards;
   protected final LinkedList buttons;
   protected AggressorClient active;

   public Collection getOtherScriptEngines(AggressorClient var1) {
      Collection var2 = this.getScriptEngines();
      var2.remove(var1.getScriptEngine());
      return var2;
   }

   public Collection getScriptEngines() {
      synchronized(this.buttons) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = this.buttons.iterator();

         while(var3.hasNext()) {
            MultiFrame._A var4 = (MultiFrame._A)var3.next();
            var2.add(var4.C.getScriptEngine());
         }

         return var2;
      }
   }

   public Map getClients() {
      synchronized(this.buttons) {
         HashMap var2 = new HashMap();
         Iterator var3 = this.buttons.iterator();

         while(var3.hasNext()) {
            MultiFrame._A var4 = (MultiFrame._A)var3.next();
            var2.put(var4.A.getText(), var4.C);
         }

         return var2;
      }
   }

   public String getActiveAlias() {
      return this.active.getTeamServerAlias();
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      return this.active != null ? this.active.getBindings().dispatchKeyEvent(var1) : false;
   }

   public void closeConnect() {
      synchronized(this.buttons) {
         if (this.buttons.size() == 0) {
            System.exit(0);
         }

      }
   }

   public void quit(Map var1) {
      CommonUtils.Guard();
      synchronized(this.buttons) {
         Iterator var3 = this.buttons.iterator();

         MultiFrame._A var4;
         Map var5;
         do {
            if (!var3.hasNext()) {
               if (this.buttons.size() == 0) {
                  System.exit(0);
               }

               return;
            }

            var4 = (MultiFrame._A)var3.next();
            var5 = var4.C.data.getMapSafe("options");
         } while(!var5.equals(var1));

         this.quit(var4.C, false);
      }
   }

   public void quit(AggressorClient var1, boolean var2) {
      CommonUtils.Guard();
      synchronized(this.buttons) {
         MultiFrame._A var4 = null;
         this.content.remove(var1);
         Iterator var5 = this.buttons.iterator();

         while(var5.hasNext()) {
            var4 = (MultiFrame._A)var5.next();
            if (var4.C == var1) {
               this.toolbar.remove(var4.A);
               var5.remove();
               this.toolbar.validate();
               this.toolbar.repaint();
               break;
            }
         }

         if (this.buttons.size() == 0 && !var2) {
            System.exit(0);
         }

         if (this.buttons.size() == 0) {
            return;
         }

         if (this.buttons.size() == 1) {
            this.getContentPane().remove(this.toolbar);
            this.validate();
         }

         if (var5.hasNext()) {
            var4 = (MultiFrame._A)var5.next();
         } else {
            var4 = (MultiFrame._A)this.buttons.getFirst();
         }

         this.set(var4.A);
      }

      System.gc();
   }

   protected MultiFrame() {
      super("");
      this.getContentPane().setLayout(new BorderLayout());
      this.toolbar = new JToolBar();
      this.content = new JPanel();
      this.cards = new CardLayout();
      this.content.setLayout(this.cards);
      this.getContentPane().add(this.content, "Center");
      this.buttons = new LinkedList();
      this.setDefaultCloseOperation(3);
      this.setSize(800, 600);
      this.setExtendedState(6);
      this.setIconImage(DialogUtils.getImage("resources/armitage-icon.gif"));
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
   }

   protected void set(JToggleButton var1) {
      this.set(var1, var1.getText());
   }

   protected void set(JToggleButton var1, String var2) {
      CommonUtils.Guard();
      synchronized(this.buttons) {
         String var4 = var2 == null ? var1.getText() : var2;
         Iterator var5 = this.buttons.iterator();

         while(var5.hasNext()) {
            MultiFrame._A var6 = (MultiFrame._A)var5.next();
            if (var6.A.getText().equals(var4)) {
               var6.A.setSelected(true);
               var6.A.setFont(var6.A.getFont().deriveFont(1));
               var6.A.setForeground(Prefs.getPreferences().getColor("connection.active.color", "#0000ff"));
               this.active = var6.C;
               this.setTitle(this.active.getTitle());
            } else {
               var6.A.setSelected(false);
               var6.A.setFont(var6.A.getFont().deriveFont(0));
               var6.A.setForeground(Color.BLACK);
            }
         }

         this.cards.show(this.content, var4);
         this.active.touch();
      }
   }

   public boolean checkCollision(String var1) {
      synchronized(this.buttons) {
         Iterator var3 = this.buttons.iterator();

         MultiFrame._A var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (MultiFrame._A)var3.next();
         } while(!var1.equals(var4.A.getText()));

         return true;
      }
   }

   public boolean isConnected(String var1) {
      synchronized(this.buttons) {
         Iterator var3 = this.buttons.iterator();

         String var5;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            MultiFrame._A var4 = (MultiFrame._A)var3.next();
            var5 = DataUtils.getTeamServerIP(var4.C.data);
         } while(!var1.equals(var5));

         return true;
      }
   }

   public void addButton(String var1, final AggressorClient var2, String var3) {
      CommonUtils.Guard();
      if (this.checkCollision(var1)) {
         String var4 = var1.equals(var3) ? var3 + " (2)" : var3;
         this.addButton(var1 + " (2)", var2, var4);
      } else {
         synchronized(this.buttons) {
            final MultiFrame._A var5 = new MultiFrame._A();
            var5.A = new JToggleButton(var1);
            var5.A.setToolTipText(var1);
            var5.C = var2;
            var2.setTeamServerAlias(var1);
            var5.A.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent var1) {
                  MultiFrame.this.set((JToggleButton)var1.getSource());
               }
            });
            var5.A.addMouseListener(new MouseAdapter() {
               public void A(MouseEvent var1) {
                  if (var1.isPopupTrigger()) {
                     final JToggleButton var2x = var5.A;
                     JPopupMenu var3 = new JPopupMenu();
                     JMenuItem var4 = new JMenuItem("Rename");
                     var4.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent var1) {
                           String var2xx = JOptionPane.showInputDialog("Rename to?", var2x.getText());
                           if (var2xx != null && !var2x.getText().equals(var2xx)) {
                              if (CommonUtils.isNullOrEmpty(var2xx)) {
                                 DialogUtils.showError("Name can not be empty.");
                              } else if (MultiFrame.this.checkCollision(var2xx)) {
                                 DialogUtils.showError("Name already in use.");
                              } else if ('*' == var2xx.charAt(0)) {
                                 DialogUtils.showError("Name can not start with *.");
                              } else {
                                 MultiFrame.this.content.remove(var2);
                                 MultiFrame.this.content.add(var2, var2xx);
                                 var2.setTeamServerAlias(var2xx);
                                 var2x.setText(var2xx);
                                 var2x.setToolTipText(var2xx);
                                 MultiFrame.this.set(var2x);
                              }

                           }
                        }
                     });
                     var3.add(var4);
                     JMenuItem var5x = new JMenuItem("Disconnect");
                     var5x.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent var1) {
                           var5.C.kill();
                        }
                     });
                     var3.add(var5x);
                     var3.show((JComponent)var1.getSource(), var1.getX(), var1.getY());
                     var1.consume();
                  }

               }

               public void mouseClicked(MouseEvent var1) {
                  this.A(var1);
               }

               public void mousePressed(MouseEvent var1) {
                  this.A(var1);
               }

               public void mouseReleased(MouseEvent var1) {
                  this.A(var1);
               }
            });
            this.toolbar.add(var5.A);
            this.content.add(var2, var1);
            this.buttons.add(var5);
            this.set(var5.A, var3);
            if (this.buttons.size() == 1) {
               this.setVisible(true);
            } else if (this.buttons.size() == 2) {
               this.getContentPane().add(this.toolbar, "South");
            }

            this.validate();
         }
      }
   }

   private static class _A {
      public AggressorClient C;
      public JToggleButton A;
      public boolean B;

      private _A() {
         this.B = false;
      }

      // $FF: synthetic method
      _A(Object var1) {
         this();
      }
   }
}
