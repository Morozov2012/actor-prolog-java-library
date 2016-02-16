// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.io.Serializable;

public abstract class PrologDomainName implements Serializable {
	public static final String tagDomainName_Simple			= "simple_domain";
	public static final String tagDomainName_Cluster		= "cluster_domain";
	public static final String tagDomainName_Interface		= "interface_domain";
	public static final String tagDomainName_World			= "world_domain";
	public static final String tagDomainName_DummyWorld		= "#";
	public static final String tagDomainName_Anonymous		= "'anonymous_domain'";
	public static final String tagDomainName_None			= "'no_domain'";
	//
	public static final String tagDomainItem_Domain			= "domain";
	public static final String tagDomainItem_AnyDomain		= "'any'";
	//
	public static final String tagDomainAlternative_DomainItem	= "item";
	public static final String tagDomainAlternative_DomainList	= "list";
	public static final String tagDomainAlternative_Integer		= "'integer'";
	public static final String tagDomainAlternative_IntegerConstant	= "integer_constant";
	public static final String tagDomainAlternative_IntegerRange	= "integer_range";
	public static final String tagDomainAlternative_Real		= "'real'";
	public static final String tagDomainAlternative_RealConstant	= "real_constant";
	public static final String tagDomainAlternative_RealRange	= "real_range";
	public static final String tagDomainAlternative_String		= "'string'";
	public static final String tagDomainAlternative_StringConstant	= "string_constant";
	public static final String tagDomainAlternative_Symbol		= "'symbol'";
	public static final String tagDomainAlternative_SymbolConstant	= "symbol_constant";
	public static final String tagDomainAlternative_World		= "world";
	public static final String tagDomainAlternative_ForeignDomain	= "'foreign_domain'";
	public static final String tagDomainAlternative_UnknownValue	= "#";
	public static final String tagDomainAlternative_Structure	= "structure";
	public static final String tagDomainAlternative_Set		= "set";
	public static final String tagDomainAlternative_OptimizedSet	= "optimized_set";
	public static final String tagDomainAlternative_EmptySet	= "'empty_set'";
	public static final String tagDomainAlternative_AnySet		= "'any_set'";
	public static final String tagDomainAlternative_Any		= "'any'";
	//
	public abstract String toString(CharsetEncoder encoder);
	//
	public static PrologDomainName termToPrologDomainName(Term value, ChoisePoint iX) throws TermIsNotPrologDomainName {
		try {
			long functor= value.getStructureFunctor(iX);
			Term[] arguments= value.getStructureArguments(iX);
			if (arguments.length==1) {
				if (functor==SymbolCodes.symbolCode_E_simple_domain) {
					String domainName= arguments[0].getStringValue(iX);
					return new SimpleDomainName(domainName);
				} else if (functor==SymbolCodes.symbolCode_E_world_domain) {
					long code= arguments[0].getSymbolValue(iX);
					return new WorldDomainName(code);
				} else {
					throw TermIsNotPrologDomainName.instance;
				}
			} else if (arguments.length==2) {
				if (functor==SymbolCodes.symbolCode_E_cluster_domain) {
					long clusterNumber= arguments[0].getLongIntegerValue(iX);
					String domainName= arguments[1].getStringValue(iX);
					return new ClusterDomainName(clusterNumber,domainName);
				} else if (functor==SymbolCodes.symbolCode_E_interface_domain) {
					long code= arguments[0].getSymbolValue(iX);
					String domainName= arguments[1].getStringValue(iX);
					return new InterfaceDomainName(code,domainName);
				} else {
					throw TermIsNotPrologDomainName.instance;
				}
			} else {
				throw TermIsNotPrologDomainName.instance;
			}
		} catch (TermIsNotAStructure e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_anonymous_domain) {
					return new AnonymousDomainName();
				} else if (code==SymbolCodes.symbolCode_E_no_domain) {
					return new NoDomainName();
				} else {
					throw TermIsNotPrologDomainName.instance;
				}
			} catch (TermIsNotASymbol e2) {
				try {
					value.isUnknownValue(iX);
					return new DummyWorldDomainName();
				} catch (Backtracking e3) {
					throw TermIsNotPrologDomainName.instance;
				}
			}
		} catch (TermIsNotAnInteger e1) {
			throw TermIsNotPrologDomainName.instance;
		} catch (TermIsNotASymbol e1) {
			throw TermIsNotPrologDomainName.instance;
		} catch (TermIsNotAString e1) {
			throw TermIsNotPrologDomainName.instance;
		}
	}
}
