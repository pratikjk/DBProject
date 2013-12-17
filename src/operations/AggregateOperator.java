/**
 * 
 */
package edu.buffalo.cse.sql.operations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.buffalo.cse.sql.Schema;
import edu.buffalo.cse.sql.Schema.Var;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.data.Datum.CastError;
import edu.buffalo.cse.sql.data.Datum.Int;
import edu.buffalo.cse.sql.plan.AggregateNode;
import edu.buffalo.cse.sql.plan.AggregateNode.AggColumn;
import edu.buffalo.cse.sql.plan.ExprTree;
import edu.buffalo.cse.sql.plan.PlanNode;
import static edu.buffalo.cse.sql.plan.AggregateNode.AType;

/**
 * @author pratik
 * 
 */
public class AggregateOperator {

	AggregateNode node;
	List<Datum[]> result;

	/**
	 * @param node
	 */
	public AggregateOperator(AggregateNode node) {
		this.node = node;
	}

	/**
	 * @param node2
	 * @param result
	 */
	public AggregateOperator(AggregateNode node2, List<Datum[]> result) {
		this(node2);
		this.result = result;
	}

	/**
	 * @return
	 */
	public List<Datum[]> getTuples() {
		List<AggColumn> aggregates = node.getAggregates();
		Iterator<AggColumn> itr = aggregates.iterator();
		Datum[] aggResult = new Datum[aggregates.size()];
		int count = 0;
		while (itr.hasNext()) {
			List<Datum[]> exprResult;
			AggregateNode.AggColumn aggColumn = (AggregateNode.AggColumn) itr
					.next();
			switch (aggColumn.aggType) {
			case SUM:
				exprResult = evaluateArithmetic(aggColumn.expr);
				aggResult[count] = aggSum(exprResult);
				 break;
			case AVG:
				exprResult = evaluateArithmetic(aggColumn.expr);
				aggResult[count] = aggAvg(exprResult);
				break;
			case COUNT:
				aggResult[count] = aggCount(result);
				break;
			case MAX:
				aggResult[count] = aggMax(result);
				break;
			case MIN:
				aggResult[count] = aggMin(result);
				break;

			default:
				break;
			}
			count++;

		}
		List<Datum[]> res = new ArrayList<Datum[]>();
		res.add(aggResult);
		return res;
	}

