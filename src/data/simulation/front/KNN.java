package data.simulation.front;

/**
 * To find k nearest neighbors of a new instance
 * Please watch my explanation of how KNN works: xxx
 *   - For classification it uses majority vote
 *   - For regression it finds the mean (average)  
 *  
 * Copyright (C) 2014 
 * @author Dr Noureddin Sadawi 
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it as you wish ONLY for legal and ethical purposes
 * 
 *  I ask you only, as a professional courtesy, to cite my name, web page 
 *  and my YouTube Channel!
 *  
 * @modified Brett Pelkey
 *  
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class KNN {

	/**
	 * Returns the majority value in an array of strings majority value is the
	 * most frequent value (the mode) handles multiple majority values (ties
	 * broken at random)
	 *
	 * @param array
	 *            an array of strings
	 * @return the most frequent string in the array
	 */
	private String findMajorityClass(String[] array) {
		// add the String array to a HashSet to get unique String values
		Set<String> h = new HashSet<String>(Arrays.asList(array));
		// convert the HashSet back to array
		String[] uniqueValues = h.toArray(new String[0]);
		// counts for unique strings
		int[] counts = new int[uniqueValues.length];
		// loop thru unique strings and count how many times they appear in
		// original array
		for (int i = 0; i < uniqueValues.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (array[j].equals(uniqueValues[i])) {
					counts[i]++;
				}
			}
		}

		// for (int i = 0; i < uniqueValues.length; i++)
		// System.out.println(uniqueValues[i]);
		// for (int i = 0; i < counts.length; i++)
		// System.out.println(counts[i]);

		int max = counts[0];
		for (int counter = 1; counter < counts.length; counter++) {
			if (counts[counter] > max) {
				max = counts[counter];
			}
		}
		// System.out.println("max # of occurences: " + max);

		// how many times max appears
		// we know that max will appear at least once in counts
		// so the value of freq will be 1 at minimum after this loop
		int freq = 0;
		for (int counter = 0; counter < counts.length; counter++) {
			if (counts[counter] == max) {
				freq++;
			}
		}

		// index of most freq value if we have only one mode
		int index = -1;
		if (freq == 1) {
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					index = counter;
					break;
				}
			}
			// System.out.println("one majority class, index is: "+index);
			return uniqueValues[index];
		} else {// we have multiple modes
			int[] ix = new int[freq];// array of indices of modes
			// System.out.println("multiple majority classes: " + freq
			// + " classes");
			int ixi = 0;
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					ix[ixi] = counter;// save index of each max count value
					ixi++; // increase index of ix array
				}
			}

			// for (int counter = 0; counter < ix.length; counter++)
			// System.out.println("class index: " + ix[counter]);

			// now choose one at random
			Random generator = new Random();
			// get random number 0 <= rIndex < size of ix
			int rIndex = generator.nextInt(ix.length);
			// System.out.println("random index: " + rIndex);
			int nIndex = ix[rIndex];
			// return unique value at that index
			return uniqueValues[nIndex];
		}

	}

	public int[] runKNN(List<double[]> shapeList) {

		int k = 1;// # of neighbours
		int[] results = new int[shapeList.size()];
		// list to save train data
		List<DataObj> dataList = new ArrayList<DataObj>();
		List<DataObj> queryList = new ArrayList<DataObj>();
		// list to save distance result

		// File train = new
		// File(getClass().getResource("/resources/merged.txt"));
		try {
			// train file is called merged.txt
			// this file should contain the shapelets for the voltage of all 9 event types
			// and will be used to predict the disturbance detected in the applet
			URL train = new URL("/path/to/merged.txt/file");
			BufferedReader brTrain = new BufferedReader(new InputStreamReader(train.openStream()));
			// add obj data to data list
			String line;
			while ((line = brTrain.readLine()) != null) {
				String[] lineArr = line.split(" ");
				double[] dArr = new double[lineArr.length];
				for (int i = 1; i < lineArr.length; i++)
					dArr[i] = Double.parseDouble(lineArr[i]);
				dataList.add(new DataObj(dArr, Integer.parseInt(lineArr[0])));
			}
			brTrain.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// find distances based on Euclideans method
		// for (double[] d : newQuery) {
		int i = 0;
		for (double[] query : shapeList) {
			List<Result> resultList = new ArrayList<Result>();
			for (DataObj obj : dataList) {
				double dist = 0.0;
				for (int j = 0; j < obj.attributes.length - 1; j++) {
					dist += Math.pow(obj.attributes[j] - query[j], 2);
					// System.out.print(city.cityAttributes[j]+" ");
				}
				double distance = Math.sqrt(dist);
				resultList.add(new Result(distance, obj.classLabel));
				// System.out.println(distance);
			}

			// System.out.println(resultList);
			Collections.sort(resultList, new DistanceComparator());
			String[] ss = new String[k];
			for (int x = 0; x < k; x++) {
				// System.out.println(resultList.get(x).classLabel + " .... "
				// + resultList.get(x).distance);
				// get classes of k nearest instances (city names) from the list
				// into an array
				ss[x] = Integer.toString(resultList.get(x).classLabel);
			}
			String majClass = findMajorityClass(ss);
			results[i] = Integer.parseInt(majClass);
			i++;
			System.out.println("Class of new instance is: " + majClass);
		}
		return results;
	}// end runKnn

	// simple class to model instances (features + class)
	static class DataObj {
		double[] attributes;
		int classLabel;

		public DataObj(double[] _attr, int _classLabel) {
			this.classLabel = _classLabel;
			this.attributes = _attr;
		}
	}

	// simple class to model results (distance + class)
	static class Result {
		double distance;
		int classLabel;

		public Result(double _distance, int _classLabel) {
			this.classLabel = _classLabel;
			this.distance = _distance;
		}
	}

	// simple comparator class used to compare results via distances
	static class DistanceComparator implements Comparator<Result> {
		@Override
		public int compare(Result a, Result b) {
			return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
		}
	}

}