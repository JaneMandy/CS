package org.apache.batik.dom.svg;

import java.net.URL;
import org.apache.batik.css.dom.CSSOMSVGViewCSS;
import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.SVGCSSEngine;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.ExtensibleDOMImplementation;
import org.apache.batik.dom.GenericDocumentType;
import org.apache.batik.dom.events.DOMTimeEvent;
import org.apache.batik.dom.events.DocumentEventSupport;
import org.apache.batik.dom.util.CSSStyleDeclarationFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.util.ParsedURL;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.events.Event;
import org.w3c.dom.stylesheets.StyleSheet;

public class SVGDOMImplementation extends ExtensibleDOMImplementation implements CSSStyleDeclarationFactory {
   public static final String SVG_NAMESPACE_URI = "http://www.w3.org/2000/svg";
   protected static final String RESOURCES = "org.apache.batik.dom.svg.resources.Messages";
   protected HashTable factories;
   protected static HashTable svg11Factories = new HashTable();
   protected static final DOMImplementation DOM_IMPLEMENTATION;

   public static DOMImplementation getDOMImplementation() {
      return DOM_IMPLEMENTATION;
   }

   public SVGDOMImplementation() {
      this.factories = svg11Factories;
      this.registerFeature("CSS", "2.0");
      this.registerFeature("StyleSheets", "2.0");
      this.registerFeature("SVG", new String[]{"1.0", "1.1"});
      this.registerFeature("SVGEvents", new String[]{"1.0", "1.1"});
   }

   protected void initLocalizable() {
      this.localizableSupport = new LocalizableSupport("org.apache.batik.dom.svg.resources.Messages", this.getClass().getClassLoader());
   }

   public CSSEngine createCSSEngine(AbstractStylableDocument var1, CSSContext var2, ExtendedParser var3, ValueManager[] var4, ShorthandManager[] var5) {
      ParsedURL var6 = ((SVGOMDocument)var1).getParsedURL();
      SVGCSSEngine var7 = new SVGCSSEngine(var1, var6, var3, var4, var5, var2);
      URL var8 = this.getClass().getResource("resources/UserAgentStyleSheet.css");
      if (var8 != null) {
         ParsedURL var9 = new ParsedURL(var8);
         InputSource var10 = new InputSource(var9.toString());
         var7.setUserAgentStyleSheet(var7.parseStyleSheet(var10, var9, "all"));
      }

      return var7;
   }

   public ViewCSS createViewCSS(AbstractStylableDocument var1) {
      return new CSSOMSVGViewCSS(var1.getCSSEngine());
   }

   public DocumentType createDocumentType(String var1, String var2, String var3) {
      return new GenericDocumentType(var1, var2, var3);
   }

   public Document createDocument(String var1, String var2, DocumentType var3) throws DOMException {
      SVGOMDocument var4 = new SVGOMDocument(var3, this);
      if (var2 != null) {
         var4.appendChild(var4.createElementNS(var1, var2));
      }

      return var4;
   }

   public CSSStyleSheet createCSSStyleSheet(String var1, String var2) {
      throw new UnsupportedOperationException("DOMImplementationCSS.createCSSStyleSheet is not implemented");
   }

   public CSSStyleDeclaration createCSSStyleDeclaration() {
      throw new UnsupportedOperationException("CSSStyleDeclarationFactory.createCSSStyleDeclaration is not implemented");
   }

   public StyleSheet createStyleSheet(Node var1, HashTable var2) {
      throw new UnsupportedOperationException("StyleSheetFactory.createStyleSheet is not implemented");
   }

   public CSSStyleSheet getUserAgentStyleSheet() {
      throw new UnsupportedOperationException("StyleSheetFactory.getUserAgentStyleSheet is not implemented");
   }

   public Element createElementNS(AbstractDocument var1, String var2, String var3) {
      if ("http://www.w3.org/2000/svg".equals(var2)) {
         String var4 = DOMUtilities.getLocalName(var3);
         ExtensibleDOMImplementation.ElementFactory var5 = (ExtensibleDOMImplementation.ElementFactory)this.factories.get(var4);
         if (var5 != null) {
            return var5.create(DOMUtilities.getPrefix(var3), var1);
         } else {
            throw var1.createDOMException((short)8, "invalid.element", new Object[]{var2, var3});
         }
      } else {
         return super.createElementNS(var1, var2, var3);
      }
   }

