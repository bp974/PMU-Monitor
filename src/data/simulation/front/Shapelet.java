package data.simulation.front;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.simulation.back.PmuSequence;

/**
 * This class will build shaplets out of each sequence. The idea behind this
 * class is to shorten the length of the sequences since they are very long
 * sequences. This is done to reduce the time that KNN takes to predict the
 * event type that has been detected.
 * 
 * @author Brett Pelkey
 * @date 9-15-15
 *
 */
public class Shapelet {

	// number of values before the event occurs
	private static final int POINTS_BEFORE_EVENT = 1203;

	/**
	 * shapelet_first_m will take the data from a file and for each sequence in
	 * the file it will find the max and min extreme points. It will then
	 * determine which one comes first and will create a new shapelet based off
	 * of that extreme point.
	 * 
	 * @param list
	 *            the data from the current file
	 * @param range_len
	 *            the length of the new shapelet
	 * @return a new list of lists of strings that is the new file data with
	 *         shapelets
	 * @throws IOException
	 */
	private static List<List<String>> shapelet_first_m(List<String> list, int range_len) throws IOException {
		List<List<String>> new_list = new ArrayList<List<String>>();
		String line;
		int max_index, min_index, index;
		for (int i = 0; i < list.size(); i++) {
			List<String> ret_line = new ArrayList<String>();
			line = list.get(i);
			// System.out.println(line);
			max_index = returnGlobalMinMaxIndex(line, "max");
			// System.out.println(max_index);
			min_index = returnGlobalMinMaxIndex(line, "min");
			// System.out.println(min_index);
			if (max_index < min_index)
				index = max_index;
			else
				index = min_index;
			ret_line = line_shapeletM_selection(line, index, range_len);
			new_list.add(ret_line);
		}

		return new_list;
	}

	/**
	 * returnGlobalMinMaxIndex will get the max or min index from the currenct
	 * sequence.
	 * 
	 * @param line
	 *            the sequence to get the max or min from
	 * @param method
	 *            the method to use ('max' or 'min')
	 * @return the index of the max or min
	 */
	private static int returnGlobalMinMaxIndex(String line, String method) {
		int size, start, mark, index;
		double top;
		// System.out.println(line);
		String[] array = line.split(" ");
		size = array.length;
		// System.out.println(size);
		double[] d_array = new double[size];
		// for(int i = 0; i < size; i++)
		// System.out.println(array[i]);
		for (int i = 1; i < size; i++)
			d_array[i] = Double.parseDouble(array[i]);
		start = POINTS_BEFORE_EVENT;
		mark = start;
		if (method.equals("max")) {
			top = d_array[mark];
			index = mark;
			for (int i = mark + 1; i < size; i++)
				if (d_array[i] > top) {
					top = d_array[i];
					index = i;
				}
		} else {
			top = d_array[mark];
			index = mark;
			for (int i = mark + 1; i < size; i++)
				if (d_array[i] < top) {
					top = d_array[i];
					index = i;
				}
		}
		return index;
	}

	/**
	 * line_shapeletM_selection takes one sequence and generates a new shapelet
	 * based off the current sequence. It uses the index of an extreme point and
	 * builds a new shapelet of the length of (range_len * 2) + 1.
	 * 
	 * @param line
	 *            the current sequence to create new shapelet wiht
	 * @param index
	 *            the index of the extreme point
	 * @param range_len
	 *            the length of the shapelet
	 * @return the new shapelet in a list of strings
	 */
	private static List<String> line_shapeletM_selection(String line, int index, int range_len) {
		List<String> ret_line = new ArrayList<String>();
		int size, start, end;
		String[] array = line.split(" ");
		size = array.length;
		// start = index - range_len;
		// end = index + range_len + 1;
		start = 1204;
		end = 1204 + range_len;
		for (int i = start; i < end; i++)
			if (i < 0 || i >= size)
				ret_line.add("0.0");
			else
				ret_line.add(array[i]);
		return ret_line;
	}

	public List<double[]> genShapeletList(List<PmuSequence> pmuList) {
		List<double[]> shapeList = new ArrayList<double[]>();
		int range_len = 360;
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> data = new ArrayList<String>();
		// divide list by 2 because there are 2*the number of pmus (1 for V and
		// 1 for F)
		// to get the F use for (int i = pmuList.size() / 2; i < pmuList.size();
		// i++) {
		// to get the V use for (int i = 0; i < pmuList.size() / 2; i++) {
		for (int i = 0; i < pmuList.size() / 2; i++) {
			String line = "";
			for (int j = 0; j < pmuList.get(i).getData().size(); j++) {
				if (j != pmuList.get(i).getData().size())
					line += Double.toString(pmuList.get(i).getData().get(j)) + " ";
				else
					line += Double.toString(pmuList.get(i).getData().get(j));
			}
			data.add(line);
		}
		// for (int i = 0; i < data.size(); i++)
		// System.out.println(data.get(i));
		// System.out.println("size of data list =" + data.size());
		try {
			list = shapelet_first_m(data, range_len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			double[] dArr = new double[list.get(i).size()];
			for (int j = 0; j < list.get(i).size(); j++) {
				dArr[j] = Double.parseDouble(list.get(i).get(j));
			}
			shapeList.add(dArr);
		}
		return shapeList;
	}
}
