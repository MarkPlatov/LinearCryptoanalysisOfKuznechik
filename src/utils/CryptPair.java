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
	public CryptPair() {
		this.in = null;
		this.onPreviousRound = null;
		this.out = null;
		this.rounds = 666;
		this.iterKeys = null;
	}
	
	@Override
	public String toString() {
		return "CryptPair{" +
				"\n     in =              " + Utils.byteArrToHexStr(in) +
				",\n    onPreviousRound = " + Utils.byteArrToHexStr(onPreviousRound) +
				",\n    out =             " + Utils.byteArrToHexStr(out) +
				",\n    rounds =          " + rounds +
				",\n    iterKeys = {\n" + Utils.byteArrayOfArraysAsHexArraysStr(iterKeys) +
				"\n}" +
				"\n}";
	}
	
	public void addToFile(String fileName){
		String text = this.toString() + "\n"; // строка для записи
		Utils.writeToFile(text, fileName);
//		try(FileOutputStream fos=new FileOutputStream(fileName, true))
//		{
//			// перевод строки в байты
//			byte[] buffer = text.getBytes();
//			fos.write(buffer, 0, buffer.length);
//		}
//		catch(IOException ex){
//			System.err.println(ex.getMessage());
//		}
//		System.out.println("The file has been written");
	}
}
