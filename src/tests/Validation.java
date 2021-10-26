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
	
	public int goodBytesInArr(byte[] out){
		int goodByteCounter = 0;
		for (byte b : out) { if (b == this.goodByte) goodByteCounter ++; }
		return goodByteCounter;
	}
	
	public static void runKusnechicTest(){
		Test.runTest();
	}
}
