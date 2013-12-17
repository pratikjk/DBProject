/**
 * 
 */
package edu.buffalo.cse.sql.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.plan.JoinNode;

/**
 * @author pratik
 * 
 */
public class JoinOperator {

	JoinNode node;

	/**
	 * @param node
	 */
	public JoinOperator(JoinNode node) {
		this.node = node;
	}

	/**
	 * @param lResult
	 * @param rResult
	 */
	public List<Datum[]> getTuples(List<Datum[]> lResult, List<Datum[]> rResult) {
		/*HashMap<String, Integer> schema = new HashMap<String, Integer>();
		int lSize = node.getLHS().getSchemaVars().size();
		int rSize = node.getRHS().getSchemaVars().size();
		String var="";
		for (int i = 0; i < lSize; i++) {
			var = node.getLHS().getSchemaVars().get(i).name;
			schema.put(var, i);

		}
		int i;
		for (i = 0; i < rSize; i++) {
			var = node.getRHS().getSchemaVars().get(i).name;
			if(!schema.containsKey(var))
				schema.put(var, i);
			else
				break;

		}
		int rJoinInd=i;
		int lJoinInd=schema.get(var);
*/		
		int inner=0;
		int	outer=0;
		List<Datum[]> result = new ArrayList<Datum[]>(lResult.size()*rResult.size());
		for(outer=0;outer<lResult.size();outer++){
			for ( inner = 0; inner < rResult.size(); inner++) {
				//if(lResult.get(outer)[lJoinInd].equals(rResult.get(inner)[rJoinInd]))
					result.add(concatDatum(lResult.get(outer),rResult.get(inner)/*,lJoinInd,rJoinInd*/));
			}
		}
		
		
		return result;

	}

	
	private Datum[] concatDatum(Datum[] left, Datum[] right/*, int lInd,int rInd*/) {
		Datum[] concat=new Datum[left.length+right.length]; 
		
		for(int i=0;i<left.length;i++)
		{
			concat[i]=left[i];
		}
		for(int i=0;i<right.length;i++)
		{
			/*if(i==rInd)
				continue;
			else*/
				concat[i+left.length]=right[i];
		}
		return concat;
	}

}
