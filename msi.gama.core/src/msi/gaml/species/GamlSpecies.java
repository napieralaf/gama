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
package msi.gaml.species;

import java.util.Iterator;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.kernel.simulation.ISimulation;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.getter;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.var;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.precompiler.*;
import msi.gama.runtime.*;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gama.util.matrix.IMatrix;
import msi.gaml.descriptions.*;
import msi.gaml.expressions.IExpression;
import msi.gaml.types.IType;

/**
 * The Class Species.
 * 
 * @author drogoul
 */
@symbol(name = { IKeyword.SPECIES, IKeyword.GLOBAL, IKeyword.GRID }, kind = ISymbolKind.SPECIES, with_sequence = true)
@inside(kinds = { ISymbolKind.MODEL, ISymbolKind.ENVIRONMENT, ISymbolKind.SPECIES }, symbols = { IKeyword.ENTITIES })
@facets(value = {
	@facet(name = IKeyword.WIDTH, type = IType.INT_STR, optional = true),
	@facet(name = IKeyword.HEIGHT, type = IType.INT_STR, optional = true),
	@facet(name = IKeyword.NEIGHBOURS, type = IType.INT_STR, optional = true),
	@facet(name = IKeyword.TORUS, type = IType.BOOL_STR, optional = true),
	@facet(name = IKeyword.NAME, type = IType.ID, optional = false),
	@facet(name = IKeyword.PARENT, type = IType.ID, optional = true),
	@facet(name = IKeyword.EDGE_SPECIES, type = IType.ID, optional = true),
	@facet(name = IKeyword.SKILLS, type = IType.LABEL, optional = true),
	@facet(name = "mirrors", type = IType.LIST_STR, optional = true),
	@facet(name = IKeyword.CONTROL, type = IType.ID, /* values = { ISpecies.EMF, IKeyword.FSM }, */optional = true),
	@facet(name = "compile", type = IType.BOOL_STR, optional = true),
	@facet(name = IKeyword.FREQUENCY, type = IType.INT_STR, optional = true),
	@facet(name = IKeyword.SCHEDULES, type = IType.CONTAINER_STR, optional = true),
	@facet(name = IKeyword.TOPOLOGY, type = IType.TOPOLOGY_STR, optional = true) }, omissible = IKeyword.NAME)
@vars({ @var(name = IKeyword.NAME, type = IType.STRING_STR) })
// FIXME Build a list of control architectures dynamically at startup and populate the values
// attribute
public class GamlSpecies extends AbstractSpecies {

	public GamlSpecies(final IDescription desc) {
		super(desc);
	}

	@Override
	public boolean isGlobal() {
		return getName().equals(IKeyword.WORLD_SPECIES);
	}

	@Override
	public String getParentName() {
		return getDescription().getParentName();
	}

	@Override
	public String getArchitectureName() {
		return getLiteral(IKeyword.CONTROL);
	}

	@Override
	@getter("name")
	public String getName() {
		return super.getName();
	}

	@Override
	public SpeciesDescription getDescription() {
		return (SpeciesDescription) description;
	}

