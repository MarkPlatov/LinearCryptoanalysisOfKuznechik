package tests;

import kuznechik.Test;

public class Validation {
	public byte goodByte;
	
	public Validation(byte goodByte) {
		this.goodByte = goodByte;
	}
	public Validation() {
		this.goodByte = (byte) 0x00;
	}
	
	public int goodBytesInArr(byte[] arr){
		int goodByteCounter = 0;
		int badByteCounter = 0;
		
//		for (int i = 0; i < 6; i ++) { if (arr[i] != this.goodByte) badByteCounter ++;}
//		if (badByteCounter == 6) return 0;
		
		for (byte b : arr) { if (b == this.goodByte) goodByteCounter ++; }
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
	
	public static void runKusnechicTest(){
		Test.runTest();
	}
}
