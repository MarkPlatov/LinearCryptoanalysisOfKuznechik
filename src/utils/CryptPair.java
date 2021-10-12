package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CryptPair {
	public byte[] in;
	public byte[] out;
	public int rounds;
	public byte[][] iterKeys;
	
	public CryptPair(byte[] in, byte[] out, int rounds, byte[][] iterKeys) {
		this.in = in;
		this.out = out;
		this.rounds = rounds;
		this.iterKeys = iterKeys;
	}
	public CryptPair() {
		this.in = null;
		this.out = null;
		this.rounds = 666;
		this.iterKeys = null;
	}
	
	@Override
	public String toString() {
		return "CryptPair{" +
				"in=" + Arrays.toString(in) +
				", out=" + Arrays.toString(out) +
				", rounds=" + rounds +
				", iterKeys=" + Arrays.toString(iterKeys) +
				'}';
	}
	
	public void addToFile(String fileName){
		String text = this.toString() + "\n"; // строка для записи
		try(FileOutputStream fos=new FileOutputStream(fileName, true))
		{
			// перевод строки в байты
			byte[] buffer = text.getBytes();
			fos.write(buffer, 0, buffer.length);
		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
//		System.out.println("The file has been written");
	}
}