	/**
	 * @param result2
	 * @return
	 */
	private Datum aggMin(List<Datum[]> result2) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < result2.size(); i++) {
			try {
				if (min > result2.get(i)[0].toInt()) {
					min = result2.get(i)[0].toInt();
				}
			} catch (CastError e) {
				e.printStackTrace();
			}
		}
		return new Datum.Int(min);
	}

	/**
	 * @param result2
	 * @return
	 */
	private Datum aggMax(List<Datum[]> result2) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < result2.size(); i++) {
			try {
				if (max < result2.get(i)[0].toInt()) {
					max = result2.get(i)[0].toInt();
				}
			} catch (CastError e) {
				e.printStackTrace();
			}
		}
		return new Datum.Int(max);
	}

	/**
	 * @param result2
	 * @return
	 */
	private Datum aggCount(List<Datum[]> result2) {

		return new Datum.Int(result2.size());
	}

	/**
	 * @param exprResult
	 * @return
	 */
	private Datum aggAvg(List<Datum[]> exprResult) {
		float sum = 0;
		for (int i = 0; i < exprResult.size(); i++) {
			try {
				sum += exprResult.get(i)[0].toInt();
			} catch (CastError e) {
				e.printStackTrace();
			}
		}
		return new Datum.Flt(sum/exprResult.size());
	}

	/**
	 * @param exprResult
	 * @return
	 */
	private Datum aggSum(List<Datum[]> exprResult) {
		int sum = 0;
		for (int i = 0; i < exprResult.size(); i++) {
			try {
				sum += exprResult.get(i)[0].toInt();
			} catch (CastError e) {
				e.printStackTrace();
			}
		}
		return new Datum.Int(sum);
		
	}

	/**
	 * @param expr
	 * @return
	 */
	private List<Datum[]> evaluateArithmetic(ExprTree expr) {
		switch (expr.op) {
		case ADD:
			List<Datum[]> lChild = evaluateArithmetic(expr.get(0));
			List<Datum[]> rChild = evaluateArithmetic(expr.get(1));
			return addColumns(lChild, rChild);
			// break;
		case CONST:
			break;
		case DIV:
			List<Datum[]> lDChild = evaluateArithmetic(expr.get(0));
			List<Datum[]> rDChild = evaluateArithmetic(expr.get(1));
			return divideColumns(lDChild, rDChild);
			// break;
		case MULT:
			List<Datum[]> lMChild = evaluateArithmetic(expr.get(0));
			List<Datum[]> rMChild = evaluateArithmetic(expr.get(1));
			return multiplyColumns(lMChild, rMChild);
			// break;
		case SUB:
			List<Datum[]> lSChild = evaluateArithmetic(expr.get(0));
			List<Datum[]> rSChild = evaluateArithmetic(expr.get(1));
			return subtractColumns(lSChild, rSChild);
			// break;
		case VAR:
			return extractColumn(((ExprTree.VarLeaf)expr).name); 
			//break;
		default:

		}
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	private List<Datum[]> extractColumn(Var name) {
		List<Var>vars=node.getChild().getSchemaVars();
		int index=0;
		for(int i=0;i<vars.size();i++){
			if(vars.get(i).name.equals(name.name)){
				index=i;
				break;
			}
		}
			List<Datum[]> col=new ArrayList<Datum[]>(result.size());
			for(int j=0;j<result.size();j++){
				col.add(j, new Datum[]{result.get(j)[index]});
			}
		
		return col;
	}

	/**
	 * @param lSChild
	 * @param rSChild
	 * @return
	 */
	private List<Datum[]> subtractColumns(List<Datum[]> lSChild,
			List<Datum[]> rSChild) {
		List<Datum[]> subR = new ArrayList<Datum[]>();
		for (int i = 0; i < lSChild.size(); i++) {
			try {
				subR.add(new Datum[] { new Datum.Int(lSChild.get(i)[0].toInt()
						* rSChild.get(i)[0].toInt()) });
			} catch (CastError e) {
				e.printStackTrace();
			}
		}
		return subR;
	}

	/**
	 * @param lMChild
	 * @param rMChild
	 * @return
	 */
	private List<Datum[]> multiplyColumns(List<Datum[]> lMChild,
			List<Datum[]> rMChild) {
		List<Datum[]> mulR = new ArrayList<Datum[]>();
		for (int i = 0; i < lMChild.size(); i++) {
			try {
				mulR.add(new Datum[] { new Datum.Int(lMChild.get(i)[0].toInt()
						* rMChild.get(i)[0].toInt()) });
			} catch (CastError e) {
				e.printStackTrace();
			}
		}
		return mulR;
	}

	/**
	 * @param lDChild
	 * @param rDChild
	 * @return
	 */
	private List<Datum[]> divideColumns(List<Datum[]> lDChild,
			List<Datum[]> rDChild) {

		List<Datum[]> divR = new ArrayList<Datum[]>();
		for (int i = 0; i < lDChild.size(); i++) {
			try {
				divR.add(new Datum[] { new Datum.Int(lDChild.get(i)[0].toInt()
						/ rDChild.get(i)[0].toInt()) });
			} catch (CastError e) {
				e.printStackTrace();
			}
		}
		return divR;
	}

	/**
	 * @param lChild
	 * @param rChild
	 * @return
	 */
	private List<Datum[]> addColumns(List<Datum[]> lChild, List<Datum[]> rChild) {
		List<Datum[]> addR = new ArrayList<Datum[]>();
		for (int i = 0; i < lChild.size(); i++) {
			try {
				addR.add(new Datum[] { new Datum.Int(lChild.get(i)[0].toInt()
						+ rChild.get(i)[0].toInt()) });
			} catch (CastError e) {
				e.printStackTrace();
			}
		}
		return addR;
	}

}