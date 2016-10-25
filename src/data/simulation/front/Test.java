package data.simulation.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Test.java is strictly used for testing of creating the unique sequence ids as
 * well as testing the query to get multiple pmu information from the db
 * 
 * @author Brett Pelkey
 * @date 8-12-15
 *
 */
public class Test {
	public static void main(String[] args) {
		/*
		 * testing unique sequence ids
		 */
		Random rand = new Random();
		List<String> seqIdArray = new ArrayList<String>();
		String seqId = "";
		for (int i = 0; i < 10; i++) {
			int randNum = rand.nextInt(298) + 1;
			System.out.println(randNum);
			seqId = "A" + 10 + "_E" + "FLT" + "_S" + randNum;
			if (seqIdArray.contains(seqId)) {
				System.out.println("ARRAY ALREADY CONTAINED THE SEQ ID");
				i--;
			} else {
				seqIdArray.add(seqId);
				System.out.println(seqId);
			}
		}

		/*
		 * testing sql queries to get multiple pmu info from database
		 */
		String selectSQL = "SELECT PMU.zone_id, PMU.pmu_name, PMU.bus_id, PMU.state, SEQUENCES.* FROM PMU, SEQUENCES "
				+ "WHERE PMU.bus_number = SEQUENCES.bus_number AND (SEQUENCES.seq_id = ";

		for (int i = 0; i < 10; i++) {
			selectSQL += "'" + seqIdArray.get(i) + "'";
			if ((10 - i) == 1) {
				selectSQL += ")";
			} else {
				selectSQL += " OR SEQUENCES.seq_id = ";
			}
			System.out.println(selectSQL);
		}
	}
}
