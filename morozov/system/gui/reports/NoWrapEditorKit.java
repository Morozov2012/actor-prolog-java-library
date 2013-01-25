// (c) 2011 IRE RAS Alexei A. Morozov
// Thanks to Stanislav Lapitsky (http://java-sl.com/wrap.html)

package morozov.system.gui.reports;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.IconView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.StyleConstants;

public class NoWrapEditorKit extends StyledEditorKit {
	public ViewFactory getViewFactory() {
		return new NoWrapViewFactory();
	}
	static class NoWrapViewFactory implements ViewFactory {
		public View create(Element elem) {
			String kind= elem.getName();
			if (kind != null) {
				if (kind.equals(AbstractDocument.ContentElementName)) {
					return new LabelView(elem);
				} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
					return new NoWrapParagraphView(elem);
					// return new ParagraphView(elem);
				} else if (kind.equals(AbstractDocument.SectionElementName)) {
					return new BoxView(elem,View.Y_AXIS);
				} else if (kind.equals(StyleConstants.ComponentElementName)) {
					return new ComponentView(elem);
				} else if (kind.equals(StyleConstants.IconElementName)) {
					return new IconView(elem);
				}
			};
			return new LabelView(elem);
		}
	}
	public static class NoWrapParagraphView extends ParagraphView {
		public NoWrapParagraphView(Element elem) {
			super(elem);
		}
		public void layout(int width, int height) {
			super.layout(Short.MAX_VALUE,height);
		}
		public float getMinimumSpan(int axis) {
			return super.getPreferredSpan(axis);
		}
	}
}
