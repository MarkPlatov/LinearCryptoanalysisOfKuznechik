package analytic;

import kuznechik.Builder;
import kuznechik.Crypt;
import tests.Validation;
import utils.CryptPair;
import utils.EntriesBuildState;
import utils.Utils;

import java.util.Arrays;

import static utils.Constants.BLOCK_SIZE;
import static utils.Constants.FILE_NAME;


class CryptThread extends Thread {
	public byte[][] entries;
	
	public CryptThread(byte[][] entries){ this.entries = entries; }
	
	@Override
	public void run() {
		System.out.println("Запущен поток  " + getName());
		Prelude.processDecr(entries);
		System.out.println("Поток " + getName() + " завершил подсчёт");
	}
}

public class Prelude {
	

	
	
	/**
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void oneBlockTest() {
		System.out.println("OneBlock test started");
		byte[][] entries = Builder.buildSequencesOneNotNullBlock();
		processEnc(entries);
		processDecr(entries);
	}
	
	/**
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void twoBlockTest() {
		System.out.println("TwoBlock test started");
		byte[][] entries = Builder.buildSequencesTwoNotNullBlock();
		processEnc(entries);
	}
	
	/**
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void threeBlockTest() {
		System.out.println("OneBlock test started");
		
		int treadsNum = 12;
		CryptThread[] treads = new CryptThread[treadsNum];
		
		EntriesBuildState state = Builder.buildSequencesThreeNotNullBlock(EntriesBuildState.defaultState());
		
		while (!state.isStateFinal()){
			byte[][] entries = state.out;
			int entriesPartsLen = entries.length / treadsNum + 1;
			int delta = 0;
			byte[][][] entriesParts = new byte[treadsNum][entriesPartsLen][BLOCK_SIZE];
			
			for (int i = 0; i < treadsNum; i++) {
				if (i == treadsNum - 1) {
					delta = entriesPartsLen * treadsNum - entries.length;
				}
				System.arraycopy(
						entries,
						i * entriesPartsLen,
						entriesParts[i],
						0,
						entriesPartsLen - delta);
			}
			
			for (int i = 0; i < treadsNum; i++) {
				treads[i] = new CryptThread(entriesParts[i]);
				treads[i].start();
			}
			
			for (int i = 0; i < treadsNum; i++) {
				try {
					treads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			state = Builder.buildSequencesThreeNotNullBlock(state);
			System.out.println();
		}
		
		
		
	}
	
	public static void interestingEntriesTest(){
		byte[][] entries1 =
				{
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0d, (byte) 0x00, (byte) 0xec, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xaa, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1d, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0x00, (byte) 0xdd, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc9, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf1, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x47, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xee},
						{(byte) 0x33, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4b},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x34, (byte) 0x9a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb2},
						{(byte) 0x37, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xd8, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x54, (byte) 0x00, (byte) 0x86, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x67, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf2},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4b, (byte) 0x33},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x71, (byte) 0x4f, (byte) 0x00, (byte) 0x00, (byte) 0xae, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x77, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xfe, (byte) 0x00, (byte) 0x00},
						{(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x67, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x8b, (byte) 0x00, (byte) 0x59, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xa6, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x91, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x47, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x9e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc1, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x5a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0xc1, (byte) 0x00, (byte) 0xa7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0xd4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xde, (byte) 0x00, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xd8, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93, (byte) 0x00, (byte) 0x83},
						{(byte) 0x00, (byte) 0x00, (byte) 0xdb, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8c},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe3, (byte) 0x39, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x19, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe8, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb7, (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xed, (byte) 0x00, (byte) 0x15, (byte) 0x00, (byte) 0x84, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0xf5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x35, (byte) 0x4c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
				};
		
		byte[][] entries2 =
				{
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xbb, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x0e, (byte) 0xea, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x46, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x67, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x2d, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x17, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb9, (byte) 0x00, (byte) 0xd4, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x5c, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x1b, (byte) 0x00, (byte) 0x5b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x1d, (byte) 0x00, (byte) 0x00, (byte) 0x66, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x26, (byte) 0x00, (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x26, (byte) 0x00, (byte) 0x00, (byte) 0xfe, (byte) 0x00, (byte) 0x3a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2c, (byte) 0x97, (byte) 0x00, (byte) 0x83},
						{(byte) 0x00, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xdb, (byte) 0x00, (byte) 0xd9, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x36, (byte) 0xb3, (byte) 0x00, (byte) 0xcb, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x3e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xfd, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x42, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xd4},
						{(byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d, (byte) 0x00, (byte) 0x32},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5c, (byte) 0xd1, (byte) 0x00, (byte) 0x00, (byte) 0xb3, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x41, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe4, (byte) 0x00, (byte) 0x00, (byte) 0xd7},
						{(byte) 0x00, (byte) 0x5f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x65, (byte) 0xbd, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc6, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x66, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xed},
						{(byte) 0x00, (byte) 0x00, (byte) 0x6b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7b, (byte) 0x00, (byte) 0x1e, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x73, (byte) 0x00, (byte) 0xa3, (byte) 0x45, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x75, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2c, (byte) 0x97, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xd4, (byte) 0x66},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x96, (byte) 0x00, (byte) 0x38, (byte) 0x00, (byte) 0x85, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x98, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x51, (byte) 0x00, (byte) 0x95, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x9a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x22, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8e},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xfd, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb5},
						{(byte) 0x00, (byte) 0x00, (byte) 0xa5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe7, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xab, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x6b, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x49, (byte) 0x00, (byte) 0xbb, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x9c, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x00},
						{(byte) 0xb8, (byte) 0x42, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb9, (byte) 0x00, (byte) 0x00, (byte) 0x95, (byte) 0x00, (byte) 0x2b, (byte) 0x00},
						{(byte) 0x00, (byte) 0xba, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x23, (byte) 0x00, (byte) 0x00, (byte) 0x3d, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0xbf, (byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc2, (byte) 0x2b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb4, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc4, (byte) 0x00, (byte) 0x24, (byte) 0xcc, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc8, (byte) 0x00, (byte) 0x00, (byte) 0x68, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x50, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xda, (byte) 0x8e, (byte) 0x00, (byte) 0x00, (byte) 0x34, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf7, (byte) 0x00, (byte) 0x64, (byte) 0x48, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0xfe, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
				};
		
		byte[][] bufferOut;
		Crypt kuz = new Crypt();
		kuz.setIterKey(Builder.get10EqualIterKeysFromKey(Crypt.ZERO_ROUND_KEY));
		
		for (byte[] e : entries1){
			bufferOut = kuz.encryptPerRound(e,9);
			System.out.println("Enc: " + Utils.byteArrToHexStr(e));
			int i = 0;
			for (byte[] bytes : bufferOut){
				System.out.println("Round " + i + ": zero bytes: " + Validation.goodBytesInArr(bytes));
				i ++;
			}
			System.out.println("\n");
		}
		
		for (byte[] e : entries2){
			bufferOut = kuz.decryptPerRound(e,9);
			System.out.println("Decrypt: " + Utils.byteArrToHexStr(e));
			int i = 0;
			for (byte[] bytes : bufferOut){
				System.out.println("Round " + i + ": zero bytes: " + Validation.goodBytesInArr(bytes));
				i ++;
			}
			System.out.println("\n");
		}
	}
	
	public static void processEnc(
			byte[][] entries
	){
		byte[] tmpOut;
		int numberOfEntries = entries.length;
		int numberOfRounds = 1;
		
//		Vector<CryptPair> goodOuts = new Vector<>();
		Crypt kusnechik = new Crypt();
		
		
		System.out.println("Encryption for " +
				numberOfEntries +
				" entries with zero key in " +
				numberOfRounds +
				" rounds...");
		
		int i = 0;
		int goodBytes;
		String prefix = "_TREE_BLOCK_TEST_";
		String[] fileName = {
				String.format("%s%s_0_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_1_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_2_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_3_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_4_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_5_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_6_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_7_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_8_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_9_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_10_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_11_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_12_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_13_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_14_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_15_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_16_0x00.txt", FILE_NAME, prefix),
		};
		
		byte[][] iterKeys = {{0x00}};
		
		for (byte[] entry : entries) {
			i ++;
			tmpOut = kusnechik.encryptOneRoundZeroKey(entry);
			goodBytes = Validation.goodBytesInArr(tmpOut);
			
			if (goodBytes > 4) {
				CryptPair goodOut = new CryptPair(
						entry,
						entry,
						Arrays.copyOf(tmpOut, tmpOut.length),
						numberOfRounds,
						iterKeys
				);
//				goodOuts.add(goodOut);
				goodOut.addToFile(fileName[goodBytes]);
			}
			
			if (i % 1000000 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
	}
	
	public static void processEnc(
			byte[][] entries,
			byte[][][] iterKeys,
			int numberOfRounds
	){
		byte[][] tmpOut;
		int numberOfIterKeys = iterKeys.length;
		int numberOfEntries = entries.length;

//		Vector<CryptPair> goodOuts = new Vector<>(); TODO delete at last time
		Crypt kusnechik = new Crypt();
		
		System.out.println("Encryption for " +
				numberOfEntries +
				" entries with " +
				numberOfIterKeys +
				" keys in " +
				numberOfRounds +
				" rounds...");
		
		int i = 0;
		int goodBytes;
		String fileNameMask = String.format("%s_0x00_", FILE_NAME);
		String fileName;
		for (byte[] entry : entries) {
			i ++;
			for (byte[][] iterKey : iterKeys) {
				kusnechik.setIterKey(iterKey);
				tmpOut = kusnechik.encryptPerRound(entry, numberOfRounds);
				
				for (int j = 1; j < tmpOut.length; j ++) {
					goodBytes = Validation.goodBytesInArr(tmpOut[j]);
					if (goodBytes > 2) {
						CryptPair goodOut = new CryptPair(
								entry,
								Arrays.copyOf(tmpOut[j - 1], tmpOut[j - 1].length),
								Arrays.copyOf(tmpOut[j], tmpOut[j].length),
								j,
								iterKey
						);
//						goodOuts.add(goodOut); TODO delete at last time
						fileName = String.format("%s%s.txt", fileNameMask, goodBytes);
						goodOut.addToFile(fileName);
					}
				}
			}
			if (i % 100000 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
//		return goodOuts; TODO delete at last time
	}
	
	public static void processDecr(
			byte[][] entries
	){
		byte[] tmpOut;
		int numberOfEntries = entries.length;
		int numberOfRounds = 1;
		
//		Vector<CryptPair> goodOuts = new Vector<>(); TODO delete at last time
		Crypt kusnechik = new Crypt();
		
		
		System.out.println("Encryption for " +
				numberOfEntries +
				" entries with zero key in " +
				numberOfRounds +
				" rounds...");
		
		int i = 0;
		int goodBytes;
		String prefix = "_TREE_BLOCK_DECRYPT_TEST_";
		String[] fileName = {
				String.format("%s%s_0_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_1_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_2_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_3_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_4_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_5_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_6_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_7_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_8_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_9_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_10_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_11_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_12_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_13_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_14_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_15_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_16_0x00.txt", FILE_NAME, prefix),
		};
		
		byte[][] iterKeys = {{0x00}};

		for (byte[] entry : entries) {
			i ++;
			tmpOut = kusnechik.decryptOneRoundZeroKey(entry);
			goodBytes = Validation.goodBytesInArr(tmpOut);
			
			if (goodBytes > 4) {
				CryptPair goodOut = new CryptPair(
						Arrays.copyOf(tmpOut, tmpOut.length),
						null,
						entry,
						numberOfRounds,
						iterKeys
				);
//				goodOuts.add(goodOut); TODO delete at last time
				goodOut.addToFile(fileName[goodBytes]);
			}
			
			if (i % 1000000 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
	}
	
	public static void processDecr(
			byte[][] entries,
			byte[][][] iterKeys,
			int numberOfRounds
	){
		byte[] tmpOut;
		int numberOfIterKeys = iterKeys.length;
		int numberOfEntries = entries.length;
		
//		Vector<CryptPair> goodOuts = new Vector<>(); TODO delete at last time
		Crypt kusnechik = new Crypt();
		
		
		System.out.println("Encryption for " +
				numberOfEntries +
				" entries with " +
				numberOfIterKeys +
				" keys in " +
				numberOfRounds +
				" rounds...");
		
		int i = 0;
		int goodBytes;
		String fileName;
		for (byte[] entry : entries) {
			i ++;
			for (byte[][] iterKey : iterKeys) {
				kusnechik.setIterKey(iterKey);
				tmpOut = kusnechik.decrypt(entry, numberOfRounds);
				goodBytes = Validation.goodBytesInArr(tmpOut);
				if (goodBytes > 2) {
					CryptPair goodOut = new CryptPair(
							Arrays.copyOf(tmpOut, tmpOut.length),
							null,
							entry,
							numberOfRounds,
							iterKey
					);
//					goodOuts.add(goodOut); TODO delete at last time
					fileName = String.format("%s_Decrypt_0x00_%s.txt", FILE_NAME, goodBytes);
					goodOut.addToFile(fileName);
				}
			}
			if (i % 1000 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
	}
	
}