   public DocumentEventSupport createDocumentEventSupport() {
      DocumentEventSupport var1 = new DocumentEventSupport();
      var1.registerEventFactory("SVGEvents", new DocumentEventSupport.EventFactory() {
         public Event createEvent() {
            return new SVGOMEvent();
         }
      });
      var1.registerEventFactory("TimeEvent", new DocumentEventSupport.EventFactory() {
         public Event createEvent() {
            return new DOMTimeEvent();
         }
      });
      return var1;
   }

   static {
      svg11Factories.put("a", new SVGDOMImplementation.AElementFactory());
      svg11Factories.put("altGlyph", new SVGDOMImplementation.AltGlyphElementFactory());
      svg11Factories.put("altGlyphDef", new SVGDOMImplementation.AltGlyphDefElementFactory());
      svg11Factories.put("altGlyphItem", new SVGDOMImplementation.AltGlyphItemElementFactory());
      svg11Factories.put("animate", new SVGDOMImplementation.AnimateElementFactory());
      svg11Factories.put("animateColor", new SVGDOMImplementation.AnimateColorElementFactory());
      svg11Factories.put("animateMotion", new SVGDOMImplementation.AnimateMotionElementFactory());
      svg11Factories.put("animateTransform", new SVGDOMImplementation.AnimateTransformElementFactory());
      svg11Factories.put("circle", new SVGDOMImplementation.CircleElementFactory());
      svg11Factories.put("clipPath", new SVGDOMImplementation.ClipPathElementFactory());
      svg11Factories.put("color-profile", new SVGDOMImplementation.ColorProfileElementFactory());
      svg11Factories.put("cursor", new SVGDOMImplementation.CursorElementFactory());
      svg11Factories.put("definition-src", new SVGDOMImplementation.DefinitionSrcElementFactory());
      svg11Factories.put("defs", new SVGDOMImplementation.DefsElementFactory());
      svg11Factories.put("desc", new SVGDOMImplementation.DescElementFactory());
      svg11Factories.put("ellipse", new SVGDOMImplementation.EllipseElementFactory());
      svg11Factories.put("feBlend", new SVGDOMImplementation.FeBlendElementFactory());
      svg11Factories.put("feColorMatrix", new SVGDOMImplementation.FeColorMatrixElementFactory());
      svg11Factories.put("feComponentTransfer", new SVGDOMImplementation.FeComponentTransferElementFactory());
      svg11Factories.put("feComposite", new SVGDOMImplementation.FeCompositeElementFactory());
      svg11Factories.put("feConvolveMatrix", new SVGDOMImplementation.FeConvolveMatrixElementFactory());
      svg11Factories.put("feDiffuseLighting", new SVGDOMImplementation.FeDiffuseLightingElementFactory());
      svg11Factories.put("feDisplacementMap", new SVGDOMImplementation.FeDisplacementMapElementFactory());
      svg11Factories.put("feDistantLight", new SVGDOMImplementation.FeDistantLightElementFactory());
      svg11Factories.put("feFlood", new SVGDOMImplementation.FeFloodElementFactory());
      svg11Factories.put("feFuncA", new SVGDOMImplementation.FeFuncAElementFactory());
      svg11Factories.put("feFuncR", new SVGDOMImplementation.FeFuncRElementFactory());
      svg11Factories.put("feFuncG", new SVGDOMImplementation.FeFuncGElementFactory());
      svg11Factories.put("feFuncB", new SVGDOMImplementation.FeFuncBElementFactory());
      svg11Factories.put("feGaussianBlur", new SVGDOMImplementation.FeGaussianBlurElementFactory());
      svg11Factories.put("feImage", new SVGDOMImplementation.FeImageElementFactory());
      svg11Factories.put("feMerge", new SVGDOMImplementation.FeMergeElementFactory());
      svg11Factories.put("feMergeNode", new SVGDOMImplementation.FeMergeNodeElementFactory());
      svg11Factories.put("feMorphology", new SVGDOMImplementation.FeMorphologyElementFactory());
      svg11Factories.put("feOffset", new SVGDOMImplementation.FeOffsetElementFactory());
      svg11Factories.put("fePointLight", new SVGDOMImplementation.FePointLightElementFactory());
      svg11Factories.put("feSpecularLighting", new SVGDOMImplementation.FeSpecularLightingElementFactory());
      svg11Factories.put("feSpotLight", new SVGDOMImplementation.FeSpotLightElementFactory());
      svg11Factories.put("feTile", new SVGDOMImplementation.FeTileElementFactory());
      svg11Factories.put("feTurbulence", new SVGDOMImplementation.FeTurbulenceElementFactory());
      svg11Factories.put("filter", new SVGDOMImplementation.FilterElementFactory());
      svg11Factories.put("font", new SVGDOMImplementation.FontElementFactory());
      svg11Factories.put("font-face", new SVGDOMImplementation.FontFaceElementFactory());
      svg11Factories.put("font-face-format", new SVGDOMImplementation.FontFaceFormatElementFactory());
      svg11Factories.put("font-face-name", new SVGDOMImplementation.FontFaceNameElementFactory());
      svg11Factories.put("font-face-src", new SVGDOMImplementation.FontFaceSrcElementFactory());
      svg11Factories.put("font-face-uri", new SVGDOMImplementation.FontFaceUriElementFactory());
      svg11Factories.put("foreignObject", new SVGDOMImplementation.ForeignObjectElementFactory());
      svg11Factories.put("g", new SVGDOMImplementation.GElementFactory());
      svg11Factories.put("glyph", new SVGDOMImplementation.GlyphElementFactory());
      svg11Factories.put("glyphRef", new SVGDOMImplementation.GlyphRefElementFactory());
      svg11Factories.put("hkern", new SVGDOMImplementation.HkernElementFactory());
      svg11Factories.put("image", new SVGDOMImplementation.ImageElementFactory());
      svg11Factories.put("line", new SVGDOMImplementation.LineElementFactory());
      svg11Factories.put("linearGradient", new SVGDOMImplementation.LinearGradientElementFactory());
      svg11Factories.put("marker", new SVGDOMImplementation.MarkerElementFactory());
      svg11Factories.put("mask", new SVGDOMImplementation.MaskElementFactory());
      svg11Factories.put("metadata", new SVGDOMImplementation.MetadataElementFactory());
      svg11Factories.put("missing-glyph", new SVGDOMImplementation.MissingGlyphElementFactory());
      svg11Factories.put("mpath", new SVGDOMImplementation.MpathElementFactory());
      svg11Factories.put("path", new SVGDOMImplementation.PathElementFactory());
      svg11Factories.put("pattern", new SVGDOMImplementation.PatternElementFactory());
      svg11Factories.put("polygon", new SVGDOMImplementation.PolygonElementFactory());
      svg11Factories.put("polyline", new SVGDOMImplementation.PolylineElementFactory());
      svg11Factories.put("radialGradient", new SVGDOMImplementation.RadialGradientElementFactory());
      svg11Factories.put("rect", new SVGDOMImplementation.RectElementFactory());
      svg11Factories.put("set", new SVGDOMImplementation.SetElementFactory());
      svg11Factories.put("script", new SVGDOMImplementation.ScriptElementFactory());
      svg11Factories.put("stop", new SVGDOMImplementation.StopElementFactory());
      svg11Factories.put("style", new SVGDOMImplementation.StyleElementFactory());
      svg11Factories.put("svg", new SVGDOMImplementation.SvgElementFactory());
      svg11Factories.put("switch", new SVGDOMImplementation.SwitchElementFactory());
      svg11Factories.put("symbol", new SVGDOMImplementation.SymbolElementFactory());
      svg11Factories.put("text", new SVGDOMImplementation.TextElementFactory());
      svg11Factories.put("textPath", new SVGDOMImplementation.TextPathElementFactory());
      svg11Factories.put("title", new SVGDOMImplementation.TitleElementFactory());
      svg11Factories.put("tref", new SVGDOMImplementation.TrefElementFactory());
      svg11Factories.put("tspan", new SVGDOMImplementation.TspanElementFactory());
      svg11Factories.put("use", new SVGDOMImplementation.UseElementFactory());
      svg11Factories.put("view", new SVGDOMImplementation.ViewElementFactory());
      svg11Factories.put("vkern", new SVGDOMImplementation.VkernElementFactory());
      DOM_IMPLEMENTATION = new SVGDOMImplementation();
   }

