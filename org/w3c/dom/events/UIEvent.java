package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;

public interface UIEvent extends Event {
   AbstractView getView();

   int getDetail();

   void initUIEvent(String var1, boolean var2, boolean var3, AbstractView var4, int var5);

   void initUIEventNS(String var1, String var2, boolean var3, boolean var4, AbstractView var5, int var6);
}
