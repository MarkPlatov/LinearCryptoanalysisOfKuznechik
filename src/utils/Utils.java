package utils;

public class Utils {
	public static String byteArrToHexStr(byte[] a, boolean printByteWord){
		String s = "{";
		String byteWord = printByteWord ? "(byte) " : "";
		for (int i = 0; i < 15; i++) s = String.format("%s%s%#x, ", s, byteWord, a[i]);
		s = String.format("%s%s%#x}", s, byteWord, a[15]);
		return s;
	}
}
