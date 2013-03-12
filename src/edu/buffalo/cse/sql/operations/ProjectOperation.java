/**
 * 
 */
package edu.buffalo.cse.sql.operations;

import java.util.ArrayList;
import java.util.List;

import edu.buffalo.cse.sql.Schema.Var;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.data.Datum.CastError;
import edu.buffalo.cse.sql.data.Datum.Int;
import edu.buffalo.cse.sql.plan.ExprTree;
import edu.buffalo.cse.sql.plan.ExprTree.ConstLeaf;
import edu.buffalo.cse.sql.plan.ExprTree.OpCode;
import edu.buffalo.cse.sql.plan.ExprTree.VarLeaf;
import edu.buffalo.cse.sql.plan.ProjectionNode;
import edu.buffalo.cse.sql.plan.ProjectionNode.Column;

/**
 * @author pratik
 * 
 */
public class ProjectOperation {

	ProjectionNode node;

	/**
	 * @param projNode
	 */
	public ProjectOperation(ProjectionNode projNode) {
		this.node = projNode;
	}

	/**
	 * @param projResult
	 * @param e
	 * @return
	 */
	public List<Datum[]> getTuples(List<Datum[]> projResult, ExprTree e) {
		List<Datum[]> result = new ArrayList<Datum[]>(projResult.size());
		List<Column> columns = node.getColumns();
		Datum[] tuple = new Datum[columns.size()];
		Datum output;
		int index=0;
		ExprTree.OpCode op;
		for (Column c : columns) {
			op = c.expr.op;
			switch (op) {
			case ADD:
				output = evaluateArithmetic(c.expr);
				tuple[index]=output;
				//result.add(tuple);
				//return result;
				 break;
			case AND:
				Boolean lNChild = evaluateBool(c.expr.get(0));
				Boolean rNChild = evaluateBool(c.expr.get(1));
				tuple[index] = new Datum.Bool(lNChild && rNChild);
				//result.add(tuple);
				//return result;
				 break;
			case CONST:
				tuple[index] = ((ConstLeaf) c.expr).v;
				//result.add(tuple);
				//return result;
				 break;
			case DIV:
				output = evaluateArithmetic(c.expr);
				tuple[index]=output;
				//result.add(tuple);
				//return result;
				break;
			case EQ:
				break;
			case GT:
				break;
			case GTE:
				break;
			case LT:
				break;
			case LTE:
				break;
			case MULT:
				output = evaluateArithmetic(c.expr);
				tuple[index]=output;
				//result.add(tuple);
				//return result;
				break;
			case NEQ:
				break;
			case NOT:
				Boolean notChild = evaluateBool(c.expr.get(0));
				tuple[index] = new Datum.Bool(!notChild);
				//result.add(tuple);
				//return result;
				break;
			case OR:
				Boolean lRChild = evaluateBool(c.expr.get(0));
				Boolean rRChild = evaluateBool(c.expr.get(1));
				tuple[index] = new Datum.Bool(rRChild || lRChild);
				//result.add(tuple);
				//return result;
				 break;
			case SUB:
				output = evaluateArithmetic(c.expr);
				tuple[index]=output;
				//result.add(tuple);
				//return result;
				break;
			case VAR:
				result=projectColumns(projResult,result,index,columns.size(),c);
				break;
			}
			index++;
		}
		if(tuple[0]!=null)
			result.add(tuple);
		return result;
	}

	/**
	 * @param projResult
	 * @param result 
	 * @param index 
	 * @param colSize 
	 * @param c 
	 * @return
	 */
	private List<Datum[]> projectColumns(List<Datum[]> projResult, List<Datum[]> result, int index, int colSize, Column c) {
		List<Var>a =node.getChild().getSchemaVars();
		VarLeaf l=(VarLeaf)c.expr;
		Datum d;
		int indl=0;
		for(int i=0;i<a.size();i++){
			if(a.get(i).equals(l.name))
			{
				indl=i;
				break;
			}
		}
		for(int j=0;j<projResult.size();j++){
			if(index!=0){
				Datum[] arr=new Datum[colSize];
				for(int k=0;k<index;k++){
					arr[k]=result.get(j)[k];
				}
				arr[index]=projResult.get(j)[indl];
				result.set(j, arr);
			}
			else{
				 d = projResult.get(j)[indl];
				 result.add(new Datum[]{d});
			}
		}
		return result;
	}

	/**
	 * @param exprTree
	 * @return
	 */
	private Datum evaluateArithmetic(ExprTree exprTree) {
		switch (exprTree.op) {
		case ADD:
			Datum lAdd = evaluateArithmetic(exprTree.get(0));
			Datum rAdd = evaluateArithmetic(exprTree.get(1));
			float fAdd=0;
			try {
				fAdd = lAdd.toFloat()+rAdd.toFloat();
			} catch (CastError e) {
				e.printStackTrace();
			}
			if(lAdd instanceof Datum.Int && rAdd instanceof Datum.Int)
				return new Datum.Int((int)fAdd);
			else
				return new Datum.Flt(fAdd);
		
		case CONST:
			return ((ConstLeaf)exprTree).v;
			//break;
		case DIV:
			//Check if the denominator is zero; returning zero
			Datum lDiv = evaluateArithmetic(exprTree.get(0));
			Datum rDiv = evaluateArithmetic(exprTree.get(1));
			try {
				if((int)rDiv.toFloat()==0){
					return null;
				}
			} catch (CastError e1) {
				e1.printStackTrace();
			}
			float fDiv=0;
			try {
				fDiv = lDiv.toFloat()/rDiv.toFloat();
			} catch (CastError e) {
				e.printStackTrace();
			}
			return new Datum.Flt(fDiv);
			//break;
		case MULT:
			Datum lMul = evaluateArithmetic(exprTree.get(0));
			Datum rMul = evaluateArithmetic(exprTree.get(1));
			float fMul=0;
			try {
				fMul = lMul.toFloat() * rMul.toFloat();
			} catch (CastError e) {
				e.printStackTrace();
			}
			if(lMul instanceof Datum.Int && rMul instanceof Datum.Int)
				return new Datum.Int((int)fMul);
			else
				return new Datum.Flt(fMul);
		case SUB:
			
			Datum lSub = evaluateArithmetic(exprTree.get(0));
			Datum rSub = evaluateArithmetic(exprTree.get(1));
			float fSub=0;
			try {
				fSub = lSub.toFloat() - rSub.toFloat();
			} catch (CastError e) {
				e.printStackTrace();
			}
			if(lSub instanceof Datum.Int && rSub instanceof Datum.Int)
				return new Datum.Int((int)fSub);
			else
				return new Datum.Flt(fSub);
		}

		return null;
	}

	/**
	 * @param exprTree
	 * @return
	 */
	private Boolean evaluateBool(ExprTree exprTree) {
		Boolean value = null;
		try {
			value = ((ConstLeaf) exprTree).v.toBool();
		} catch (CastError e) {

			e.printStackTrace();
		}
		return value;
		// null;
	}

}