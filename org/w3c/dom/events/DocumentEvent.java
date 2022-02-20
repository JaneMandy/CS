package org.w3c.dom.events;

import org.w3c.dom.DOMException;

public interface DocumentEvent {
   Event createEvent(String var1) throws DOMException;

   boolean canDispatch(String var1, String var2);
}
