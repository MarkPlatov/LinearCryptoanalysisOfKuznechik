package utils;

public class ApproximationPair {
	public byte alpha;
	public byte beta;
	public byte[] bigAlpha;
	public byte[] bigBeta;
	public double epsilon;
	public int numberOfEquals;
	public String deviation;
	public String powerOfTwo;
	
	public ApproximationPair() {
	}
	
	public ApproximationPair(byte[] bigAlpha, byte[] bigBeta, double epsilon, int numberOfApp) {
		this.bigAlpha = bigAlpha;
		this.bigBeta = bigBeta;
		this.epsilon = epsilon;
		this.powerOfTwo = "2^" + (numberOfApp - 1);
	}
	
	public ApproximationPair(byte alpha, byte beta, double epsilon, int numberOfEquals) {
		this.alpha = alpha;
		this.beta = beta;
		this.epsilon = epsilon;
		this.numberOfEquals = numberOfEquals;
		int num = numberOfEquals;
		if (num < 128) {
			num = 128 - num;
			this.deviation = "- " + num;
		} else if ((num > 128)){
			num = num - 128;
			this.deviation = "\"+ " + num +"\"";
		} else {
			this.deviation = "0";
		}
	}

	@Override
	public String toString() {
		return "ApproximationPair{" + ", \n" +
//				"   in =        " + in + ", \n" +
				"   alpha =             " + Utils.toBinaryString(alpha) + ", \n" +
//				"\n" +
//				"   out =       " + out + ", \n" +
				"   beta =              " + Utils.toBinaryString(beta) + ", \n" +
				"   epsilon =           " + epsilon + ", \n" +
				"   numberOfEquals =    " + numberOfEquals + ", \n" +
				"   deviation =         " + deviation + ", \n" +
				'}';
	}
	
	public String toTable(){
		return "" +
				Utils.toBinaryString(alpha) + "|" +
				Utils.toBinaryString(beta) + "|" +
				epsilon + "|" +
				numberOfEquals + "|" +
				deviation + "|";
	}
	public String toTableBigApproximation(){
		return "" +
				Utils.byteArrToHexStr(bigAlpha) + "|" +
				Utils.byteArrToHexStr(bigBeta) + "|" +
				powerOfTwo + "*" + epsilon + "|";
	}
	
}