	@Override
	public boolean extendsSpecies(final ISpecies s) {
		return getDescription().getSelfWithParents().contains(s.getDescription());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.ISpecies#getFrequency()
	 */
	@Override
	public IExpression getFrequency() {
		return this.getFacet(IKeyword.FREQUENCY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.interfaces.ISpecies#getSchedule()
	 */
	@Override
	public IExpression getSchedule() {
		return this.getFacet(IKeyword.SCHEDULES);
	}

	@Override
	public IAgent get(final IScope scope, final Integer index) throws GamaRuntimeException {
		return getPopulation(scope).get(scope, index);
	}

	@Override
	public boolean contains(final IScope scope, final Object o) throws GamaRuntimeException {
		return getPopulation(scope).contains(scope, o);
	}

	@Override
	public IAgent first(final IScope scope) throws GamaRuntimeException {
		return getPopulation(scope).first(scope);
	}

	@Override
	public IAgent last(final IScope scope) throws GamaRuntimeException {
		return getPopulation(scope).last(scope);
	}

	@Override
	public int length(final IScope scope) {
		return getPopulation(scope).length(scope);
	}

	@Override
	public IAgent max(final IScope scope) throws GamaRuntimeException {
		return getPopulation(scope).max(scope);
	}

	@Override
	public IAgent min(final IScope scope) throws GamaRuntimeException {
		return getPopulation(scope).min(scope);
	}

	@Override
	public Object product(final IScope scope) throws GamaRuntimeException {
		return getPopulation(scope).product(scope);
	}

	@Override
	public Object sum(final IScope scope) throws GamaRuntimeException {
		return getPopulation(scope).sum(scope);
	}

	@Override
	public boolean isEmpty(final IScope scope) {
		return getPopulation(scope).isEmpty(scope);
	}

	@Override
	public IContainer<Integer, IAgent> reverse(final IScope scope) throws GamaRuntimeException {
		return getPopulation(scope).reverse(scope);
	}

	@Override
	public IAgent any(final IScope scope) {
		return getPopulation(scope).any(scope);
	}

	@Override
	public boolean isFixedLength() {
		return true;
	}

	@Override
	public boolean checkIndex(final Object index) {
		return false;
	}

	@Override
	public boolean checkValue(final Object value) {
		return value instanceof IAgent;
	}

	@Override
	public boolean checkBounds(final Integer index, final boolean forAdding) {
		return false;
	}

	@Override
	public void addAll(final IContainer value, final Object param) throws GamaRuntimeException {
		// NOT ALLOWED
	}

	@Override
	public void addAll(final Integer index, final IContainer value, final Object param)
		throws GamaRuntimeException {
		// NOT ALLOWED
	}

	@Override
	public void add(final IAgent value, final Object param) throws GamaRuntimeException {
		// NOT ALLOWED
	}

	@Override
	public void add(final Integer index, final IAgent value, final Object param)
		throws GamaRuntimeException {
		// NOT ALLOWED
	}

	@Override
	public boolean removeFirst(final IAgent value) throws GamaRuntimeException {
		return false;
		// NOT ALLOWED
	}

	@Override
	public boolean removeAll(final IContainer<?, IAgent> value) throws GamaRuntimeException {
		return false;
		// NOT ALLOWED
	}

	@Override
	public Object removeAt(final Integer index) throws GamaRuntimeException {
		return null;
		// NOT ALLOWED
	}

	@Override
	public void putAll(final IAgent value, final Object param) throws GamaRuntimeException {}

	@Override
	public void put(final Integer index, final IAgent value, final Object param)
		throws GamaRuntimeException {
		// NOT ALLOWED
	}

	@Override
	public void clear() throws GamaRuntimeException {
		// NOT ALLOWED
	}

	@Override
	public IMatrix matrixValue(final IScope scope) throws GamaRuntimeException {
		return getPopulation(scope).matrixValue(scope);
	}

	@Override
	public IMatrix matrixValue(final IScope scope, final ILocation preferredSize)
		throws GamaRuntimeException {
		return getPopulation(scope).matrixValue(scope, preferredSize);
	}

	@Override
	public Iterator<IAgent> iterator() {
		// FIX ME: should not have to get the current scope like this
		ISimulation sim = GAMA.getFrontmostSimulation();
		if ( sim == null ) { return GamaList.EMPTY_LIST.iterator(); }
		IScope scope = sim.getExecutionScope();
		if ( scope == null ) { return GamaList.EMPTY_LIST.iterator(); }
		return scope.getWorldScope().getPopulationFor(this).iterator();
	}

	@Override
	public IAgent getFromIndicesList(final IScope scope, final IList indices)
		throws GamaRuntimeException {
		return (IAgent) getPopulation(scope).getFromIndicesList(scope, indices);
	}

	@Override
	public boolean isMirror() {
		return getDescription().isMirror();
	}

}
