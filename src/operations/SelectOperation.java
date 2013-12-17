package edu.buffalo.cse.sql.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.buffalo.cse.sql.Schema;
import edu.buffalo.cse.sql.Schema.Var;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.data.Datum.CastError;
import edu.buffalo.cse.sql.plan.ExprTree;
import edu.buffalo.cse.sql.plan.SelectionNode;
import edu.buffalo.cse.sql.plan.ExprTree.VarLeaf;

/**
 * @author pratik
 * 
 */
public class SelectOperation {
	SelectionNode node;

	/**
	 * 
	 */
	public SelectOperation(SelectionNode node) {
		this.node = node;
	}

	/**
	 * @param selectResult
	 * @param condition 
	 * @return
	 */
	public List<Datum[]> getTuples(List<Datum[]> selectResult, ExprTree condition) {
		//ExprTree condition = node.getCondition();
		//condition.op.
		switch (condition.op) {
		case ADD:
			break;
		case AND:
			List<Datum[]> lChild = getTuples(selectResult, condition.get(0));
			List<Datum[]> rChild = getTuples(selectResult, condition.get(1));
			return getIntersection(lChild,rChild);
			//break;
		case CONST:
			break;
		case DIV:
			break;
		case EQ:
			return getEquality(selectResult,condition);
			//break;
		case GT:
			return getGreaterThan(selectResult,condition);
			//break;
		case GTE:
			return getGreaterThanEqual(selectResult,condition);
			//break;
		case LT:
			return getLessThan(selectResult,condition);
			//break;
		case LTE:
			return getLessThanEqual(selectResult,condition);
			//break;
		case MULT:
			break;
		case NEQ:
			break;
		case NOT:
			break;
		case OR:
			List<Datum[]> orLChild = getTuples(selectResult, condition.get(0));
			List<Datum[]> orRChild = getTuples(selectResult, condition.get(1));
			return getUnion(orLChild,orRChild);
			//break;
		case SUB:
			break;
		case VAR:
			break;
		}
		return null;
	}

