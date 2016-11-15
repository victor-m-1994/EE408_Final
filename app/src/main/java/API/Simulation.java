package API;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This is a simplified version of the Simulation, it has since been re-written
 * using a more complicated Complex class. This file should have been provided 
 * to you with the accompanying documentation.
 * 
 * @author ZackRauen
 * @version 1.4
 * @see SimulationManager
 * @see SimulationSetup
 *
 */

public class Simulation {
	
	private final int re=0;
	private final int im=1;
	
	private List<Complex> alphaVals;
	private List<Complex> hVals;
	private Complex vVal = new Complex();
	private Complex yVal = new Complex();
	private List<Complex> nVals;
	private Complex thetaHat = new Complex();
	private List<Sensor> sensorList;

	private SimulationSetup setup;

    private Random rand = new Random();

	public Simulation(SimulationSetup setup) {
		this.setup = setup;
		this.runSimulation();
	}


	private void runSimulation() {
       generateVValue();
       buildSensorList();
       generateYValue();
       generateThetaHat();
	}

	private void buildSensorList() {
	       generateNValues();
	       generateHValues();
	       generateAlphaValues();
	       sensorList = new ArrayList<Sensor>();
		for (int i=0;i<setup.getSensorCount();i++) {
			sensorList.add(new Sensor(i+1,alphaVals.get(i),hVals.get(i),nVals.get(i)));
		}
	}

	private void generateAlphaValues() {
	       if (!setup.isRician()) {
	    	   Double uniformAlpha;
	    	   uniformAlpha= Math.sqrt(setup.getPower() / setup.getSensorCount());
	    	   alphaVals = new ArrayList<Complex>();
	   			for (int i=0;i<setup.getSensorCount();i++) {
					alphaVals.add(new Complex(uniformAlpha,0));
	   			}
	       }
	       else {
	    	   Double summation = 0d;
	    	   for (int i=0;i<setup.getSensorCount();i++) {
	    		   summation+=Math.pow(Math.sqrt(hVals.get(i).getMagnitude()/(1+(setup.getPower()*Math.pow(hVals.get(i).getMagnitude(), 2))*setup.getVarianceN())), 2);
	    	   }
	    	   Double alphaMag = 0d;
	    	   alphaVals = new ArrayList<Complex>();
	   			for (int i=0;i<setup.getSensorCount();i++) {
	   				alphaVals.add(new Complex());
					alphaMag=(Math.sqrt(setup.getPower()/summation)*(hVals.get(i).getMagnitude()/(1+(setup.getPower()*Math.pow(hVals.get(i).getMagnitude(), 2))*setup.getVarianceN())));
		   			alphaVals.get(i).setReal(alphaMag*Math.cos(-1*hVals.get(i).getPhase()));
		   			alphaVals.get(i).setImaginary(alphaMag*Math.sin(-1*hVals.get(i).getPhase()));
	   			}
	       }
	}

	private void generateHValues() {
	   hVals = new ArrayList<Complex>();

	       if (!setup.isRician()) {
	   			for (int i=0;i<setup.getSensorCount();i++) {
	   				hVals.add(new Complex(1,0));
	   			}
	       }
	       else {
	   			for (int i=0;i<setup.getSensorCount();i++) {
	   			Double Variance = new Double(1/(setup.getK()+1));
	   			Double Mean = new Double(Math.sqrt((setup.getK())/(setup.getK()+1)));
	   			Double randomReal = (rand.nextBoolean()) ? rand.nextGaussian() : rand.nextGaussian();
	   			Double randomIm = (rand.nextBoolean()) ? rand.nextGaussian() : rand.nextGaussian();
				hVals.add(new Complex(Math.sqrt(Variance/2)*randomReal+Mean, Math.sqrt(Variance/2)*randomIm+Mean));
	   			}
	       }
	}

	private void generateNValues() {
	 	   nVals = new ArrayList<Complex>();


		for (int i=0;i<setup.getSensorCount();i++) {
			nVals.add(new Complex(Math.sqrt(setup.getVarianceN())*rand.nextGaussian(),Math.sqrt(setup.getVarianceN())*rand.nextGaussian()));
		}

	}

	private void generateVValue() {
			vVal.setValues(Math.sqrt(setup.getVarianceV())*rand.nextGaussian(),Math.sqrt(setup.getVarianceV())*rand.nextGaussian());
	}

