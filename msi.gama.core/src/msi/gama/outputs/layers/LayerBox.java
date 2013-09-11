/*
 * GAMA - V1.4 http://gama-platform.googlecode.com
 * 
 * (c) 2007-2011 UMI 209 UMMISCO IRD/UPMC & Partners (see below)
 * 
 * Developers :
 * 
 * - Alexis Drogoul, UMI 209 UMMISCO, IRD/UPMC (Kernel, Metamodel, GAML), 2007-2012
 * - Vo Duc An, UMI 209 UMMISCO, IRD/UPMC (SWT, multi-level architecture), 2008-2012
 * - Patrick Taillandier, UMR 6228 IDEES, CNRS/Univ. Rouen (Batch, GeoTools & JTS), 2009-2012
 * - Beno�t Gaudou, UMR 5505 IRIT, CNRS/Univ. Toulouse 1 (Documentation, Tests), 2010-2012
 * - Phan Huy Cuong, DREAM team, Univ. Can Tho (XText-based GAML), 2012
 * - Pierrick Koch, UMI 209 UMMISCO, IRD/UPMC (XText-based GAML), 2010-2011
 * - Romain Lavaud, UMI 209 UMMISCO, IRD/UPMC (RCP environment), 2010
 * - Francois Sempe, UMI 209 UMMISCO, IRD/UPMC (EMF model, Batch), 2007-2009
 * - Edouard Amouroux, UMI 209 UMMISCO, IRD/UPMC (C++ initial porting), 2007-2008
 * - Chu Thanh Quang, UMI 209 UMMISCO, IRD/UPMC (OpenMap integration), 2007-2008
 */
package msi.gama.outputs.layers;

import msi.gama.metamodel.shape.*;
import msi.gama.runtime.*;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.expressions.*;
import msi.gaml.operators.Cast;

/**
 * Written by drogoul Modified on 16 nov. 2010
 * 
 * @todo Description
 * 
 */
public class LayerBox implements IDisplayLayerBox {

	IExpression transparency = new ConstantExpression(0d);
	IExpression position = new ConstantExpression(new GamaPoint(0, 0, 0));
	IExpression size = new ConstantExpression(new GamaPoint(1, 1, 1));
	IExpression refresh = new ConstantExpression(true);

	Double currentTransparency = 0d;
	ILocation currentPosition;
	ILocation currentSize;
	// Double currentElevation;
	Boolean currentRefresh;

	ILocation constantPosition = null;
	ILocation constantSize = null;
	Double constantTransparency = null;
	// Double constantElevation = null;
	Boolean constantRefresh = null;

	// Rectangle2D.Double currentBoundingBox = new Rectangle2D.Double();
	boolean constantBoundingBox = false;

	public LayerBox(final IExpression transp, final IExpression pos, final IExpression ext, final IExpression refr)
		throws GamaRuntimeException {
		IScope scope = GAMA.obtainNewScope();
		setTransparency(scope, transp == null ? transparency : transp);
		setPosition(scope, pos == null ? position : pos);
		setSize(scope, ext == null ? size : ext);
		// setElevation(scope, elev == null ? elevation : elev);
		setRefresh(scope, refr == null ? refresh : refr);

	}

	// public LayerBox(final Double transp, final GamaPoint pos, final GamaPoint ext, final Double elev, final Boolean
	// refr) {
	// setTransparency(transp);
	// setPosition(pos);
	// setExtent(ext);
	// setElevation(elev);
	// setRefresh(refr);
	// }

	// public LayerBox(final Double transp, final Double posx, final Double posy, final Double posz, final Double extx,
	// final Double exty, final Double elev, final Boolean refr) {
	// setTransparency(transp);
	// setPosition(posx, posy, posz);
	// setExtent(extx, exty);
	// setElevation(elev);
	// setRefresh(refr);
	// }

	@Override
	public void compute(final IScope scope) throws GamaRuntimeException {
		try {
			currentTransparency =
				constantTransparency == null ? 1d - Math.min(
					Math.max(Cast.asFloat(scope, transparency.value(scope)), 0d), 1d) : constantTransparency;
			if ( !constantBoundingBox ) {
				currentPosition =
					constantPosition == null ? Cast.asPoint(scope, position.value(scope)) : constantPosition;
				currentSize = constantSize == null ? Cast.asPoint(scope, size.value(scope)) : constantSize;
				// if ( currentPosition != null && currentExtent != null ) {
				// computeBoundingBox();
				// }
				// currentElevation =
				// constantElevation == null ? Cast.asFloat(scope, elevation.value(scope)) : constantElevation;
				currentRefresh = constantRefresh == null ? Cast.asBool(scope, refresh.value(scope)) : constantRefresh;
			}
		} catch (Exception e) {
			throw GamaRuntimeException.create(e);
		}

	}

