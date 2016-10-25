package data.simulation.back;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The SequenceDriver class gets all the Sequence data from the all the csv
 * files and inserts into database.
 * 
 * ***Note*** If the csv files have been altered in any way this class will not
 * work. DO NOT ALTER THE FILES IN ANY WAY UNLESS YOU MAKE A COPY FIRST
 * 
 * It also sends all the Sequences to MySQL database
 * 
 * Also includes a create table method to create the SEQUENCES table in the db
 * 
 * Note: Not used during running of the applet
 *
 * @author Brett Pelkey
 * @date 7-14-15
 */
public class SequenceDriver {

	private Connection dbConnection = null;
	private PreparedStatement preparedStatement = null;

	// list to hold all the individual sequences
	private List<Sequence> sequenceList = null;

	// constants for start and end of data row numbers
	/*
	 * data starts on row 2235 but because of 5 empty rows are not counted in
	 * readRecord method when row 2230 is reached it is the start of real data
	 */
	private static final int START_OF_DATA = 2230;
	private static final int END_OF_DATA = 5830;

	public static DbConf dbConf;

	// main method to make call to readAllCsvFiles() which starts the program
	// and all other calls are within readAllCsvFiles()
	/**
	 * Main method that will start the connection to the db and add all the data
	 * from the csv files to the database
	 * 
	 * To run uncomment s.readAllCsvFiles() and make change to folder location
	 * inside that method to correspond with the folder of csv files on your
	 * machine
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		dbConf = new DbConf();
		dbConf = dbConf.getPropValues("/path/to/.properties/file");

		// print time before and after method call to test the time it takes
		System.out.println("Starting time: " + new Date());
		SequenceDriver s = new SequenceDriver();

		try {
			s.dbConnection = DriverManager.getConnection(dbConf.url, dbConf.userName, dbConf.password);
		} catch (SQLException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * uncomment next line to actually run class...commented to avoid
		 * possible double entries on accidental run
		 */
		// s.readAllCsvFiles();
		System.out.println("Ending time: " + new Date());
		// s.createTableStatement();
	}

	/**
	 * readSingleCsv() Method
	 * 
	 * This method will cycle through a single csv file and extract all the data
	 * into an array list of sequences
	 * 
	 * @param fileName
	 *            the filename of current file to get all the data from
	 * @param eventType
	 *            the type of event extracted from filename in readAllCsvFiles
	 *            method
	 * @param area_id
	 *            the area id of the sequence
	 */
	private void readSingleCsv(String fileName, String eventType, String area_id) {
		try {
			// create the unique sequence id for the sequence
			String seq_id = "A" + area_id + "_E" + eventType + "_S";
			// convert area_id from string to int
			int i_area_id = Integer.parseInt(area_id);
			// read csv file using CSVReader class
			// CsvReader sequenceData = new CsvReader(
			// "/Users/Brett/interngit/Files/PMU_DATA_SAMPLE_ORIG/PMU_DATA_SAMPLE/FLT_A10_Z100_B10011.csv");

			// the folder location + the name of the csv file to read
			CsvReader sequenceData = new CsvReader("/Users/btp97/Desktop/TEMPCSV/" + fileName);

			// create array list to hold all Sequence data
			sequenceList = new ArrayList<Sequence>();
			// List<Double> dataList = new ArrayList<Double>();
			int i = 1;
			// loop through all rows until the data starts
			// data starts on row 2235 but because of 5 empty rows are not
			// counted in readRecord method when i reaches 2230 it is the start
			// of real data
			while (i < START_OF_DATA) {
				sequenceData.readRecord();
				i++;
			}
			// create 298 sequences with char type of V
			char type = 'V';
			for (int k = 1; k < 299; k++) {
				Sequence sequence = new Sequence(k, i_area_id, type, eventType, seq_id + k, fileName);
				sequenceList.add(sequence);
			}
			// create 298 sequences with char type of F
			type = 'F';
			for (int k = 1; k < 299; k++) {
				Sequence sequence = new Sequence(k, i_area_id, type, eventType, seq_id + k, fileName);
				sequenceList.add(sequence);
			}
			// test printing sequence
			// for (int k = 0; k < sequenceList.size(); k++)
			// System.out.println(sequenceList.get(k));

			// loop through all the rows that contain data
			while (i < END_OF_DATA) {
				sequenceData.readRecord();

				// add all the V data to the list
				// j starts at 1 because the first column to get data from is
				// column 2 but index 1
				for (int j = 1; j < 299; j++) {
					double singleSeqData = Double.parseDouble(sequenceData.get(j));
					// j - 1 so that we can add the data to index 0 of the list
					// to start with
					sequenceList.get(j - 1).getData().add(singleSeqData);
				}
				// add all the F data to the list
				for (int j = 299; j < 597; j++) {
					double singleSeqData = Double.parseDouble(sequenceData.get(j));
					sequenceList.get(j - 1).getData().add(singleSeqData);
				}
				i++;
			}
			// test printing
			// for (int j = 297; j < 298; j++)
			// System.out.println(sequenceList.get(j));
			System.out.println(fileName + " " + sequenceList.size());
			System.out.println(sequenceList.get(0).getData().size());

			// close instance to CsvReader class
			sequenceData.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * insertSequenceData() Method
	 * 
	 * This method will the take the array list after reading single csv file
	 * and all the data to the db using a prepared statement
	 * 
	 * @throws SQLException
	 */
	private void insertSequenceData() throws SQLException {

		// sql statement
		String insertTableSQL = "INSERT INTO SEQUENCES"
				+ "(bus_number, area_id, type, event_type, seq_id, file_name, data) VALUES" + "(?,?,?,?,?,?,?)";

		// try to add each sequence to the db using a prepared statement
		try {
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);
			int i = 0;
			while (i < sequenceList.size()) {
				/*-System.out.println("bus#: " + sequenceList.get(i).getBusNumber());
				System.out.println("area_id: " + sequenceList.get(i).getAreaId());
				System.out.println("type: " + sequenceList.get(i).getType());
				System.out.println("e_type: " + sequenceList.get(i).getEventType());
				System.out.println("seq_id: " + sequenceList.get(i).getSeqId());
				System.out.println("filename: " + sequenceList.get(i).getFileName());*/

				preparedStatement.setInt(1, sequenceList.get(i).getBusNumber());
				preparedStatement.setInt(2, sequenceList.get(i).getAreaId());
				preparedStatement.setString(3, sequenceList.get(i).getType());
				preparedStatement.setString(4, sequenceList.get(i).getEventType());
				preparedStatement.setString(5, sequenceList.get(i).getSeqId());
				preparedStatement.setString(6, sequenceList.get(i).getFileName());
				// substring the data to get rid of [ ] at beginning and end
				String data = sequenceList.get(i).getData().toString();
				data = data.substring(1, data.length() - 1);
				// data = data.replace("[", "");
				// data = data.replace("]", "");
				preparedStatement.setString(7, data);
				// preparedStatement.setString(6, sequenceList.get(i).getData()
				// .toString());

				// execute insert SQL stetement
				preparedStatement.executeUpdate();

				// System.out.println("Record is inserted into SEQUENCES
				// table!");
				i++;
			}
			System.out.println("ALL Records have been inserted into SEQUENCES table!");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}

		}
	}

	private void outputToCsv() {
		String outputFile = "test.csv";

		// before we open the file check to see if it already exists
		boolean alreadyExists = new File(outputFile).exists();

		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');

			for (int i = 0; i < sequenceList.size() / 2; i++) {
				int classLabel = 0;
				switch (sequenceList.get(i).getEventType()) {
				case "FLT":
					classLabel = 1;
					break;
				case "GNL":
					classLabel = 2;
					break;
				case "LSF":
					classLabel = 3;
					break;
				case "LSN":
					classLabel = 4;
					break;
				case "SIN":
					classLabel = 5;
					break;
				case "SHF":
					classLabel = 5;
					break;
				case "SIF":
					classLabel = 6;
					break;
				case "SHN":
					classLabel = 6;
					break;
				case "SMS":
					classLabel = 7;
					break;
				case "SRF":
					classLabel = 8;
					break;
				case "SRN":
					classLabel = 9;
					break;
				}
				csvOutput.write(Integer.toString(classLabel));

				// System.out.println(sequenceList.get(i + 298).getData());

				// System.out.println(sequenceList.get(i).getData().size());
				/*
				 * for (int j = 0; j < 3600; j++) {
				 * csvOutput.write(Double.toString(sequenceList.get(i)
				 * .getData().get(j))); }
				 */
				String v = sequenceList.get(i).getData().toString();
				v = v.replace("[", "");
				v = v.replace("]", "");
				// System.out.println(v);

				String f = sequenceList.get(i + 298).getData().toString();
				f = f.replace("[", "");
				f = f.replace("]", "");
				// System.out.println(f);
				String[] vArr = v.split(",");
				String[] fArr = f.split(",");
				for (int k = 0; k < vArr.length; k++)
					csvOutput.write(vArr[k]);
				for (int k = 0; k < fArr.length; k++)
					csvOutput.write(fArr[k]);
				// csvOutput.write(v);
				// csvOutput.write(f);

				csvOutput.endRecord();

			}
			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * readAllCsvFiles() Method
	 * 
	 * This is the first method call my Main. It will read through all the files
	 * in a given folder and pass each one of them to readSingleCsvFile to
	 * extract all the data
	 */
	private void readAllCsvFiles() {
		// the folder to read from
		// CHANGE TO THE FOLDER THAT CONTAINS THE CSV FILES ON YOUR MACHINE
		// THESE FILES SHOULD CONTAIN THE SEQUENCES FOR ALL THE PMU'S
		File folder = new File("/Users/btp97/Desktop/TEMPCSV");
		// array of all files in folder
		File[] listOfFiles = folder.listFiles();

		// for loop to walk through each file
		for (File file : listOfFiles) {
			if (file.isFile()) {
				// File file = listOfFiles[i];
				System.out.println(file.getName());
				// split out event type
				String[] arr = file.getName().split("_");
				String eventType = arr[0];
				// get area id out of file name
				String area_id = arr[1];
				area_id = area_id.replaceAll("[^0-9]", "");
				System.out.println(eventType);
				System.out.println(area_id);
				// pass to method readSingleCsv to get all the data from the
				// current file
				readSingleCsv(file.getName(), eventType, area_id);
				// outputToCsv();
				// after each single file is read callinsertSequenceData
				// to add each csv file data to the database
				try {
					insertSequenceData();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		// once all the csv files have been read close the connection to the
		// database
		if (dbConnection != null) {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * createTableStatement() Method
	 * 
	 * Used to create the table in the database.
	 */
	@SuppressWarnings("unused")
	private void createTableStatement() {
		Connection dbConnection = null;
		Statement stmt = null;
		try {
			dbConnection = DriverManager.getConnection(dbConf.url, dbConf.userName, dbConf.password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String createTableSQL = "CREATE TABLE SEQUENCES " + "(bus_number INTEGER, " + "area_id INTEGER, "
				+ "type CHAR(1), " + "event_type CHAR(3), " + "seq_id VARCHAR(32), " + "file_name VARCHAR(64), "
				+ "data MEDIUMTEXT)";

		try {

			stmt = dbConnection.createStatement();
			stmt.executeUpdate(createTableSQL);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
