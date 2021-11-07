package utils;

public class EntriesBuildState {
	public byte a;
	public byte b;
	public byte c;
	public int i;
	public int j;
	public int k;
	public byte[][] out;
	
	public EntriesBuildState(byte a, byte b, byte c, int i, int j, int k, byte[][] out) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.i = i;
		this.j = j;
		this.k = k;
		this.out = out;
	}
	
	public static EntriesBuildState defaultState(){
		return new EntriesBuildState(
				(byte)0x00,
				(byte)0x00,
				(byte)0x00,
				0,
				0,
				0,
				new byte[][]{{}}
		);
	}
	
	public static EntriesBuildState finalState(byte[][] arr){
		return new EntriesBuildState(
				(byte)0xff,
				(byte)0xff,
				(byte)0xff,
				13,
				14,
				15,
				arr
		);
	}
	
	public boolean isStateFinal(){
		return
				(this.a == (byte) 0xff) &&
				(this.b == (byte) 0xff) &&
				(this.c == (byte) 0xff) &&
				(this.i == 13) &&
				(this.j == 14) &&
				(this.k == 15);
	}
}