	/**
	 * @param selectResult
	 * @param condition
	 * @return
	 * 
	 * Less Than Equal to implemented considering that Integers/Floats will only be present  
	 */
	private List<Datum[]> getLessThanEqual(List<Datum[]> selectResult,
			ExprTree condition) {
		List<Var>a =node.getSchemaVars();
		VarLeaf l=(VarLeaf)condition.get(0);
		VarLeaf r=(VarLeaf)condition.get(1);
		int indl=0;
		int indr=0;
		for(int i=0;i<a.size();i++){
			if(a.get(i).equals(l.name))
				indl=i;
			if(a.get(i).equals(r.name))
				indr=i;
		}
		List<Datum[]> result= new ArrayList<Datum[]>(selectResult.size());
		for(int i=0;i<selectResult.size();i++){
			 
			try {
				if(selectResult.get(i)[indl].toInt() <= selectResult.get(i)[indr].toInt())
					result.add(selectResult.get(i));
				else
					continue;
			} catch (CastError e) {
				try {
					if(selectResult.get(i)[indl].toFloat() <= selectResult.get(i)[indr].toFloat())
						result.add(selectResult.get(i));
					else
						continue;
				} catch (CastError e1) {
					
					e1.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * @param selectResult
	 * @param condition
	 * @return
	 */
	private List<Datum[]> getLessThan(List<Datum[]> selectResult,
			ExprTree condition) {
		List<Var>a =node.getSchemaVars();
		VarLeaf l=(VarLeaf)condition.get(0);
		VarLeaf r=(VarLeaf)condition.get(1);
		int indl=0;
		int indr=0;
		for(int i=0;i<a.size();i++){
			if(a.get(i).equals(l.name))
				indl=i;
			if(a.get(i).equals(r.name))
				indr=i;
		}
		List<Datum[]> result= new ArrayList<Datum[]>(selectResult.size());
		for(int i=0;i<selectResult.size();i++){
			 
			try {
				if(selectResult.get(i)[indl].toInt() < selectResult.get(i)[indr].toInt())
					result.add(selectResult.get(i));
				else
					continue;
			} catch (CastError e) {
				try {
					if(selectResult.get(i)[indl].toFloat() < selectResult.get(i)[indr].toFloat())
						result.add(selectResult.get(i));
					else
						continue;
				} catch (CastError e1) {
					
					e1.printStackTrace();
				}
			}
		}
		return result;

	}

	/**
	 * @param selectResult
	 * @param condition
	 * @return
	 */
	private List<Datum[]> getGreaterThanEqual(List<Datum[]> selectResult,
			ExprTree condition) {
		List<Var>a =node.getSchemaVars();
		VarLeaf l=(VarLeaf)condition.get(0);
		VarLeaf r=(VarLeaf)condition.get(1);
		int indl=0;
		int indr=0;
		for(int i=0;i<a.size();i++){
			if(a.get(i).equals(l.name))
				indl=i;
			if(a.get(i).equals(r.name))
				indr=i;
		}
		List<Datum[]> result= new ArrayList<Datum[]>(selectResult.size());
		for(int i=0;i<selectResult.size();i++){
			 
			try {
				if(selectResult.get(i)[indl].toInt() >= selectResult.get(i)[indr].toInt())
					result.add(selectResult.get(i));
				else
					continue;
			} catch (CastError e) {
				try {
					if(selectResult.get(i)[indl].toFloat() >= selectResult.get(i)[indr].toFloat())
						result.add(selectResult.get(i));
					else
						continue;
				} catch (CastError e1) {
					
					e1.printStackTrace();
				}
			}
		}
		return result;

	}

	/**
	 * @param selectResult
	 * @param condition
	 * @return
	 */
	private List<Datum[]> getGreaterThan(List<Datum[]> selectResult,
			ExprTree condition) {
		List<Var>a =node.getSchemaVars();
		VarLeaf l=(VarLeaf)condition.get(0);
		VarLeaf r=(VarLeaf)condition.get(1);
		int indl=0;
		int indr=0;
		for(int i=0;i<a.size();i++){
			if(a.get(i).equals(l.name))
				indl=i;
			if(a.get(i).equals(r.name))
				indr=i;
		}
		List<Datum[]> result= new ArrayList<Datum[]>(selectResult.size());
		for(int i=0;i<selectResult.size();i++){
			 
			try {
				if(selectResult.get(i)[indl].toInt() > selectResult.get(i)[indr].toInt())
					result.add(selectResult.get(i));
				else
					continue;
			} catch (CastError e) {
				try {
					if(selectResult.get(i)[indl].toFloat() > selectResult.get(i)[indr].toFloat())
						result.add(selectResult.get(i));
					else
						continue;
				} catch (CastError e1) {
					
					e1.printStackTrace();
				}
			}
		}
		return result;

	}

	/**
	 * @param orLChild
	 * @param orRChild
	 * @return
	 * 
	 * Assuming No Duplicates
	 */
	private List<Datum[]> getUnion(List<Datum[]> orLChild,
			List<Datum[]> orRChild) {
		
		Map<Datum[], Boolean> map=new HashMap<Datum[], Boolean>();
		List<Datum[]> result= new ArrayList<Datum[]>(orLChild.size());
		for(int i=0;i<orLChild.size();i++)
		{
			map.put(orLChild.get(i), true);
			result.add(orLChild.get(i));
		}
		
		for(int j=0;j<orRChild.size();j++)
			if(!map.containsKey(orRChild.get(j)))
				result.add(orRChild.get(j));
		return result;
	}

	/**
	 * @param lChild
	 * @param rChild
	 * @return
	 * 
	 * TODO: need to check if entries from lChild are duplicates or not
	 */
	private List<Datum[]> getIntersection(List<Datum[]> lChild,
			List<Datum[]> rChild) {
		Map<Datum[], Boolean> map=new HashMap<Datum[], Boolean>();
		List<Datum[]> result= new ArrayList<Datum[]>(lChild.size());
		for(int i=0;i<lChild.size();i++)
			map.put(lChild.get(i), true);
		
		for(int j=0;j<rChild.size();j++)
			if(map.containsKey(rChild.get(j)))
				result.add(rChild.get(j));
		return result;
	}

	/**
	 * @param selectResult
	 * @param condition2 
	 * @return
	 */
	private List<Datum[]> getEquality(List<Datum[]> selectResult, ExprTree condition) {
		//Collection<ExprTree> condition=node.conjunctiveClauses();
		List<Var>a =node.getSchemaVars();
		VarLeaf l=(VarLeaf)condition.get(0);
		VarLeaf r=(VarLeaf)condition.get(1);
		int ind1=0;
		int ind2=0;
		for(int i=0;i<a.size();i++){
			if(a.get(i).equals(r.name))
				ind1=i;
			if(a.get(i).equals(l.name))
				ind2=i;
		}
		List<Datum[]> result= new ArrayList<Datum[]>(selectResult.size());
		for(int i=0;i<selectResult.size();i++){
			 
			if(selectResult.get(i)[ind1].equals(selectResult.get(i)[ind2]))
				result.add(selectResult.get(i));
			else
				continue;
		}
		return result;
	}
}
