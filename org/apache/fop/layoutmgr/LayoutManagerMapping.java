package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.AreaTreeHandler;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FOText;
import org.apache.fop.fo.FObjMixed;
import org.apache.fop.fo.extensions.ExternalDocument;
import org.apache.fop.fo.flow.BasicLink;
import org.apache.fop.fo.flow.BidiOverride;
import org.apache.fop.fo.flow.Block;
import org.apache.fop.fo.flow.BlockContainer;
import org.apache.fop.fo.flow.Character;
import org.apache.fop.fo.flow.ExternalGraphic;
import org.apache.fop.fo.flow.Footnote;
import org.apache.fop.fo.flow.Inline;
import org.apache.fop.fo.flow.InlineContainer;
import org.apache.fop.fo.flow.InlineLevel;
import org.apache.fop.fo.flow.InstreamForeignObject;
import org.apache.fop.fo.flow.Leader;
import org.apache.fop.fo.flow.ListBlock;
import org.apache.fop.fo.flow.ListItem;
import org.apache.fop.fo.flow.PageNumber;
import org.apache.fop.fo.flow.PageNumberCitation;
import org.apache.fop.fo.flow.PageNumberCitationLast;
import org.apache.fop.fo.flow.RetrieveMarker;
import org.apache.fop.fo.flow.RetrieveTableMarker;
import org.apache.fop.fo.flow.Wrapper;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableBody;
import org.apache.fop.fo.flow.table.TableCell;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TableFooter;
import org.apache.fop.fo.flow.table.TableHeader;
import org.apache.fop.fo.flow.table.TableRow;
import org.apache.fop.fo.pagination.Flow;
import org.apache.fop.fo.pagination.PageSequence;
import org.apache.fop.fo.pagination.SideRegion;
import org.apache.fop.fo.pagination.StaticContent;
import org.apache.fop.fo.pagination.Title;
import org.apache.fop.layoutmgr.inline.BasicLinkLayoutManager;
import org.apache.fop.layoutmgr.inline.BidiLayoutManager;
import org.apache.fop.layoutmgr.inline.CharacterLayoutManager;
import org.apache.fop.layoutmgr.inline.ContentLayoutManager;
import org.apache.fop.layoutmgr.inline.ExternalGraphicLayoutManager;
import org.apache.fop.layoutmgr.inline.FootnoteLayoutManager;
import org.apache.fop.layoutmgr.inline.ICLayoutManager;
import org.apache.fop.layoutmgr.inline.InlineLayoutManager;
import org.apache.fop.layoutmgr.inline.InlineLevelLayoutManager;
import org.apache.fop.layoutmgr.inline.InstreamForeignObjectLM;
import org.apache.fop.layoutmgr.inline.LeaderLayoutManager;
import org.apache.fop.layoutmgr.inline.PageNumberCitationLastLayoutManager;
import org.apache.fop.layoutmgr.inline.PageNumberCitationLayoutManager;
import org.apache.fop.layoutmgr.inline.PageNumberLayoutManager;
import org.apache.fop.layoutmgr.inline.TextLayoutManager;
import org.apache.fop.layoutmgr.inline.WrapperLayoutManager;
import org.apache.fop.layoutmgr.list.ListBlockLayoutManager;
import org.apache.fop.layoutmgr.list.ListItemLayoutManager;
import org.apache.fop.layoutmgr.table.TableLayoutManager;

public class LayoutManagerMapping implements LayoutManagerMaker {
   protected static Log log;
   private Map makers = new HashMap();

   public LayoutManagerMapping() {
      this.initialize();
   }

