package utils;

import java.util.Random;

public class Utils {
	
	
	
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
