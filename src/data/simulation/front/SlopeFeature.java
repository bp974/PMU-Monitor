package data.simulation.front;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is currently not used.
 * 
 * @author Brett Pelkey
 * 
 */
public class SlopeFeature {
	public List<double[]> calLineSlope(List<double[]> list) {
		int start = 1;
		String slope = "";
		List<double[]> retList = new ArrayList<double[]>();
		// String[] lineArr = line.split(" ");
		// int size = lineArr.length;
		// slope += lineArr[0];
		int count = 0;
		for (int j = 0; j < list.size(); j++) {
			double[] dArr = new double[360];
			for (int i = start; i < list.get(j).length - 2; i++) {
				double first = list.get(j)[i - 1];
				double second = list.get(j)[i + 1];
				double s = (second - first) / 2;
				System.out.println("fist=" + first + " second=" + second + " s=" + s);
				dArr[count] = s;
			}
			retList.add(dArr);
		}
		return retList;
	}
}
