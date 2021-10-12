package utils;

import java.util.Random;

public class Utils {
	public static final byte[] EXAMPLE_KUZNECHIK_KEY = {
			(byte) 0x77, (byte) 0x66, (byte) 0x55, (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11, (byte) 0x00,
			(byte) 0xff, (byte) 0xee, (byte) 0xdd, (byte) 0xcc, (byte) 0xbb, (byte) 0xaa, (byte) 0x99, (byte) 0x88,
			(byte) 0xef, (byte) 0xcd, (byte) 0xab, (byte) 0x89, (byte) 0x67, (byte) 0x45, (byte) 0x23, (byte) 0x01,
			(byte) 0x10, (byte) 0x32, (byte) 0x54, (byte) 0x76, (byte) 0x98, (byte) 0xba, (byte) 0xdc, (byte) 0xfe,
	};
	
	
	public static final byte[] EXAMPLE_KUZNECHIK_DECRYPTED_TEXT = {
			(byte) 0x88, (byte) 0x99, (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff,
			(byte) 0x00, (byte) 0x77, (byte) 0x66, (byte) 0x55, (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11,
	};
	
	public static final byte[] EXAMPLE_KUZNECHIK_ENCRYPTED_TEXT = {
			(byte) 0xcd, (byte) 0xed, (byte) 0xd4, (byte) 0xb9, (byte) 0x42, (byte) 0x8d, (byte) 0x46, (byte) 0x5a,
			(byte) 0x30, (byte) 0x24, (byte) 0xbc, (byte) 0xbe, (byte) 0x90, (byte) 0x9d, (byte) 0x67, (byte) 0x7f,
	};
	
	
	public static String byteArrToHexStr(byte[] a, boolean printByteWord){
		String s = "{";
		String byteWord = printByteWord ? "(byte) " : "";
		for (int i = 0; i < 15; i++) s = String.format("%s%s%#x, ", s, byteWord, a[i]);
		s = String.format("%s%s%#x}", s, byteWord, a[15]);
		return s;
	}
	public static String byteArrToHexStr(byte[] a) {
		return byteArrToHexStr(a, false);
	}
	
	public static void printArrayOfHexArrays(byte[][] arr){
		for (byte[] a : arr) { System.out.println(Utils.byteArrToHexStr(a)); }
	}
	
	public static byte getRandByte(){
		Random rnd = new Random();
		return (byte) rnd.nextInt();
	}
}
