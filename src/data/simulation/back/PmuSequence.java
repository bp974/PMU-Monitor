package data.simulation.back;

import java.util.List;

/**
 * The PmuSequence class sets up each individual sequence of data and holds all
 * the variables related to the sequence and the PMU that the sequence came
 * from.
 * 
 * This class is called when getting the data from the database that will
 * displayed on the tool.
 *
 * @author Brett Pelkey
 * @date 7-29-15
 */
public class PmuSequence {

	private int busNumber;// number of bus 1-298
	private int areaId;// id of area
	private String areaName;// name of area
	private int zoneId;// id of zone
	private String pmuName;// name of pmu
	private int busId;// id of bus
	private String state;// state the pmu lies in
	private List<Double> data;// sequence data
	private String type;// F or V for type
	private String eventType;// e.g. Fault FLT
	private String fileName;// name of file

	/**
	 * Empty constructor
	 */
	public PmuSequence() {
		super();
	}

	/**
	 * Constructor to set up the data for both the PMU and Sequence for each
	 * individual sequence that will be displayed in the applet
	 * 
	 * @param busNumber
	 *            the number of the bus 1-298
	 * @param areaId
	 *            the area id of the bus
	 * @param areaName
	 *            the area name that the bus is in
	 * @param zoneId
	 *            the zone id that the bus is in
	 * @param pmuName
	 *            the name of the PMU
	 * @param busId
	 *            the id of the bus
	 * @param state
	 *            the state that the bus is in
	 * @param data
	 *            the data sequence
	 * @param type
	 *            the data type (V or F)
	 * @param eventType
	 *            the event type (e.g. FLT)
	 * @param fileName
	 *            the name of the file that the sequence came from
	 */
	public PmuSequence(int busNumber, int areaId, String areaName, int zoneId, String pmuName, int busId, String state,
			List<Double> data, String type, String eventType, String fileName) {
		super();
		this.busNumber = busNumber;
		this.areaId = areaId;
		this.areaName = areaName;
		this.zoneId = zoneId;
		this.pmuName = pmuName;
		this.busId = busId;
		this.state = state;
		this.data = data;
		this.type = type;
		this.eventType = eventType;
		this.fileName = fileName;
	}

	/*
	 * start of getters and setters
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

	/**
	 * Gets the area name that the bus is in
	 * 
	 * @return the name of the area
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * Sets the area name that the bus is in
	 * 
	 * @param areaName
	 *            the name of the area
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * Gets the zone id that the bus is in
	 * 
	 * @return the zone id
	 */
	public int getZoneId() {
		return zoneId;
	}

	/**
	 * Sets the zone id that the bus is in
	 * 
	 * @param zoneId
	 *            the zone id to set
	 */
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	/**
	 * Gets the name of the PMU
	 * 
	 * @return the name of the PMU
	 */
	public String getPmuName() {
		return pmuName;
	}

	/**
	 * Sets the name of the PMU
	 * 
	 * @param pmuName
	 *            the PMU name to set
	 */
	public void setPmuName(String pmuName) {
		this.pmuName = pmuName;
	}

	/**
	 * Gets the bus id
	 * 
	 * @return the bus id
	 */
	public int getBusId() {
		return busId;
	}

	/**
	 * Sets the bus id
	 * 
	 * @param busId
	 *            the bus id
	 */
	public void setBusId(int busId) {
		this.busId = busId;
	}

	/**
	 * Gets the state the PMU is in
	 * 
	 * @return the state the PMU is in
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state the PMU is in
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
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
	 * Gets the type of data (V or F) F=Frequency V=Voltage
	 * 
	 * @return the type (V of F)
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of data (V or F)
	 * 
	 * @param type
	 *            the type of data to set
	 */
	public void setType(String type) {
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

	/*
	 * end of getters and setters
	 */

	@Override
	public String toString() {
		return "PmuSequence [busNumber=" + busNumber + ", areaId=" + areaId + ", areaName=" + areaName + ", zoneId="
				+ zoneId + ", pmuName=" + pmuName + ", busId=" + busId + ", state=" + state + ", type=" + type
				+ ", eventType=" + eventType + ", fileName=" + fileName + ", data=" + data + "]";
	}

}
