// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class Java2DSetComposite extends Java2DAuxiliaryCommand {
	protected int rule;
	protected float alpha;
	protected AlphaComposite composite;
	public Java2DSetComposite(int r) {
		this(r,1.0f);
	}
	public Java2DSetComposite(int r, float a) {
		rule= r;
		alpha= a;
		composite= AlphaComposite.getInstance(rule,alpha);
	}
	public void execute(Graphics2D g2, DrawingMode drawingMode) {
		g2.setComposite(composite);
	}
}
