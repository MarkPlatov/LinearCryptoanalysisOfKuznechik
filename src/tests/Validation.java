package tests;

public class Validation {
	
	public static int goodBytesInArr(byte[] arr){
		int goodByteCounter = 0;
		byte goodByte = (byte) 0x00;
		
		for (byte b : arr) { if (b == goodByte) goodByteCounter ++; }
		return goodByteCounter;
	}
	
	public int equalBytesInArr(byte[] out){
		int equalByteCounter = 1;
		int tmp = 1;
		for (int i = 0; i < out.length - 1; i ++) {
			for (int j = i + 1; j < out.length; j ++) {
				if (out[i] == out[j]) tmp++;
			}
			equalByteCounter = Math.max(tmp, equalByteCounter);
			tmp = 1;
		}
		return equalByteCounter;
	}
}
