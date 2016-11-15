package API;

import java.text.DecimalFormat;

/**
 * 
 * This version has been minimized to keep things simple.
 * 
 * @author ZackRauen
 * @version 0.5
 *
 */

public class Complex {
	
	private double realPart = 0;
	private double imaginaryPart = 0;
	
	public Complex() {
		realPart=0;
		imaginaryPart=0;
	}

	public Complex(double real, double im) {
		realPart=real;
		imaginaryPart=im;
	}
	
	public double getReal() { return realPart; }
	public double getImaginary() { return imaginaryPart; }
	public double getMagnitude() { return Math.sqrt((realPart*realPart)+(imaginaryPart*imaginaryPart)); }
	public double getPhase() {
		if (realPart == 0) {
			return Math.PI/2;
		}
		else {
			return Math.atan2(imaginaryPart,realPart);
		}
	}
	
	public void setReal(double num) { realPart=num; }
	public void setImaginary(double num) { imaginaryPart=num; }
	
	public void setValues(double real, double im) {
		realPart=real;
		imaginaryPart=im;
	}
	
	@Override
	public String toString() {
		return realPart + " + " + imaginaryPart + "j";
	}
	
	public String toFormattedString() {
    	DecimalFormat two = new DecimalFormat("#.##");
    	if (realPart==0)
    		return two.format(imaginaryPart) + "j";
    	else if (imaginaryPart==0)
    		return two.format(realPart);
    	else
    		return two.format(realPart) + " + " + two.format(imaginaryPart) + "j";
	}

}
