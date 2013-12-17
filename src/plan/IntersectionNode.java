/**
 * 
 *//*
package edu.buffalo.cse.sql.plan;

*//**
 * @author pratik
 *
 *//*
public class IntersectionNode extends PlanNode.Binary{
		  
	  public IntersectionNode(){ super(PlanNode.Type.UNION); }
	  
	  public List<Schema.Var> getSchemaVars()
	  {
	    //This is unsafe.  You should have a typechecking step to validate that
	    //the union's input schemas are alike.
	    return getLHS().getSchemaVars();
	  }
	    
	  public static UnionNode make(PlanNode lhs, PlanNode rhs){
	    UnionNode u = new UnionNode();
	    u.setLHS(lhs); u.setRHS(rhs);
	    return u;
	  }
	}


}
*/