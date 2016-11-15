package API;

/**
 * Simple model for the Sensor.
 * 
 * @author ZackRauen
 * @version 1.0
 * @see Simulation
 * @see SimulationSetup
 *
 */

public class Sensor {
	private Complex alphaVal;
	private Complex hVal;
	private Complex nVal;
	private int sensorID;

	Sensor(int id, Complex alpha, Complex h, Complex n) {
		sensorID=id;
		alphaVal=alpha;
		hVal=h;
		nVal=n;
	}

	public int getID() { return sensorID; }
	public Complex getAlpha() { return alphaVal; }
	public Complex getHVal() { return hVal; }
	public Complex getNVal() { return nVal; }

	public void setID(int id) { sensorID = id; }
	public void setAlpha(Complex alpha) { alphaVal=alpha; }
	public void setHVal(Complex h) { hVal=h; }
	public void setNVal(Complex n) { nVal=n; }
	

}