   protected void initialize() {
      this.registerMaker(FOText.class, new LayoutManagerMapping.FOTextLayoutManagerMaker());
      this.registerMaker(FObjMixed.class, new LayoutManagerMapping.Maker());
      this.registerMaker(BidiOverride.class, new LayoutManagerMapping.BidiOverrideLayoutManagerMaker());
      this.registerMaker(Inline.class, new LayoutManagerMapping.InlineLayoutManagerMaker());
      this.registerMaker(Footnote.class, new LayoutManagerMapping.FootnodeLayoutManagerMaker());
      this.registerMaker(InlineContainer.class, new LayoutManagerMapping.InlineContainerLayoutManagerMaker());
      this.registerMaker(BasicLink.class, new LayoutManagerMapping.BasicLinkLayoutManagerMaker());
      this.registerMaker(Block.class, new LayoutManagerMapping.BlockLayoutManagerMaker());
      this.registerMaker(Leader.class, new LayoutManagerMapping.LeaderLayoutManagerMaker());
      this.registerMaker(RetrieveMarker.class, new LayoutManagerMapping.RetrieveMarkerLayoutManagerMaker());
      this.registerMaker(RetrieveTableMarker.class, new LayoutManagerMapping.Maker());
      this.registerMaker(Character.class, new LayoutManagerMapping.CharacterLayoutManagerMaker());
      this.registerMaker(ExternalGraphic.class, new LayoutManagerMapping.ExternalGraphicLayoutManagerMaker());
      this.registerMaker(BlockContainer.class, new LayoutManagerMapping.BlockContainerLayoutManagerMaker());
      this.registerMaker(ListItem.class, new LayoutManagerMapping.ListItemLayoutManagerMaker());
      this.registerMaker(ListBlock.class, new LayoutManagerMapping.ListBlockLayoutManagerMaker());
      this.registerMaker(InstreamForeignObject.class, new LayoutManagerMapping.InstreamForeignObjectLayoutManagerMaker());
      this.registerMaker(PageNumber.class, new LayoutManagerMapping.PageNumberLayoutManagerMaker());
      this.registerMaker(PageNumberCitation.class, new LayoutManagerMapping.PageNumberCitationLayoutManagerMaker());
      this.registerMaker(PageNumberCitationLast.class, new LayoutManagerMapping.PageNumberCitationLastLayoutManagerMaker());
      this.registerMaker(Table.class, new LayoutManagerMapping.TableLayoutManagerMaker());
      this.registerMaker(TableBody.class, new LayoutManagerMapping.Maker());
      this.registerMaker(TableColumn.class, new LayoutManagerMapping.Maker());
      this.registerMaker(TableRow.class, new LayoutManagerMapping.Maker());
      this.registerMaker(TableCell.class, new LayoutManagerMapping.Maker());
      this.registerMaker(TableFooter.class, new LayoutManagerMapping.Maker());
      this.registerMaker(TableHeader.class, new LayoutManagerMapping.Maker());
      this.registerMaker(Wrapper.class, new LayoutManagerMapping.WrapperLayoutManagerMaker());
      this.registerMaker(Title.class, new LayoutManagerMapping.InlineLayoutManagerMaker());
   }

   protected void registerMaker(Class clazz, LayoutManagerMapping.Maker maker) {
      this.makers.put(clazz, maker);
   }

   public void makeLayoutManagers(FONode node, List lms) {
      LayoutManagerMapping.Maker maker = (LayoutManagerMapping.Maker)this.makers.get(node.getClass());
      if (maker == null) {
         if ("http://www.w3.org/1999/XSL/Format".equals(node.getNamespaceURI())) {
            log.error("No LayoutManager maker for class " + node.getClass());
         } else if (log.isDebugEnabled()) {
            log.debug("Skipping the creation of a layout manager for " + node.getClass());
         }
      } else {
         maker.make(node, lms);
      }

   }

   public LayoutManager makeLayoutManager(FONode node) {
      List lms = new ArrayList();
      this.makeLayoutManagers(node, lms);
      if (lms.size() == 0) {
         throw new IllegalStateException("LayoutManager for class " + node.getClass() + " is missing.");
      } else if (lms.size() > 1) {
         throw new IllegalStateException("Duplicate LayoutManagers for class " + node.getClass() + " found, only one may be declared.");
      } else {
         return (LayoutManager)lms.get(0);
      }
   }

   public PageSequenceLayoutManager makePageSequenceLayoutManager(AreaTreeHandler ath, PageSequence ps) {
      return new PageSequenceLayoutManager(ath, ps);
   }

   public FlowLayoutManager makeFlowLayoutManager(PageSequenceLayoutManager pslm, Flow flow) {
      return new FlowLayoutManager(pslm, flow);
   }

   public ContentLayoutManager makeContentLayoutManager(PageSequenceLayoutManager pslm, Title title) {
      return new ContentLayoutManager(pslm, title);
   }