   protected static class VkernElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public VkernElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMVKernElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class ViewElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public ViewElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMViewElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class UseElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public UseElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMUseElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class TspanElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public TspanElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMTSpanElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class TrefElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public TrefElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMTRefElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class TitleElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public TitleElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMTitleElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class TextPathElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public TextPathElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMTextPathElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class TextElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public TextElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMTextElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class SymbolElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public SymbolElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMSymbolElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class SwitchElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public SwitchElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMSwitchElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class SvgElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public SvgElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMSVGElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class StyleElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public StyleElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMStyleElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class StopElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public StopElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMStopElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class SetElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public SetElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMSetElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class ScriptElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public ScriptElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMScriptElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class RectElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public RectElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMRectElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class RadialGradientElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public RadialGradientElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMRadialGradientElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class PolylineElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public PolylineElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMPolylineElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class PolygonElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public PolygonElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMPolygonElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class PatternElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public PatternElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMPatternElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class PathElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public PathElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMPathElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class MpathElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public MpathElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMMPathElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class MissingGlyphElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public MissingGlyphElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMMissingGlyphElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class MetadataElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public MetadataElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMMetadataElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class MaskElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public MaskElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMMaskElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class MarkerElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public MarkerElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMMarkerElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class LinearGradientElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public LinearGradientElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMLinearGradientElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class LineElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public LineElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMLineElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class ImageElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public ImageElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMImageElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class HkernElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public HkernElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMHKernElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class GlyphRefElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public GlyphRefElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMGlyphRefElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class GlyphElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public GlyphElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMGlyphElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class GElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public GElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMGElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class ForeignObjectElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public ForeignObjectElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMForeignObjectElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FontFaceUriElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FontFaceUriElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFontFaceUriElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FontFaceSrcElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FontFaceSrcElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFontFaceSrcElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FontFaceNameElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FontFaceNameElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFontFaceNameElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FontFaceFormatElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FontFaceFormatElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFontFaceFormatElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FontFaceElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FontFaceElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFontFaceElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FontElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FontElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFontElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FilterElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FilterElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFilterElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeTurbulenceElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeTurbulenceElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFETurbulenceElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeTileElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeTileElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFETileElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeSpotLightElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeSpotLightElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFESpotLightElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeSpecularLightingElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeSpecularLightingElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFESpecularLightingElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FePointLightElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FePointLightElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEPointLightElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeOffsetElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeOffsetElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEOffsetElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeMorphologyElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeMorphologyElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEMorphologyElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeMergeNodeElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeMergeNodeElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEMergeNodeElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeMergeElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeMergeElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEMergeElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeImageElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeImageElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEImageElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeGaussianBlurElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeGaussianBlurElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEGaussianBlurElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeFuncBElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeFuncBElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEFuncBElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeFuncGElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeFuncGElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEFuncGElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeFuncRElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeFuncRElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEFuncRElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeFuncAElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeFuncAElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEFuncAElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeFloodElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeFloodElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEFloodElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeDistantLightElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeDistantLightElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEDistantLightElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeDisplacementMapElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeDisplacementMapElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEDisplacementMapElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeDiffuseLightingElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeDiffuseLightingElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEDiffuseLightingElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeConvolveMatrixElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeConvolveMatrixElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEConvolveMatrixElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeCompositeElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeCompositeElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFECompositeElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeComponentTransferElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeComponentTransferElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEComponentTransferElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeColorMatrixElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeColorMatrixElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEColorMatrixElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class FeBlendElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public FeBlendElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMFEBlendElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class EllipseElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public EllipseElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMEllipseElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class DescElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public DescElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMDescElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class DefsElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public DefsElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMDefsElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class DefinitionSrcElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public DefinitionSrcElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMDefinitionSrcElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class CursorElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public CursorElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMCursorElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class ColorProfileElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public ColorProfileElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMColorProfileElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class ClipPathElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public ClipPathElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMClipPathElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class CircleElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public CircleElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMCircleElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class AnimateTransformElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public AnimateTransformElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMAnimateTransformElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class AnimateMotionElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public AnimateMotionElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMAnimateMotionElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class AnimateColorElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public AnimateColorElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMAnimateColorElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class AnimateElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public AnimateElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMAnimateElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class AltGlyphItemElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public AltGlyphItemElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMAltGlyphItemElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class AltGlyphDefElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public AltGlyphDefElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMAltGlyphDefElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class AltGlyphElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public AltGlyphElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMAltGlyphElement(var1, (AbstractDocument)var2);
      }
   }

   protected static class AElementFactory implements ExtensibleDOMImplementation.ElementFactory {
      public AElementFactory() {
      }

      public Element create(String var1, Document var2) {
         return new SVGOMAElement(var1, (AbstractDocument)var2);
      }
   }
}
