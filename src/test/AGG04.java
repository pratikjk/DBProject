package edu.buffalo.cse.sql.test;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import edu.buffalo.cse.sql.Schema;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.plan.ExprTree;
import edu.buffalo.cse.sql.plan.PlanNode;
import edu.buffalo.cse.sql.plan.NullSourceNode;
import edu.buffalo.cse.sql.plan.ProjectionNode;
import edu.buffalo.cse.sql.plan.SelectionNode;
import edu.buffalo.cse.sql.plan.ScanNode;
import edu.buffalo.cse.sql.plan.JoinNode;
import edu.buffalo.cse.sql.plan.UnionNode;
import edu.buffalo.cse.sql.plan.AggregateNode;
public class AGG04 extends TestHarness {
  public static void main(String args[]) {
    TestHarness.main(args, new AGG04());
  }
  public void testRA() {
    Map<String, Schema.TableFromFile> tables
      = new HashMap<String, Schema.TableFromFile>();
    Schema.TableFromFile table_R;
    table_R = new Schema.TableFromFile(new File("test/r.dat"));
    table_R.add(new Schema.Column("R", "A", Schema.Type.INT));
    table_R.add(new Schema.Column("R", "B", Schema.Type.INT));
    tables.put("R", table_R);
    ScanNode child_1 = new ScanNode("R", "R", table_R);
    AggregateNode query_0 = new AggregateNode();
    query_0.addAggregate(new AggregateNode.AggColumn("Max", new ExprTree.VarLeaf(null, "A"),AggregateNode.AType.MAX));
    query_0.setChild(child_1);
    TestHarness.testQuery(tables, getResults0(), query_0);
    System.out.println("Passed RA Test AGG04");
  }
  public void testSQL() {
    List<List<Datum[]>> expected = new ArrayList<List<Datum[]>>();
    expected.add(getResults0());
    TestHarness.testProgram(new File("test/AGG04.SQL"),
                            expected);
    System.out.println("Passed SQL Test AGG04");
  }
  ArrayList<Datum[]> getResults0() {
    ArrayList<Datum[]> ret = new ArrayList<Datum[]>();
    ret.add(new Datum[] {new Datum.Int(5)}); 
    return ret;
  }
}