   public StaticContentLayoutManager makeStaticContentLayoutManager(PageSequenceLayoutManager pslm, StaticContent sc, SideRegion reg) {
      return new StaticContentLayoutManager(pslm, sc, reg);
   }

   public StaticContentLayoutManager makeStaticContentLayoutManager(PageSequenceLayoutManager pslm, StaticContent sc, org.apache.fop.area.Block block) {
      return new StaticContentLayoutManager(pslm, sc, block);
   }

   public ExternalDocumentLayoutManager makeExternalDocumentLayoutManager(AreaTreeHandler ath, ExternalDocument ed) {
      return new ExternalDocumentLayoutManager(ath, ed);
   }

   static {
      log = LogFactory.getLog(LayoutManagerMapping.class);
   }

   public class WrapperLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new WrapperLayoutManager((Wrapper)node));
         Iterator baseIter = node.getChildNodes();
         if (baseIter != null) {
            while(baseIter.hasNext()) {
               FONode child = (FONode)baseIter.next();
               LayoutManagerMapping.this.makeLayoutManagers(child, lms);
            }

         }
      }
   }

   public class RetrieveMarkerLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         Iterator baseIter = node.getChildNodes();
         if (baseIter != null) {
            while(baseIter.hasNext()) {
               FONode child = (FONode)baseIter.next();
               LayoutManagerMapping.this.makeLayoutManagers(child, lms);
            }

         }
      }
   }

   public static class TableLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         Table table = (Table)node;
         TableLayoutManager tlm = new TableLayoutManager(table);
         lms.add(tlm);
      }
   }

   public static class PageNumberCitationLastLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new PageNumberCitationLastLayoutManager((PageNumberCitationLast)node));
      }
   }

   public static class PageNumberCitationLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new PageNumberCitationLayoutManager((PageNumberCitation)node));
      }
   }

   public static class PageNumberLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new PageNumberLayoutManager((PageNumber)node));
      }
   }

   public static class InstreamForeignObjectLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new InstreamForeignObjectLM((InstreamForeignObject)node));
      }
   }

   public static class ListBlockLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new ListBlockLayoutManager((ListBlock)node));
      }
   }

   public static class ListItemLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new ListItemLayoutManager((ListItem)node));
      }
   }

   public static class BlockContainerLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new BlockContainerLayoutManager((BlockContainer)node));
      }
   }

   public static class ExternalGraphicLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         ExternalGraphic eg = (ExternalGraphic)node;
         if (!eg.getSrc().equals("")) {
            lms.add(new ExternalGraphicLayoutManager(eg));
         }

      }
   }

   public static class CharacterLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         Character foCharacter = (Character)node;
         if (foCharacter.getCharacter() != 0) {
            lms.add(new CharacterLayoutManager(foCharacter));
         }

      }
   }

   public static class LeaderLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new LeaderLayoutManager((Leader)node));
      }
   }

   public static class BlockLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new BlockLayoutManager((Block)node));
      }
   }

   public static class BasicLinkLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new BasicLinkLayoutManager((BasicLink)node));
      }
   }

   public static class InlineContainerLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         ArrayList childList = new ArrayList();
         super.make(node, childList);
         lms.add(new ICLayoutManager((InlineContainer)node, childList));
      }
   }

   public static class FootnodeLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new FootnoteLayoutManager((Footnote)node));
      }
   }

   public static class InlineLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         lms.add(new InlineLayoutManager((InlineLevel)node));
      }
   }

   public static class BidiOverrideLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(BidiOverride node, List lms) {
         ArrayList childList = new ArrayList();
         super.make(node, childList);

         for(int count = childList.size() - 1; count >= 0; --count) {
            LayoutManager lm = (LayoutManager)childList.get(count);
            if (lm instanceof InlineLevelLayoutManager) {
               LayoutManager blm = new BidiLayoutManager(node, (InlineLayoutManager)lm);
               lms.add(blm);
            } else {
               lms.add(lm);
            }
         }

      }
   }

   public static class FOTextLayoutManagerMaker extends LayoutManagerMapping.Maker {
      public void make(FONode node, List lms) {
         FOText foText = (FOText)node;
         if (foText.length() > 0) {
            lms.add(new TextLayoutManager(foText));
         }

      }
   }

   public static class Maker {
      public void make(FONode node, List lms) {
      }
   }
}
