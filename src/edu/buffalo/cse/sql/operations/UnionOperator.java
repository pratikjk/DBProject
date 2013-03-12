/**
 * 
 */
package edu.buffalo.cse.sql.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.plan.UnionNode;

/**
 * @author pratik
 *
 */
public class UnionOperator {
	UnionNode node;

	/**
	 * @param unionNode
	 */
	public UnionOperator(UnionNode unionNode) {
		this.node=unionNode;
	}
	
	public List<Datum[]> getTuples(List<Datum[]> orLChild,
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


}