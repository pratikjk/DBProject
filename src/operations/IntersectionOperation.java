/**
 * 
 */
package edu.buffalo.cse.sql.operations;

import edu.buffalo.cse.sql.plan.SelectionNode;

/**
 * @author pratik
 *
 */
public class IntersectionOperation {
	
	SelectionNode node;
	
	public IntersectionOperation(SelectionNode node){
		this.node=node;
	}

}
