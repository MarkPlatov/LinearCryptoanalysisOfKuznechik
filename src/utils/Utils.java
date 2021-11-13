package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Utils {
	
	public static void log(String text){
		String file = Constants.FILE_NAME + "/log.txt";
		writeToFile(text, file);
	}
	
	public static void writeToFile(String text, String file){
		try(FileOutputStream fos=new FileOutputStream(file, true))
		{
			// перевод строки в байты
			byte[] buffer = text.getBytes();
			fos.write(buffer, 0, buffer.length);
			fos.flush();
		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
	}
	
	public static String byteArrToHexStr(byte[] a, boolean printByteWord){
		if (a == null) return "null";
		String s = "{";
		String byteWord = printByteWord ? "(byte) " : "";
		for (int i = 0; i < a.length - 1; i++) {
			String leadingZero = (a[i] < 16 && a[i] > -1) ? "0" : "";
			s = String.format("%s%s0x%s%x, ", s, byteWord, leadingZero, a[i]);
		}
		String leadingZero = (a[a.length-1] < 16 && a[a.length-1] > -1) ? "0" : "";
		s = String.format("%s%s0x%s%x}", s, byteWord, leadingZero, a[a.length-1]);
		return s;
	}
	public static String byteArrToHexStr(byte[] a) {
		return byteArrToHexStr(a, false);
	}
	
	public static void printArrayOfHexArrays(byte[][] arr){
		for (byte[] a : arr) { System.out.println(Utils.byteArrToHexStr(a)); }
	}
	
	public static String byteArrayOfArraysAsHexArraysStr(byte[][] arr){
		String s = "";
		for (byte[] a : arr) {
			s = String.format("%s\n%s", s, Utils.byteArrToHexStr(a));
		}
		return s;
	}
	
	public static byte getRandByte(){
		Random rnd = new Random();
		return (byte) rnd.nextInt();
	}
}
