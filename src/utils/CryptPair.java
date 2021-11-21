package utils;

public class CryptPair {
	public byte[] in;
	public byte[] onPreviousRound;
	public byte[] out;
	public int rounds;
	public byte[][] iterKeys;
	
	public CryptPair(
			byte[] in,
			byte[] onPreviousRound,
			byte[] out,
			int rounds,
			byte[][] iterKeys) {
		this.in = in;
		this.onPreviousRound = onPreviousRound;
		this.out = out;
		this.rounds = rounds;
		this.iterKeys = iterKeys;
	}
	
	@Override
	public String toString() {
		return "CryptPair{\n" +
				"    in =              " + Utils.byteArrToHexStr(in) + ",\n" +
				"    onPreviousRound = " + Utils.byteArrToHexStr(onPreviousRound) + ",\n" +
				"    out =             " + Utils.byteArrToHexStr(out) + ",\n" +
				"    rounds =          " + rounds + ",\n" +
				"    iterKeys = ,\n" +
				"{\n" +
				Utils.byteArrayOfArraysAsHexArraysStr(iterKeys) + ",\n" +
				"    }" + "\n" +
				"\n}";
	}
	
	public void addToFile(String fileName){
		String text = this + "\n"; // строка для записи
		Utils.writeToFile(text, fileName);
	}
}
