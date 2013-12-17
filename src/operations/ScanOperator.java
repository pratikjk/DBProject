/**
 * 
 */
package edu.buffalo.cse.sql.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.buffalo.cse.sql.Schema;
import edu.buffalo.cse.sql.Schema.TableFromFile;
import edu.buffalo.cse.sql.Schema.Type;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.plan.ScanNode;

/**
 * @author pratik
 * 
 */
public class ScanOperator {

	ScanNode node;

	/**
	 * 
	 */
	public ScanOperator(ScanNode q) {
		this.node = q;
	}

	/**
	 * 
	 */
	public ScanOperator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param tables 
	 * @return
	 */
	public List<Datum[]> getTuples(Map<String, TableFromFile> tables) {
		
		
		File file =tables.get(node.table).getFile(); 
		FileReader fir;
		BufferedReader reader;
		List<Datum[]> result = null;
		Datum[] data;
		try {
			fir = new FileReader(file);
			reader = new BufferedReader(fir);
			String record;
			StringTokenizer st = null;
			result = new ArrayList<Datum[]>();
			while ((record = reader.readLine()) != null) {
				data = new Datum[node.schema.size()];
				int i = 0;
				st = new StringTokenizer(record, ",");
				while (st.hasMoreElements()) {
					String token = st.nextToken().trim();
					switch (node.schema.get(i).type) {
					case INT:
						data[i] = new Datum.Int(Integer.parseInt(token));
						break;
					case FLOAT:
						data[i] = new Datum.Flt(Float.parseFloat(token));
						break;
					case STRING:
						data[i] = new Datum.Str(token);
						break;
					case BOOL:
						data[i] = new Datum.Bool(new Boolean(token));
						break;
					default:
						break;
					}
					i++;
				}
				result.add(data);

			}

			fir.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		return result;
	}

	public static void main(String[] args) {
		
	}

}