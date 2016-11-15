package API;

import java.util.ArrayList;

/**
 * This is the generic SimulationSetup. This file should have been provided to you with the accompanying
 * documentation.
 * 
 * @author ZackRauen
 * @version 1.0
 * @see Simulation
 * @see SimulationManager
 *
 */
public class SimulationSetup {

	final static String DEFAULT_OBSERVATION = "Temperature";
	final static double DEFAULT_THETA = 65;
	final static int DEFAULT_SENSOR_COUNT = 20;
	final static double DEFAULT_POWER = 1d;
	final static double DEFAULT_N = 5d;
	final static double DEFAULT_V = 1d;
	final static Boolean DEFAULT_RICIAN = true;
	final static double DEFAULT_K = 1.5d;
	final static Boolean DEFAULT_UNIFORM = false;


	private String observationName;
	private double theta;
	private int sensorCount;
	private double powerValue;
	private double nVariance;
	private double vVariance;
	private double kValue;
	private Boolean uniform;
	private Boolean rician;
	
	private ArrayList<SetupListener> listeners = new ArrayList<>();
	
	
	/**
	 * 
	 */
	public SimulationSetup() {
		this.newSetup(DEFAULT_OBSERVATION, DEFAULT_THETA, DEFAULT_SENSOR_COUNT, DEFAULT_POWER, DEFAULT_N, DEFAULT_V, DEFAULT_K, DEFAULT_UNIFORM, DEFAULT_RICIAN);
	}
	
	/**
	 * @param observationName
	 * @param theta
	 * @param numberOfSensors
	 * @param powerValue
	 * @param nVariance
	 * @param vVariance
	 * @param kValue
	 * @param uniform
	 * @param rician
	 */
	public SimulationSetup(String observationName, double theta, int numberOfSensors, double powerValue, double nVariance, double vVariance, double kValue, Boolean uniform, Boolean rician) {
		this.newSetup(observationName, theta, numberOfSensors, powerValue, nVariance, vVariance, kValue, uniform, rician);
	}
	
	/**
	 * @param observationName
	 * @param theta
	 * @param numberOfSensors
	 * @param powerValue
	 * @param nVariance
	 * @param vVariance
	 * @param kValue
	 * @param uniform
	 * @param rician
	 */
	public void newSetup(String observationName, double theta, int numberOfSensors, double powerValue, double nVariance, double vVariance, double kValue, Boolean uniform, Boolean rician) {
		if (rician && uniform) 
			throw new SetupException(SetupException.ExceptionType.RICIAN_WITH_UNIFORM_ALPHA);
		if (numberOfSensors < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		if (powerValue < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		if (nVariance < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		if (vVariance < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		if (kValue < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		
		this.observationName = observationName;   
		this.theta = theta;
		this.sensorCount = numberOfSensors;
		this.powerValue = powerValue;
		this.nVariance = nVariance;
		this.vVariance = vVariance;
		this.kValue = kValue;
		this.uniform = uniform;
		this.rician = rician;
		
		this.alertListeners();
	}
	
    /**
     * @return C value
     */
    public double getC() {
    	double cValue = 19;
        if (uniform) {
        	cValue = (vVariance*powerValue+1)/powerValue;
            if (rician) {
                cValue = cValue*((kValue+1)/kValue);
            }
        }
        return cValue;
    }


	/**
	 * @return observation name
	 */
	public String getObservation() {
		return observationName;
	}


	/**
	 * @param observationName new name for the observation
	 */
	public void setObservation(String observationName) {
		this.observationName = observationName;
		this.alertListeners();
	}


	/**
	 * @return
	 */
	public double getTheta() {
		return theta;
	}


	/**
	 * @param theta
	 */
	public void setTheta(double theta) {
		this.theta = theta;
		this.alertListeners();
	}


	/**
	 * @return
	 */
	public int getSensorCount() {
		return sensorCount;
	}

	/**
	 * @param sensorCount - must be positive or else an error is thrown
	 */
	public void setSensorCount(int sensorCount) {
		if (sensorCount < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		this.sensorCount = sensorCount;
		this.alertListeners();
	}

	/**
	 * @return
	 */
	public double getPower() {
		return powerValue;
	}

	/**
	 * @param powerValue
	 */
	public void setPower(double powerValue) {
		if (powerValue < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		this.powerValue = powerValue;
		this.alertListeners();
	}

	/**
	 * @return
	 */
	public double getVarianceN() {
		return nVariance;
	}

	/**
	 * @param nVariance
	 */
	public void setVarianceN(double nVariance) {
		if (nVariance < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		this.nVariance = nVariance;
		this.alertListeners();
	}

	/**
	 * @return
	 */
	public double getVarianceV() {
		return vVariance;
	}

	/**
	 * @param vVariance
	 */
	public void setVarianceV(double vVariance) {
		if (vVariance < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		this.vVariance = vVariance;
		this.alertListeners();
	}
	

	/**
	 * @return
	 */
	public double getK() {
		return kValue;
	}

	/**
	 * @param kValue
	 */
	public void setK(double kValue) {
		if (kValue < 0)
			throw new SetupException(SetupException.ExceptionType.NEGATIVE_INPUT);
		this.kValue = kValue;
		this.alertListeners();
	}

	/**
	 * @return
	 */
	public Boolean isUniform() {
		return uniform;
	}
	
	/**
	 * @return
	 */
	public Boolean isOptimum() {
		return !uniform;
	}
	
	/**
	 * @param optimum - if this is false and the channels are Rician, will throw a SetupException
	 */
	public void setOptimum(Boolean optimum) {
		this.setUniform(!optimum);
	}

	/**
	 * @param uniform - if this is true and the channels are Rician, will throw a SetupException
	 */
	public void setUniform(Boolean uniform) {
		if (this.rician && uniform)
			throw new SetupException(SetupException.ExceptionType.RICIAN_WITH_UNIFORM_ALPHA);
		this.uniform = uniform;
		this.alertListeners();
	}

	/**
	 * @return if the channels are Rician
	 */
	public Boolean isRician() {
		return rician;
	}
	
	/**
	 * @return if the channels are AWGN
	 */
	public Boolean isAWGN() {
		return !rician;
	}
	
	/**
	 * @param awgn - if this is false, the alpha values are reset to optimum
	 */
	public void setAWGN(Boolean awgn) {
		this.setRician(!awgn);
	}

	/**
	 * @param rician - if this is true, the alpha values are reset to optimum
	 */
	public void setRician(Boolean rician) {
		if (rician && this.uniform) 
			this.uniform = false;
		this.rician = rician;
		this.alertListeners();
	}
	
	public double getSensingSNR() {
		return (theta*theta)/nVariance;
	}
	
	public double getChannelSNR() {
		return powerValue/nVariance;
	}
	
	/**
	 * @param listener SetupListener to add to the setup
	 */
	public void addListener(SetupListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * @param listener SetupListener to remove from the setup
	 */
	public void removeListener(SetupListener listener) {
		listeners.remove(listener);
	}

	private void alertListeners() {
		for (SetupListener l : listeners) {
			l.setupChanged();
		}
	}
	

}
