package data.simulation.back;

/**
 * The PMU class sets up each individual PMU with all the variables related to
 * the PMU
 * 
 * Strictly used to build a PMU object from raw csv files to insert into
 * database
 * 
 * Note: Not used during running of the applet
 *
 * @author Brett Pelkey
 * @date 7-14-15
 */
public class PMU {

	private int busNumber;// number of bus 1 - 298
	private int areaId;// id of area
	private String areaName;// name of area
	private int zoneId;// id of zone
	private String pmuName;// name of pmu
	private int busId;// id of bus
	private String state;// state the pmu lies in

	/**
	 * Empty Constructor
	 * 
	 */
	public PMU() {

	}

	/**
	 * Constructor for a PMU with all class variables
	 * 
	 * @param busNumber
	 *            the number of the bus 1 - 298
	 * @param areaId
	 *            the id of the area that the bus is in
	 * @param areaName
	 *            name of the area
	 * @param zoneId
	 *            the id of the zone that the bus is in
	 * @param pmuName
	 *            the name of the PMU
	 * @param busId
	 *            the id of the bus
	 * @param state
	 *            the state that the PMU is in
	 */
	public PMU(int busNumber, int areaId, String areaName, int zoneId, String pmuName, int busId, String state) {
		super();
		this.busNumber = busNumber;
		this.areaId = areaId;
		this.areaName = areaName;
		this.zoneId = zoneId;
		this.pmuName = pmuName;
		this.busId = busId;
		this.state = state;
	}

	/*
	 * Start of getters and setters
	 */

	/**
	 * Gets the number of the bus
	 * 
	 * @return the bus number
	 */
	public int getBusNumber() {
		return busNumber;
	}

	/**
	 * Sets the number of the bus
	 * 
	 * @param busNumber
	 *            the number to set the bus to
	 */
	public void setBusNumber(int busNumber) {
		this.busNumber = busNumber;
	}

	/**
	 * Gets the area id of the bus
	 * 
	 * @return the area id of the bus
	 */
	public int getAreaId() {
		return areaId;
	}

	/**
	 * Sets the area id of the bus
	 * 
	 * @param areaId
	 *            the area id to set the bus to
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

	/*
	 * End of getters and setters
	 */
	@Override
	public String toString() {
		return "PMU [busNumber=" + busNumber + ", areaId=" + areaId + ", areaName=" + areaName + ", zoneId=" + zoneId
				+ ", pmuName=" + pmuName + ", busId=" + busId + ", state=" + state + "]";
	}

}