	private void generateYValue() {
		Complex summations = new Complex();

	    if (!setup.isRician()) {
			for (int i=0;i<setup.getSensorCount();i++) {
				summations.setReal(summations.getReal() + (setup.getTheta()+nVals.get(i).getReal())*(alphaVals.get(i).getReal()*hVals.get(i).getReal())-(nVals.get(i).getImaginary())*(alphaVals.get(i).getReal()*hVals.get(i).getImaginary()));
				summations.setImaginary(summations.getImaginary() + (setup.getTheta()+nVals.get(i).getReal())*(alphaVals.get(i).getReal()*hVals.get(i).getImaginary())+(nVals.get(i).getImaginary())*(alphaVals.get(i).getReal()*hVals.get(i).getReal()));
			}
	    }
	    else {
			for (int i=0;i<setup.getSensorCount();i++) {
				summations.setReal(summations.getReal() + (setup.getTheta()+nVals.get(i).getReal())*((alphaVals.get(i).getReal()*hVals.get(i).getReal())-(alphaVals.get(i).getImaginary()*hVals.get(i).getImaginary()))-((nVals.get(i).getImaginary())*((alphaVals.get(i).getReal()*hVals.get(i).getImaginary())+(alphaVals.get(i).getImaginary()*hVals.get(i).getReal()))));
				summations.setImaginary(summations.getImaginary() + (setup.getTheta()+nVals.get(i).getReal())*((alphaVals.get(i).getReal()*hVals.get(i).getImaginary())+(alphaVals.get(i).getImaginary()*hVals.get(i).getReal()))-((nVals.get(i).getImaginary())*((alphaVals.get(i).getReal()*hVals.get(i).getReal())+(alphaVals.get(i).getImaginary()*hVals.get(i).getImaginary()))));
			}
	    }
		yVal.setValues(summations.getReal()+vVal.getReal(), summations.getImaginary()+vVal.getImaginary());
	}

	private void generateThetaHat() {
		double [] summations = new double[2];
		summations[0]=0;
		summations[1]=0;
		if (!setup.isRician()) {
			for (int i=0;i<setup.getSensorCount();i++) {
			summations[re]+=alphaVals.get(i).getReal()*hVals.get(i).getReal();
			summations[im]+=alphaVals.get(i).getReal()*hVals.get(i).getImaginary();
			}
		}
		else {
			for (int i=0;i<setup.getSensorCount();i++) {
				summations[re]+=(alphaVals.get(i).getReal()*hVals.get(i).getReal())-(alphaVals.get(i).getImaginary()*hVals.get(i).getImaginary());
				summations[im]+=(alphaVals.get(i).getReal()*hVals.get(i).getImaginary())+(alphaVals.get(i).getImaginary()*hVals.get(i).getReal());
			}
		}
		thetaHat.setReal((yVal.getReal()*summations[re]+summations[im]*yVal.getImaginary())/(Math.pow(summations[re], 2)-Math.pow(summations[im], 2)));
		thetaHat.setImaginary((-1)*(yVal.getReal()*summations[im]+summations[re]*yVal.getReal())/(Math.pow(summations[re], 2)-Math.pow(summations[im], 2)));

	}


	/**
	 * @return the vVal
	 */
	public Complex getVVal() {
		return vVal;
	}


	/**
	 * @return the yVal
	 */
	public Complex getYVal() {
		return yVal;
	}

	/**
	 * @return the thetaHat
	 */
	public Complex getThetaHat() {
		return thetaHat;
	}

	/**
	 * @return the sensorList
	 */
	public List<Sensor> getSensorList() {
		return sensorList;
	}

	public Sensor getSensor(int i) {
		return sensorList.get(i);
	}


	/**
	 * @return the setup
	 */
	public SimulationSetup getSetup() {
		return setup;
	}


	public void sortSensorsByNumber(final boolean descending) {
		Collections.sort(sensorList, new Comparator<Sensor>() {

			@Override
			public int compare(Sensor a, Sensor b) {
				if (a.getID() > b.getID())
					return descending ? -1 : 1;
				else if (a.getID() < b.getID())
					return descending ? 1 : -1;
				else
					return 0;
			}

		});
	}

	public void sortSensorsByAlpha(final boolean descending) {
		Collections.sort(sensorList, new Comparator<Sensor>() {

			@Override
			public int compare(Sensor a, Sensor b) {
				if (a.getAlpha().getMagnitude() > b.getAlpha().getMagnitude())
					return descending ? -1 : 1;
				else if (a.getAlpha().getMagnitude() < b.getAlpha().getMagnitude())
					return descending ? 1 : -1;
				else
					return 0;
			}

		});
	}

	public void sortSensorsByH(final boolean descending) {
		Collections.sort(sensorList, new Comparator<Sensor>() {

			@Override
			public int compare(Sensor a, Sensor b) {
				if (a.getHVal().getMagnitude() > b.getHVal().getMagnitude())
					return descending ? -1 : 1;
				else if (a.getHVal().getMagnitude() < b.getHVal().getMagnitude())
					return descending ? 1 : -1;
				else
					return 0;
			}

		});
	}

	public void sortSensorsByN(final boolean descending) {
		Collections.sort(sensorList, new Comparator<Sensor>() {

			@Override
			public int compare(Sensor a, Sensor b) {
				if (a.getNVal().getMagnitude() > b.getNVal().getMagnitude())
					return descending ? -1 : 1;
				else if (a.getNVal().getMagnitude() < b.getNVal().getMagnitude())
					return descending ? 1 : -1;
				else
					return 0;
			}

		});
	}
    
    

}