	@Override
	public void setTransparency(final IScope scope, final IExpression t) throws GamaRuntimeException {
		if ( t != null ) {
			transparency = t;
			if ( t.isConst() ) {
				setTransparency(Cast.asFloat(scope, t.value(scope)));
			}
		}
	}

	@Override
	public void setPosition(final IScope scope, final IExpression p) throws GamaRuntimeException {
		if ( p != null ) {
			position = p;
			// if ( p instanceof BinaryOperator ) {
			// isAbsoluteX = ((BinaryOperator) p).left().containsAny(PixelUnitExpression.class);
			// isAbsoluteY = ((BinaryOperator) p).right().containsAny(PixelUnitExpression.class);
			// }
			if ( p.isConst() ) {
				setPosition(Cast.asPoint(scope, position.value(scope)));
			}
		}
	}

	@Override
	public void setSize(final IScope scope, final IExpression e) throws GamaRuntimeException {
		if ( e != null ) {
			size = e;
			// if ( e instanceof BinaryOperator ) {
			// isAbsoluteWidth = ((BinaryOperator) e).left().containsAny(PixelUnitExpression.class);
			// isAbsoluteHeight = ((BinaryOperator) e).right().containsAny(PixelUnitExpression.class);
			// } else {
			// isAbsoluteHeight = e.containsAny(PixelUnitExpression.class);
			// }
			if ( e.isConst() ) {
				setSize(Cast.asPoint(scope, size.value(scope)));
			}
		}
	}

	// @Override
	// public void setElevation(final IScope scope, final IExpression e) throws GamaRuntimeException {
	// if ( e != null ) {
	// elevation = e;
	// if ( e.isConst() ) {
	// setElevation(Cast.asFloat(scope, e.value(scope)));
	// }
	// }
	// }

	@Override
	public void setRefresh(final IScope scope, final IExpression r) throws GamaRuntimeException {
		if ( r != null ) {
			refresh = r;
			if ( r.isConst() ) {
				setRefresh(Cast.asBool(scope, r.value(scope)));
			}
		}

	}

	@Override
	public void setTransparency(final double f) {
		currentTransparency = constantTransparency = 1d - Math.min(Math.max(f, 0d), 1d);
	}

	@Override
	public void setSize(final ILocation p) {
		setSize(p.getX(), p.getY(), p.getZ());
	}

	@Override
	public void setSize(final double width, final double height, final double depth) {
		currentSize = constantSize = new GamaPoint(width, height, depth);
		if ( constantPosition != null ) {
			constantBoundingBox = true;
			// computeBoundingBox();
		}
	}

	@Override
	public void setPosition(final ILocation p) {
		setPosition(p.getX(), p.getY(), p.getZ());
	}

	@Override
	public void setPosition(final double x, final double y, final double z) {
		currentPosition = constantPosition = new GamaPoint(x, y, z);
		if ( constantSize != null ) {
			constantBoundingBox = true;
			// computeBoundingBox();
		}
	}

	// @Override
	// public void setElevation(final double e) {
	// currentElevation = constantElevation = e;
	// }

	@Override
	public void setRefresh(final Boolean r) {
		currentRefresh = constantRefresh = r;

	}

	@Override
	public final Double getTransparency() {
		return currentTransparency;
	}

	// @Override
	// public Rectangle2D.Double getBoundingBox() {
	// return currentBoundingBox;
	// }

	@Override
	public ILocation getPosition() {
		return currentPosition;
	}

	@Override
	public ILocation getSize() {
		return currentSize;
	}

	//
	// @Override
	// public final Double getElevation() {
	// return currentElevation;
	// }

	@Override
	public Boolean getRefresh() {
		return currentRefresh;
	}

	// private Rectangle2D.Double computeBoundingBox() {
	// currentBoundingBox.setRect(currentPosition.getX(), currentPosition.getY(), Math.abs(currentExtent.getX()),
	// Math.abs(currentExtent.getY()));
	// return currentBoundingBox;
	// }

}
