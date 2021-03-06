package ac.soton.eventb.atomicitydecomposition.generator.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.atomicitydecomposition.All;
import ac.soton.eventb.atomicitydecomposition.Child;
import ac.soton.eventb.atomicitydecomposition.FlowDiagram;
import ac.soton.eventb.atomicitydecomposition.Leaf;
import ac.soton.eventb.atomicitydecomposition.Loop;
import ac.soton.eventb.atomicitydecomposition.One;
import ac.soton.eventb.atomicitydecomposition.Par;
import ac.soton.eventb.atomicitydecomposition.Some;
import ac.soton.eventb.atomicitydecomposition.TypedParameterExpression;
import ac.soton.eventb.atomicitydecomposition.generator.strings.Strings;
import ac.soton.eventb.atomicitydecomposition.generator.utils.Utils;
import ac.soton.eventb.emf.diagrams.generator.AbstractRule;
import ac.soton.eventb.emf.diagrams.generator.GenerationDescriptor;
import ac.soton.eventb.emf.diagrams.generator.IRule;
import ac.soton.eventb.emf.diagrams.generator.utils.Find;
import ac.soton.eventb.emf.diagrams.generator.utils.Make;

import org.eventb.emf.core.machine.Invariant; // test


public class TR_leaf4_pLeaf_sInv extends AbstractRule  implements IRule {
	
	@Override
	public boolean enabled(EventBElement sourceElement) throws Exception  {
		Leaf sourceLeaf = (Leaf) sourceElement;
		FlowDiagram parentFlow = Utils.getParentFlow(sourceLeaf);
		return sourceLeaf.getDecompose().isEmpty() &&
				!(sourceLeaf.eContainer() instanceof Loop) && //non loop leaf
				Utils.predecessor(sourceLeaf, parentFlow.getParameters(), parentFlow.isSw()) != null;
	}

	//TODO add dependency on previously typed variable
	@Override
	public boolean dependenciesOK(EventBElement sourceElement, final List<GenerationDescriptor> generatedElements) throws Exception  {
//		Leaf sourceLeaf = (Leaf) sourceElement;		
//		Machine	container = (Machine)EcoreUtil.getRootContainer(sourceElement);
//		
//		
//		FlowDiagram parentFlow = Utils.getParentFlow(sourceLeaf);
//		Child parentChild = Utils.getParentChild(sourceLeaf);
//		
//		List<Object> pred = Utils.predecessor(sourceLeaf, parentFlow.getParameters(), parentFlow.isSw());
//		List<TypedParameterExpression> par = new ArrayList<TypedParameterExpression>();
//		
//		par.addAll(parentFlow.getParameters());
//		
//		if(parentChild instanceof All){
//			par.add( ((All)parentChild).getNewParameter() );
//		}
//		else if(parentChild instanceof Some){
//			par.add( ((Some)parentChild).getNewParameter() );
//		}
//		else if(parentChild instanceof One){
//			par.add( ((One)parentChild).getNewParameter() );
//		}
//		else if(parentChild instanceof Par){
//			par.add( ((Par)parentChild).getNewParameter() );
//		}
//
//		String mustBeTypedBefore = Utils.build_seq_inv((Child)pred.get(0), (List<TypedParameterExpression>)pred.get(1) , sourceLeaf, par).get(2);
//	
		/**
		 * FIXME Ordering problems
		 */
//		System.out.println(mustBeTypedBefore);
		return true;//Find.generatedElement(generatedElements, container, invariants, Strings.INV_ + mustBeTypedBefore + Strings._SEQ) != null || 
				//Find.generatedElement(generatedElements, container, invariants, Strings.INV_ + mustBeTypedBefore + Strings._TYPE) != null;
		
		//----------------------------------------------------------------------------------------
		//Dana Test
		/*Leaf sourceLeaf = (Leaf) sourceElement;	
		Machine	container = (Machine)EcoreUtil.getRootContainer(sourceElement);
		FlowDiagram parentFlow = Utils.getParentFlow(sourceLeaf);

        boolean ret = true;
		List<Object> pred = Utils.predecessor(sourceLeaf, parentFlow.getParameters(), parentFlow.isSw());
		for(int i = 0; i < pred.size(); i++){
			if(pred.get(i) instanceof Leaf){
			String pred_inv_name = Strings.INV_ + ((Leaf) pred.get(i)).getName() + "_" + parentFlow.getName() + Strings._SEQ;
			String type_name = Strings.INV_ + ((Leaf) pred.get(i)).getName() + Strings._TYPE;
			
				if (Find.generatedElement(generatedElements, container, invariants, type_name) == null){
					if(Find.generatedElement(generatedElements, container, invariants, pred_inv_name) == null){
						ret= false;
						break;
					}
				
				}	
				
			}
		}
		//String pred_inv_name = Strings.INV_ + pred.getName() + "_" + parentFlow.getName() + Strings._SEQ;
		return ret;*/
		//----------------------------------------------------------------------------------------
		
	}

	
	
	
	/**
	 * TR_leaf4, Transform a leaf with a predecessor node to a sequencing invariant
	 */
	@Override
	public List<GenerationDescriptor> fire(EventBElement sourceElement, List<GenerationDescriptor> generatedElements) throws Exception {
		List<GenerationDescriptor> ret = new ArrayList<GenerationDescriptor>();
		Leaf sourceLeaf = (Leaf) sourceElement;		
		Machine	container = (Machine)EcoreUtil.getRootContainer(sourceElement);
		
		//String name = Strings.INV_ + sourceLeaf.getName() + Strings._SEQ;
		
		FlowDiagram parentFlow = Utils.getParentFlow(sourceLeaf);
		
		String name = Strings.INV_ + sourceLeaf.getName() + "_" + parentFlow.getName() + Strings._SEQ;//fixed by dana to allow more than 1 diagram
		
		Child parentChild = Utils.getParentChild(sourceLeaf);
		
		List<Object> pred = Utils.predecessor(sourceLeaf, parentFlow.getParameters(), parentFlow.isSw());
		List<TypedParameterExpression> par = new ArrayList<TypedParameterExpression>();
		
		par.addAll(parentFlow.getParameters());
		
		if(parentChild instanceof All){
			par.add( ((All)parentChild).getNewParameter() );
		}
		else if(parentChild instanceof Some){
			par.add( ((Some)parentChild).getNewParameter() );
		}
		else if(parentChild instanceof One){
			par.add( ((One)parentChild).getNewParameter() );
		}
		else if(parentChild instanceof Par){
			par.add( ((Par)parentChild).getNewParameter() );
		}

		@SuppressWarnings("unchecked")
		String predicate = Utils.build_seq_inv_string(Utils.build_seq_inv((Child)pred.get(0), (List<TypedParameterExpression>)pred.get(1) , sourceLeaf, par));
		//Must be placed in inverse order so dependencies come before
		
		//----------------------------------------------------
		//Dana
		//just test
		// This has fixed the ordering problem 
		
		// This has fixed the ordering but now xor-inv is added before?
		
		/*List <Invariant> invs = container.getInvariants();
		Invariant inv = Make.invariant(name, predicate, "");
		invs.add(inv);
		ret.add(Make.descriptor(container, invariants, inv, 2));*/
		//----------------------------------------------------
		ret.add(Make.descriptor(container, invariants, Make.invariant(name, predicate, ""), 2));
		
		
		
		return ret;
		
	}
	

	
	
}
