package data.simulation.back;

import java.util.ArrayList;
import java.util.List;

/**
 * The Sequence class sets up each individual sequence of data and holds all the
 * variables related to the sequence.
 * 
 * This class is used to initialize all the data in the database from the raw
 * csv files.
 * 
 * Note: Not used during running of the applet
 *
 * @author Brett Pelkey
 * @date 7-14-15
 */

public class Sequence {

	private int busNumber;// unique number of bus 1-298
	private int areaId;
	private char type;// type of sequence V for voltage and F for Frequency
	private String eventType;// type of event ex. FLT = fault
	private String seqId;// unique sequence id e.g A10_EFLT_S1
	private String fileName;// name of file the data came from
	private List<Double> data;// list to hold the actual sequence

	// empty constructor to set up array list
	/**
	 * Empty constructor that will set up the array list
	 */
	public Sequence() {
		this.data = new ArrayList<Double>();
	}

	/**
	 * Contructor that contains all class variables
	 *
	 * @param busNumber
	 *            the number of the bus 1-298
	 * @param areaId
	 *            the area the bus is in
	 * @param type
	 *            the type of data V or F
	 * @param eventType
	 *            the type of event e.g. FLT
	 * @param seqId
	 *            the unique sequence id
	 * @param fileName
	 *            the name of file the sequence came from
	 * @param data
	 *            the sequence data (A string of 3600 doubles seperated by
	 *            comma)
	 */
	public Sequence(int busNumber, int areaId, char type, String eventType, String seqId, String fileName,
			List<Double> data) {
		super();
		this.busNumber = busNumber;
		this.areaId = areaId;
		this.type = type;
		this.eventType = eventType;
		this.seqId = seqId;
		this.fileName = fileName;
		this.data = new ArrayList<Double>();
	}

	/**
	 * Contructor that will initialize all class variables except the array list
	 * Just sets up an empty array list to work with later
	 * 
	 * @param busNumber
	 *            the number of the bus 1-298
	 * @param areaId
	 *            the area the bus is in
	 * @param type
	 *            the type of data V or F
	 * @param eventType
	 *            the type of event e.g. FLT
	 * @param seqId
	 *            the unique sequence id
	 * @param fileName
	 *            the name of file the sequence came from
	 */
	public Sequence(int busNumber, int areaId, char type, String eventType, String seqId, String fileName) {
		super();
		this.busNumber = busNumber;
		this.areaId = areaId;
		this.type = type;
		this.eventType = eventType;
		this.seqId = seqId;
		this.fileName = fileName;
		this.data = new ArrayList<Double>();
	}

	// constructor with all class variables except ArrayList and seqId
	/**
	 * Constructor that will initialize all class variables except for array
	 * list and seqId
	 * 
	 * @param busNumber
	 *            the number of the bus 1-298
	 * @param areaId
	 *            the area the bus is in
	 * @param type
	 *            the type of data V or F
	 * @param eventType
	 *            the type of event e.g. FLT
	 * @param fileName
	 *            the name of file the sequence came from
	 */
	public Sequence(int busNumber, int areaId, char type, String eventType, String fileName) {
		super();
		this.busNumber = busNumber;
		this.areaId = areaId;
		// this.seqId = 0;
		this.type = type;
		this.eventType = eventType;
		this.fileName = fileName;
		this.data = new ArrayList<Double>();
	}

	/*
	 * Start of getters and setters
	 */
	/**
	 * Gets the bus number of the bus
	 * 
	 * @return the bus number
	 */
	public int getBusNumber() {
		return busNumber;
	}

	/**
	 * Sets the bus number
	 * 
	 * @param busNumber
	 *            the bus number to set
	 */
	public void setBusNumber(int busNumber) {
		this.busNumber = busNumber;
	}

	/**
	 * Gets the type of data (V or F) F=Frequency V=Voltage
	 * 
	 * @return the type (V of F)
	 */
	public String getType() {
		return String.valueOf(type);
	}

	/**
	 * Sets the type of data (V or F)
	 * 
	 * @param type
	 *            the type of data to set
	 */
	public void setType(char type) {
		this.type = type;
	}

	/**
	 * Gets the event type of sequence (e.g. FLT)
	 * 
	 * @return the event type of sequence
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Set the event type of sequence (e.g. FLT)
	 * 
	 * @param eventType
	 *            the type of event to set the sequence to
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * Gets the unique sequence id of sequence
	 * 
	 * @return the unique sequence id
	 */
	public String getSeqId() {
		return seqId;
	}

	/**
	 * Sets the unique sequence id of the sequence
	 * 
	 * @param seqId
	 *            the sequence id to set the sequence to
	 */
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}

	/**
	 * Gets the file name the sequence came from
	 * 
	 * @return the name of the file the sequence came from
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name the sequence came from
	 * 
	 * @param fileName
	 *            the name of the file to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the data for the sequence...will contain 3600 values
	 * 
	 * @return a List of doubles with all the sequence data
	 */
	public List<Double> getData() {
		return data;
	}

	/**
	 * Sets the data for the sequence...will contain 3600 values
	 * 
	 * @param data
	 *            the List of doubles to set the data to
	 */
	public void setData(List<Double> data) {
		this.data = data;
	}

	/**
	 * Gets the area id of the sequence
	 * 
	 * @return the area id of the sequence
	 */
	public int getAreaId() {
		return areaId;
	}

	/**
	 * Sets the area id of the sequence
	 * 
	 * @param areaId
	 *            the area id to set the sequence to
	 */
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	/*
	 * End of getters and setters
	 */
	@Override
	public String toString() {
		return "Sequence [busNumber=" + busNumber + ", areaId=" + areaId + ", type=" + type + ", eventType=" + eventType
				+ ", seqId=" + seqId + ", fileName=" + fileName + ", data=" + data + "]";
	}

}
