package cipher;

import utils.Utils;

import java.util.Arrays;

public class Crypt {
	public static final int BLOCK_SIZE = 16; // длина блока в байтах
	public static final int VALID_KEY_LEN = 32; // длина ключа в байтах
	public static final byte[] ZERO_ROUND_KEY = new byte[]{
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	};
	
	// Таблица прямого нелинейного преобразования
	static public final byte[] PI = {
			(byte) 0xFC, (byte) 0xEE, (byte) 0xDD, (byte) 0x11, (byte) 0xCF, (byte) 0x6E, (byte) 0x31, (byte) 0x16,
			(byte) 0xFB, (byte) 0xC4, (byte) 0xFA, (byte) 0xDA, (byte) 0x23, (byte) 0xC5, (byte) 0x04, (byte) 0x4D,
			(byte) 0xE9, (byte) 0x77, (byte) 0xF0, (byte) 0xDB, (byte) 0x93, (byte) 0x2E, (byte) 0x99, (byte) 0xBA,
			(byte) 0x17, (byte) 0x36, (byte) 0xF1, (byte) 0xBB, (byte) 0x14, (byte) 0xCD, (byte) 0x5F, (byte) 0xC1,
			(byte) 0xF9, (byte) 0x18, (byte) 0x65, (byte) 0x5A, (byte) 0xE2, (byte) 0x5C, (byte) 0xEF, (byte) 0x21,
			(byte) 0x81, (byte) 0x1C, (byte) 0x3C, (byte) 0x42, (byte) 0x8B, (byte) 0x01, (byte) 0x8E, (byte) 0x4F,
			(byte) 0x05, (byte) 0x84, (byte) 0x02, (byte) 0xAE, (byte) 0xE3, (byte) 0x6A, (byte) 0x8F, (byte) 0xA0,
			(byte) 0x06, (byte) 0x0B, (byte) 0xED, (byte) 0x98, (byte) 0x7F, (byte) 0xD4, (byte) 0xD3, (byte) 0x1F,
			(byte) 0xEB, (byte) 0x34, (byte) 0x2C, (byte) 0x51, (byte) 0xEA, (byte) 0xC8, (byte) 0x48, (byte) 0xAB,
			(byte) 0xF2, (byte) 0x2A, (byte) 0x68, (byte) 0xA2, (byte) 0xFD, (byte) 0x3A, (byte) 0xCE, (byte) 0xCC,
			(byte) 0xB5, (byte) 0x70, (byte) 0x0E, (byte) 0x56, (byte) 0x08, (byte) 0x0C, (byte) 0x76, (byte) 0x12,
			(byte) 0xBF, (byte) 0x72, (byte) 0x13, (byte) 0x47, (byte) 0x9C, (byte) 0xB7, (byte) 0x5D, (byte) 0x87,
			(byte) 0x15, (byte) 0xA1, (byte) 0x96, (byte) 0x29, (byte) 0x10, (byte) 0x7B, (byte) 0x9A, (byte) 0xC7,
			(byte) 0xF3, (byte) 0x91, (byte) 0x78, (byte) 0x6F, (byte) 0x9D, (byte) 0x9E, (byte) 0xB2, (byte) 0xB1,
			(byte) 0x32, (byte) 0x75, (byte) 0x19, (byte) 0x3D, (byte) 0xFF, (byte) 0x35, (byte) 0x8A, (byte) 0x7E,
			(byte) 0x6D, (byte) 0x54, (byte) 0xC6, (byte) 0x80, (byte) 0xC3, (byte) 0xBD, (byte) 0x0D, (byte) 0x57,
			(byte) 0xDF, (byte) 0xF5, (byte) 0x24, (byte) 0xA9, (byte) 0x3E, (byte) 0xA8, (byte) 0x43, (byte) 0xC9,
			(byte) 0xD7, (byte) 0x79, (byte) 0xD6, (byte) 0xF6, (byte) 0x7C, (byte) 0x22, (byte) 0xB9, (byte) 0x03,
			(byte) 0xE0, (byte) 0x0F, (byte) 0xEC, (byte) 0xDE, (byte) 0x7A, (byte) 0x94, (byte) 0xB0, (byte) 0xBC,
			(byte) 0xDC, (byte) 0xE8, (byte) 0x28, (byte) 0x50, (byte) 0x4E, (byte) 0x33, (byte) 0x0A, (byte) 0x4A,
			(byte) 0xA7, (byte) 0x97, (byte) 0x60, (byte) 0x73, (byte) 0x1E, (byte) 0x00, (byte) 0x62, (byte) 0x44,
			(byte) 0x1A, (byte) 0xB8, (byte) 0x38, (byte) 0x82, (byte) 0x64, (byte) 0x9F, (byte) 0x26, (byte) 0x41,
			(byte) 0xAD, (byte) 0x45, (byte) 0x46, (byte) 0x92, (byte) 0x27, (byte) 0x5E, (byte) 0x55, (byte) 0x2F,
			(byte) 0x8C, (byte) 0xA3, (byte) 0xA5, (byte) 0x7D, (byte) 0x69, (byte) 0xD5, (byte) 0x95, (byte) 0x3B,
			(byte) 0x07, (byte) 0x58, (byte) 0xB3, (byte) 0x40, (byte) 0x86, (byte) 0xAC, (byte) 0x1D, (byte) 0xF7,
			(byte) 0x30, (byte) 0x37, (byte) 0x6B, (byte) 0xE4, (byte) 0x88, (byte) 0xD9, (byte) 0xE7, (byte) 0x89,
			(byte) 0xE1, (byte) 0x1B, (byte) 0x83, (byte) 0x49, (byte) 0x4C, (byte) 0x3F, (byte) 0xF8, (byte) 0xFE,
			(byte) 0x8D, (byte) 0x53, (byte) 0xAA, (byte) 0x90, (byte) 0xCA, (byte) 0xD8, (byte) 0x85, (byte) 0x61,
			(byte) 0x20, (byte) 0x71, (byte) 0x67, (byte) 0xA4, (byte) 0x2D, (byte) 0x2B, (byte) 0x09, (byte) 0x5B,
			(byte) 0xCB, (byte) 0x9B, (byte) 0x25, (byte) 0xD0, (byte) 0xBE, (byte) 0xE5, (byte) 0x6C, (byte) 0x52,
			(byte) 0x59, (byte) 0xA6, (byte) 0x74, (byte) 0xD2, (byte) 0xE6, (byte) 0xF4, (byte) 0xB4, (byte) 0xC0,
			(byte) 0xD1, (byte) 0x66, (byte) 0xAF, (byte) 0xC2, (byte) 0x39, (byte) 0x4B, (byte) 0x63, (byte) 0xB6,
			
			
			/*FC, EE, DD, 11, CF, 6E, 31, 16, FB, C4, FA, DA, 23, C5, 04, 4D,
			E9, 77, F0, DB, 93, 2E, 99, BA, 17, 36, F1, BB, 14, CD, 5F, C1,
			F9, 18, 65, 5A, E2, 5C, EF, 21, 81, 1C, 3C, 42, 8B, 01, 8E, 4F,
			05, 84, 02, AE, E3, 6A, 8F, A0, 06, 0B, ED, 98, 7F, D4, D3, 1F,
			EB, 34, 2C, 51, EA, C8, 48, AB, F2, 2A, 68, A2, FD, 3A, CE, CC,
			B5, 70, 0E, 56, 08, 0C, 76, 12, BF, 72, 13, 47, 9C, B7, 5D, 87,
			15, A1, 96, 29, 10, 7B, 9A, C7, F3, 91, 78, 6F, 9D, 9E, B2, B1,
			32, 75, 19, 3D, FF, 35, 8A, 7E, 6D, 54, C6, 80, C3, BD, 0D, 57,
			DF, F5, 24, A9, 3E, A8, 43, C9, D7, 79, D6, F6, 7C, 22, B9, 03,
			E0, 0F, EC, DE, 7A, 94, B0, BC, DC, E8, 28, 50, 4E, 33, 0A, 4A,
			A7, 97, 60, 73, 1E, 00, 62, 44, 1A, B8, 38, 82, 64, 9F, 26, 41,
			AD, 45, 46, 92, 27, 5E, 55, 2F, 8C, A3, A5, 7D, 69, D5, 95, 3B,
			07, 58, B3, 40, 86, AC, 1D, F7, 30, 37, 6B, E4, 88, D9, E7, 89,
			E1, 1B, 83, 49, 4C, 3F, F8, FE, 8D, 53, AA, 90, CA, D8, 85, 61,
			20, 71, 67, A4, 2D, 2B, 09, 5B, CB, 9B, 25, D0, BE, E5, 6C, 52,
			59, A6, 74, D2, E6, F4, B4, C0, D1, 66, AF, C2, 39, 4B, 63, B6,*/
	};
	
	// Таблица обратного нелинейного преобразования
	static final public byte[] REVERSE_PI = {
			(byte) 0xA5, (byte) 0x2D, (byte) 0x32, (byte) 0x8F, (byte) 0x0E, (byte) 0x30, (byte) 0x38, (byte) 0xC0,
			(byte) 0x54, (byte) 0xE6, (byte) 0x9E, (byte) 0x39, (byte) 0x55, (byte) 0x7E, (byte) 0x52, (byte) 0x91,
			(byte) 0x64, (byte) 0x03, (byte) 0x57, (byte) 0x5A, (byte) 0x1C, (byte) 0x60, (byte) 0x07, (byte) 0x18,
			(byte) 0x21, (byte) 0x72, (byte) 0xA8, (byte) 0xD1, (byte) 0x29, (byte) 0xC6, (byte) 0xA4, (byte) 0x3F,
			(byte) 0xE0, (byte) 0x27, (byte) 0x8D, (byte) 0x0C, (byte) 0x82, (byte) 0xEA, (byte) 0xAE, (byte) 0xB4,
			(byte) 0x9A, (byte) 0x63, (byte) 0x49, (byte) 0xE5, (byte) 0x42, (byte) 0xE4, (byte) 0x15, (byte) 0xB7,
			(byte) 0xC8, (byte) 0x06, (byte) 0x70, (byte) 0x9D, (byte) 0x41, (byte) 0x75, (byte) 0x19, (byte) 0xC9,
			(byte) 0xAA, (byte) 0xFC, (byte) 0x4D, (byte) 0xBF, (byte) 0x2A, (byte) 0x73, (byte) 0x84, (byte) 0xD5,
			(byte) 0xC3, (byte) 0xAF, (byte) 0x2B, (byte) 0x86, (byte) 0xA7, (byte) 0xB1, (byte) 0xB2, (byte) 0x5B,
			(byte) 0x46, (byte) 0xD3, (byte) 0x9F, (byte) 0xFD, (byte) 0xD4, (byte) 0x0F, (byte) 0x9C, (byte) 0x2F,
			(byte) 0x9B, (byte) 0x43, (byte) 0xEF, (byte) 0xD9, (byte) 0x79, (byte) 0xB6, (byte) 0x53, (byte) 0x7F,
			(byte) 0xC1, (byte) 0xF0, (byte) 0x23, (byte) 0xE7, (byte) 0x25, (byte) 0x5E, (byte) 0xB5, (byte) 0x1E,
			(byte) 0xA2, (byte) 0xDF, (byte) 0xA6, (byte) 0xFE, (byte) 0xAC, (byte) 0x22, (byte) 0xF9, (byte) 0xE2,
			(byte) 0x4A, (byte) 0xBC, (byte) 0x35, (byte) 0xCA, (byte) 0xEE, (byte) 0x78, (byte) 0x05, (byte) 0x6B,
			(byte) 0x51, (byte) 0xE1, (byte) 0x59, (byte) 0xA3, (byte) 0xF2, (byte) 0x71, (byte) 0x56, (byte) 0x11,
			(byte) 0x6A, (byte) 0x89, (byte) 0x94, (byte) 0x65, (byte) 0x8C, (byte) 0xBB, (byte) 0x77, (byte) 0x3C,
			(byte) 0x7B, (byte) 0x28, (byte) 0xAB, (byte) 0xD2, (byte) 0x31, (byte) 0xDE, (byte) 0xC4, (byte) 0x5F,
			(byte) 0xCC, (byte) 0xCF, (byte) 0x76, (byte) 0x2C, (byte) 0xB8, (byte) 0xD8, (byte) 0x2E, (byte) 0x36,
			(byte) 0xDB, (byte) 0x69, (byte) 0xB3, (byte) 0x14, (byte) 0x95, (byte) 0xBE, (byte) 0x62, (byte) 0xA1,
			(byte) 0x3B, (byte) 0x16, (byte) 0x66, (byte) 0xE9, (byte) 0x5C, (byte) 0x6C, (byte) 0x6D, (byte) 0xAD,
			(byte) 0x37, (byte) 0x61, (byte) 0x4B, (byte) 0xB9, (byte) 0xE3, (byte) 0xBA, (byte) 0xF1, (byte) 0xA0,
			(byte) 0x85, (byte) 0x83, (byte) 0xDA, (byte) 0x47, (byte) 0xC5, (byte) 0xB0, (byte) 0x33, (byte) 0xFA,
			(byte) 0x96, (byte) 0x6F, (byte) 0x6E, (byte) 0xC2, (byte) 0xF6, (byte) 0x50, (byte) 0xFF, (byte) 0x5D,
			(byte) 0xA9, (byte) 0x8E, (byte) 0x17, (byte) 0x1B, (byte) 0x97, (byte) 0x7D, (byte) 0xEC, (byte) 0x58,
			(byte) 0xF7, (byte) 0x1F, (byte) 0xFB, (byte) 0x7C, (byte) 0x09, (byte) 0x0D, (byte) 0x7A, (byte) 0x67,
			(byte) 0x45, (byte) 0x87, (byte) 0xDC, (byte) 0xE8, (byte) 0x4F, (byte) 0x1D, (byte) 0x4E, (byte) 0x04,
			(byte) 0xEB, (byte) 0xF8, (byte) 0xF3, (byte) 0x3E, (byte) 0x3D, (byte) 0xBD, (byte) 0x8A, (byte) 0x88,
			(byte) 0xDD, (byte) 0xCD, (byte) 0x0B, (byte) 0x13, (byte) 0x98, (byte) 0x02, (byte) 0x93, (byte) 0x80,
			(byte) 0x90, (byte) 0xD0, (byte) 0x24, (byte) 0x34, (byte) 0xCB, (byte) 0xED, (byte) 0xF4, (byte) 0xCE,
			(byte) 0x99, (byte) 0x10, (byte) 0x44, (byte) 0x40, (byte) 0x92, (byte) 0x3A, (byte) 0x01, (byte) 0x26,
			(byte) 0x12, (byte) 0x1A, (byte) 0x48, (byte) 0x68, (byte) 0xF5, (byte) 0x81, (byte) 0x8B, (byte) 0xC7,
			(byte) 0xD6, (byte) 0x20, (byte) 0x0A, (byte) 0x08, (byte) 0x00, (byte) 0x4C, (byte) 0xD7, (byte) 0x74,
	};
	
	// Вектор линейного преобразования
	static final byte[] L_VEC = {
			(byte) 1  , (byte) 148, (byte) 32 , (byte) 133, (byte) 16 , (byte) 194, (byte) 192, (byte) 1  ,
			(byte) 251, (byte) 1  , (byte) 192, (byte) 194, (byte) 16 , (byte) 133, (byte) 32 , (byte) 148,
	};
	
	// Массив для хранения констант
//	static byte[][] ITER_C = new byte[32][16];
	static final byte[][] ITER_C = {
			{(byte) 0x01, (byte) 0x94, (byte) 0x84, (byte) 0xdd, (byte) 0x10, (byte) 0xbd, (byte) 0x27, (byte) 0x5d, (byte) 0xb8, (byte) 0x7a, (byte) 0x48, (byte) 0x6c, (byte) 0x72, (byte) 0x76, (byte) 0xa2, (byte) 0x6e},
			{(byte) 0x02, (byte) 0xeb, (byte) 0xcb, (byte) 0x79, (byte) 0x20, (byte) 0xb9, (byte) 0x4e, (byte) 0xba, (byte) 0xb3, (byte) 0xf4, (byte) 0x90, (byte) 0xd8, (byte) 0xe4, (byte) 0xec, (byte) 0x87, (byte) 0xdc},
			{(byte) 0x03, (byte) 0x7f, (byte) 0x4f, (byte) 0xa4, (byte) 0x30, (byte) 0x04, (byte) 0x69, (byte) 0xe7, (byte) 0x0b, (byte) 0x8e, (byte) 0xd8, (byte) 0xb4, (byte) 0x96, (byte) 0x9a, (byte) 0x25, (byte) 0xb2},
			{(byte) 0x04, (byte) 0x15, (byte) 0x55, (byte) 0xf2, (byte) 0x40, (byte) 0xb1, (byte) 0x9c, (byte) 0xb7, (byte) 0xa5, (byte) 0x2b, (byte) 0xe3, (byte) 0x73, (byte) 0x0b, (byte) 0x1b, (byte) 0xcd, (byte) 0x7b},
			{(byte) 0x05, (byte) 0x81, (byte) 0xd1, (byte) 0x2f, (byte) 0x50, (byte) 0x0c, (byte) 0xbb, (byte) 0xea, (byte) 0x1d, (byte) 0x51, (byte) 0xab, (byte) 0x1f, (byte) 0x79, (byte) 0x6d, (byte) 0x6f, (byte) 0x15},
			{(byte) 0x06, (byte) 0xfe, (byte) 0x9e, (byte) 0x8b, (byte) 0x60, (byte) 0x08, (byte) 0xd2, (byte) 0x0d, (byte) 0x16, (byte) 0xdf, (byte) 0x73, (byte) 0xab, (byte) 0xef, (byte) 0xf7, (byte) 0x4a, (byte) 0xa7},
			{(byte) 0x07, (byte) 0x6a, (byte) 0x1a, (byte) 0x56, (byte) 0x70, (byte) 0xb5, (byte) 0xf5, (byte) 0x50, (byte) 0xae, (byte) 0xa5, (byte) 0x3b, (byte) 0xc7, (byte) 0x9d, (byte) 0x81, (byte) 0xe8, (byte) 0xc9},
			{(byte) 0x08, (byte) 0x2a, (byte) 0xaa, (byte) 0x27, (byte) 0x80, (byte) 0xa1, (byte) 0xfb, (byte) 0xad, (byte) 0x89, (byte) 0x56, (byte) 0x05, (byte) 0xe6, (byte) 0x16, (byte) 0x36, (byte) 0x59, (byte) 0xf6},
			{(byte) 0x09, (byte) 0xbe, (byte) 0x2e, (byte) 0xfa, (byte) 0x90, (byte) 0x1c, (byte) 0xdc, (byte) 0xf0, (byte) 0x31, (byte) 0x2c, (byte) 0x4d, (byte) 0x8a, (byte) 0x64, (byte) 0x40, (byte) 0xfb, (byte) 0x98},
			{(byte) 0x0a, (byte) 0xc1, (byte) 0x61, (byte) 0x5e, (byte) 0xa0, (byte) 0x18, (byte) 0xb5, (byte) 0x17, (byte) 0x3a, (byte) 0xa2, (byte) 0x95, (byte) 0x3e, (byte) 0xf2, (byte) 0xda, (byte) 0xde, (byte) 0x2a},
			{(byte) 0x0b, (byte) 0x55, (byte) 0xe5, (byte) 0x83, (byte) 0xb0, (byte) 0xa5, (byte) 0x92, (byte) 0x4a, (byte) 0x82, (byte) 0xd8, (byte) 0xdd, (byte) 0x52, (byte) 0x80, (byte) 0xac, (byte) 0x7c, (byte) 0x44},
			{(byte) 0x0c, (byte) 0x3f, (byte) 0xff, (byte) 0xd5, (byte) 0xc0, (byte) 0x10, (byte) 0x67, (byte) 0x1a, (byte) 0x2c, (byte) 0x7d, (byte) 0xe6, (byte) 0x95, (byte) 0x1d, (byte) 0x2d, (byte) 0x94, (byte) 0x8d},
			{(byte) 0x0d, (byte) 0xab, (byte) 0x7b, (byte) 0x08, (byte) 0xd0, (byte) 0xad, (byte) 0x40, (byte) 0x47, (byte) 0x94, (byte) 0x07, (byte) 0xae, (byte) 0xf9, (byte) 0x6f, (byte) 0x5b, (byte) 0x36, (byte) 0xe3},
			{(byte) 0x0e, (byte) 0xd4, (byte) 0x34, (byte) 0xac, (byte) 0xe0, (byte) 0xa9, (byte) 0x29, (byte) 0xa0, (byte) 0x9f, (byte) 0x89, (byte) 0x76, (byte) 0x4d, (byte) 0xf9, (byte) 0xc1, (byte) 0x13, (byte) 0x51},
			{(byte) 0x0f, (byte) 0x40, (byte) 0xb0, (byte) 0x71, (byte) 0xf0, (byte) 0x14, (byte) 0x0e, (byte) 0xfd, (byte) 0x27, (byte) 0xf3, (byte) 0x3e, (byte) 0x21, (byte) 0x8b, (byte) 0xb7, (byte) 0xb1, (byte) 0x3f},
			{(byte) 0x10, (byte) 0x54, (byte) 0x97, (byte) 0x4e, (byte) 0xc3, (byte) 0x81, (byte) 0x35, (byte) 0x99, (byte) 0xd1, (byte) 0xac, (byte) 0x0a, (byte) 0x0f, (byte) 0x2c, (byte) 0x6c, (byte) 0xb2, (byte) 0x2f},
			{(byte) 0x11, (byte) 0xc0, (byte) 0x13, (byte) 0x93, (byte) 0xd3, (byte) 0x3c, (byte) 0x12, (byte) 0xc4, (byte) 0x69, (byte) 0xd6, (byte) 0x42, (byte) 0x63, (byte) 0x5e, (byte) 0x1a, (byte) 0x10, (byte) 0x41},
			{(byte) 0x12, (byte) 0xbf, (byte) 0x5c, (byte) 0x37, (byte) 0xe3, (byte) 0x38, (byte) 0x7b, (byte) 0x23, (byte) 0x62, (byte) 0x58, (byte) 0x9a, (byte) 0xd7, (byte) 0xc8, (byte) 0x80, (byte) 0x35, (byte) 0xf3},
			{(byte) 0x13, (byte) 0x2b, (byte) 0xd8, (byte) 0xea, (byte) 0xf3, (byte) 0x85, (byte) 0x5c, (byte) 0x7e, (byte) 0xda, (byte) 0x22, (byte) 0xd2, (byte) 0xbb, (byte) 0xba, (byte) 0xf6, (byte) 0x97, (byte) 0x9d},
			{(byte) 0x14, (byte) 0x41, (byte) 0xc2, (byte) 0xbc, (byte) 0x83, (byte) 0x30, (byte) 0xa9, (byte) 0x2e, (byte) 0x74, (byte) 0x87, (byte) 0xe9, (byte) 0x7c, (byte) 0x27, (byte) 0x77, (byte) 0x7f, (byte) 0x54},
			{(byte) 0x15, (byte) 0xd5, (byte) 0x46, (byte) 0x61, (byte) 0x93, (byte) 0x8d, (byte) 0x8e, (byte) 0x73, (byte) 0xcc, (byte) 0xfd, (byte) 0xa1, (byte) 0x10, (byte) 0x55, (byte) 0x01, (byte) 0xdd, (byte) 0x3a},
			{(byte) 0x16, (byte) 0xaa, (byte) 0x09, (byte) 0xc5, (byte) 0xa3, (byte) 0x89, (byte) 0xe7, (byte) 0x94, (byte) 0xc7, (byte) 0x73, (byte) 0x79, (byte) 0xa4, (byte) 0xc3, (byte) 0x9b, (byte) 0xf8, (byte) 0x88},
			{(byte) 0x17, (byte) 0x3e, (byte) 0x8d, (byte) 0x18, (byte) 0xb3, (byte) 0x34, (byte) 0xc0, (byte) 0xc9, (byte) 0x7f, (byte) 0x09, (byte) 0x31, (byte) 0xc8, (byte) 0xb1, (byte) 0xed, (byte) 0x5a, (byte) 0xe6},
			{(byte) 0x18, (byte) 0x7e, (byte) 0x3d, (byte) 0x69, (byte) 0x43, (byte) 0x20, (byte) 0xce, (byte) 0x34, (byte) 0x58, (byte) 0xfa, (byte) 0x0f, (byte) 0xe9, (byte) 0x3a, (byte) 0x5a, (byte) 0xeb, (byte) 0xd9},
			{(byte) 0x19, (byte) 0xea, (byte) 0xb9, (byte) 0xb4, (byte) 0x53, (byte) 0x9d, (byte) 0xe9, (byte) 0x69, (byte) 0xe0, (byte) 0x80, (byte) 0x47, (byte) 0x85, (byte) 0x48, (byte) 0x2c, (byte) 0x49, (byte) 0xb7},
			{(byte) 0x1a, (byte) 0x95, (byte) 0xf6, (byte) 0x10, (byte) 0x63, (byte) 0x99, (byte) 0x80, (byte) 0x8e, (byte) 0xeb, (byte) 0x0e, (byte) 0x9f, (byte) 0x31, (byte) 0xde, (byte) 0xb6, (byte) 0x6c, (byte) 0x05},
			{(byte) 0x1b, (byte) 0x01, (byte) 0x72, (byte) 0xcd, (byte) 0x73, (byte) 0x24, (byte) 0xa7, (byte) 0xd3, (byte) 0x53, (byte) 0x74, (byte) 0xd7, (byte) 0x5d, (byte) 0xac, (byte) 0xc0, (byte) 0xce, (byte) 0x6b},
			{(byte) 0x1c, (byte) 0x6b, (byte) 0x68, (byte) 0x9b, (byte) 0x03, (byte) 0x91, (byte) 0x52, (byte) 0x83, (byte) 0xfd, (byte) 0xd1, (byte) 0xec, (byte) 0x9a, (byte) 0x31, (byte) 0x41, (byte) 0x26, (byte) 0xa2},
			{(byte) 0x1d, (byte) 0xff, (byte) 0xec, (byte) 0x46, (byte) 0x13, (byte) 0x2c, (byte) 0x75, (byte) 0xde, (byte) 0x45, (byte) 0xab, (byte) 0xa4, (byte) 0xf6, (byte) 0x43, (byte) 0x37, (byte) 0x84, (byte) 0xcc},
			{(byte) 0x1e, (byte) 0x80, (byte) 0xa3, (byte) 0xe2, (byte) 0x23, (byte) 0x28, (byte) 0x1c, (byte) 0x39, (byte) 0x4e, (byte) 0x25, (byte) 0x7c, (byte) 0x42, (byte) 0xd5, (byte) 0xad, (byte) 0xa1, (byte) 0x7e},
			{(byte) 0x1f, (byte) 0x14, (byte) 0x27, (byte) 0x3f, (byte) 0x33, (byte) 0x95, (byte) 0x3b, (byte) 0x64, (byte) 0xf6, (byte) 0x5f, (byte) 0x34, (byte) 0x2e, (byte) 0xa7, (byte) 0xdb, (byte) 0x03, (byte) 0x10},
			{(byte) 0x20, (byte) 0xa8, (byte) 0xed, (byte) 0x9c, (byte) 0x45, (byte) 0xc1, (byte) 0x6a, (byte) 0xf1, (byte) 0x61, (byte) 0x9b, (byte) 0x14, (byte) 0x1e, (byte) 0x58, (byte) 0xd8, (byte) 0xa7, (byte) 0x5e},
	};
	// Массив для хранения ключей
	private byte[][] iterKey = new byte[10][16];
	
	
	// Функция XOR
	static private byte[] xor(byte[] a, byte[] b) {
		byte[] c = new byte[BLOCK_SIZE];
		for (int i = 0; i < BLOCK_SIZE; i++) c[i] = (byte) (a[i] ^ b[i]);
		return c;
	}
	
	// Функция S
	static public byte[] transformS(byte[] in_data, boolean reverse) {
		byte[] out_data = new byte[in_data.length];
		int data;
		byte[] pi = reverse ? REVERSE_PI : PI;
		
		for (int i = 0; i < BLOCK_SIZE; i++) {
			data = (in_data[i] < 0) ? in_data[i] + 256 : in_data[i];
			out_data[i] = pi[data];
		}
		return out_data;
	}
	
	// Умножение в поле Галуа
	static private byte multGF(byte a, byte b) {
		byte c = 0;
		byte hi_bit;
		
		for (int i = 0; i < 8; i++) {
			if ((b & 1) == 1) c ^= a;
			hi_bit = (byte) (a & 0x80);
			a <<= 1;
			if (hi_bit < 0) a ^= 0xc3; //полином  x^8+x^7+x^6+x+1
			b >>= 1;
		}
		return c;
	}
	
	// Функция R сдвигает данные и реализует уравнение, представленное для расчета L-функции
	static private byte[] transformR(byte[] state) {
		byte a_15 = 0;
		byte[] internal = new byte[16];
		for (int i = 15; i >= 1; i--) {
			internal[i - 1] = state[i];
			a_15 ^= multGF(state[i], L_VEC[i]);
		}

		a_15 ^= multGF(state[0], L_VEC[0]);
		internal[15] = a_15;
		return internal;
	}
	
	static private byte[] transformRReverse(byte[] state) {
		byte a_0 = state[15];
		byte[] internal = new byte[16];
		for (int i = 1; i < 16; i++) {
			internal[i] = state[i - 1];
			a_0 ^= multGF(internal[i], L_VEC[i]);
		}
		
		internal[0] = a_0;
		return internal;
	}
	
	static public byte[] transformL(byte[] in_data) {
		byte[] out_data;
		byte[] internal = in_data;
		
		for (int i = 0; i < 16; i++) internal = transformR(internal);
		
		out_data = internal;
		return out_data;
	}
	
	static public byte[] transformLReverse(byte[] in_data) {
		byte[] out_data;
		byte[] internal = in_data;
		
		for (int i = 0; i < 16; i++) internal = transformRReverse(internal);
		
		out_data = internal;
		return out_data;
	}
	
	// Функция расчета констант
	/* Константы на то и константы, что их можно посчитать и выписать
	static private void calcIterC() {
		byte[][] iter_num = new byte[32][16];
		for (int i = 0; i < 32; i++) {
			for(int j = 0; j < BLOCK_SIZE; j++) iter_num[i][j] = 0;
			iter_num[i][0] = (byte) (i+1);
		}
		System.out.println("\nConstants: \n");
		for (int i = 0; i < 32; i++) {
			ITER_C[i] = transformL(iter_num[i]);
			System.out.println(Utils.byteArrToHexStr(ITER_C[i], true));
		}
	}
	*/
	
	// Функция, выполняющая преобразования ячейки Фейстеля
	static private byte[][] transformFeystel(byte[] in_key_0, byte[] in_key_1, byte[] iter_const) {
		byte[] internal;
		byte[][] key = new byte[2][];
		key[1] = in_key_0;
		
		internal = xor(in_key_0, iter_const);
		internal = transformS(internal, false);
		internal = transformL(internal);
		key[0] = xor(internal, in_key_1);
		return key;
	}
	
	// Функция расчета раундовых ключей
	void expandKey(byte[] key_1, byte[] key_2) {
		byte[][] iter12 = new byte[2][];
		byte[][] iter34;
		
//		calcIterC();
		iterKey[0] = key_1;
		iterKey[1] = key_2;
		iter12[0] = key_1;
		iter12[1] = key_2;
		for (int i = 0; i < 4; i++) {
			iter34 = transformFeystel(iter12[0], iter12[1], ITER_C[0 + 8 * i]);
			iter12 = transformFeystel(iter34[0], iter34[1], ITER_C[1 + 8 * i]);
			iter34 = transformFeystel(iter12[0], iter12[1], ITER_C[2 + 8 * i]);
			iter12 = transformFeystel(iter34[0], iter34[1], ITER_C[3 + 8 * i]);
			iter34 = transformFeystel(iter12[0], iter12[1], ITER_C[4 + 8 * i]);
			iter12 = transformFeystel(iter34[0], iter34[1], ITER_C[5 + 8 * i]);
			iter34 = transformFeystel(iter12[0], iter12[1], ITER_C[6 + 8 * i]);
			iter12 = transformFeystel(iter34[0], iter34[1], ITER_C[7 + 8 * i]);
			
			iterKey[2 * i + 2] = iter12[0];
			iterKey[2 * i + 3] = iter12[1];
		}
	}
	
	// Функция установки ключа
	public boolean setKey(byte[] key) {
		if (key.length != VALID_KEY_LEN) return false;
		byte[] keyLeft = Arrays.copyOfRange(key, 0, VALID_KEY_LEN / 2);
		byte[] keyRight = Arrays.copyOfRange(key, VALID_KEY_LEN / 2, VALID_KEY_LEN);
		expandKey(keyLeft, keyRight);
		return true;
	}
	
	public void setIterKey(byte[][] iterKey){ this.iterKey = iterKey;}
	
	public byte[][] getIterKey(){ return this.iterKey;}
	
	
	// Функция шифрования блока на 1 раунд
	public byte[] encryptOneRoundZeroKey(byte[] blk) {
		byte[] out_blk = blk;
		out_blk = transformS(out_blk, false);
		out_blk = transformL(out_blk);
		return out_blk;
	}
	public byte[] decryptOneRoundZeroKey(byte[] blk) {
		byte[] out_blk = blk;
		out_blk = transformLReverse(out_blk);
		out_blk = transformS(out_blk, true);
		return out_blk;
	}

	
	public byte[] encryptPrintAllSteps(byte[] blk, int numberOfRounds) {
		if (numberOfRounds < 1 || numberOfRounds > 9) {
			System.err.println("Invalid number of rounds. For full encrypt call encryptFull(byte[] blk)");
			return blk;
		}
		byte[] out_blk = blk;
		
		for(int i = 0; i < numberOfRounds; i++) {
			System.out.println("Round #" + i);
			System.out.println("in = \n" + Utils.byteArrToHexStr(out_blk));
			out_blk = xor(iterKey[i], out_blk);
			System.out.println(
					"xor(iterKey[i], out_blk) = \n" +
					Utils.byteArrToHexStr(out_blk)
			);
			out_blk = transformS(out_blk, false);
			System.out.println(
					"funcS(out_blk, false) = \n" +
					Utils.byteArrToHexStr(out_blk)
			);
			out_blk = transformL(out_blk);
			System.out.println(
					"transformL(out_blk) = \n" +
					Utils.byteArrToHexStr(out_blk)
			);
		}
		System.out.println();
		return out_blk;
	}
		
		// Функция шифрования блока
	public byte[] encryptFull(byte[] blk) {
		return xor(encrypt(blk, 9), iterKey[9]);
	}
	
	// Функция расшифрования блока
	public byte[] decryptFull(byte[] blk) {
		byte[] out_blk = blk;
		
		out_blk = xor(out_blk, iterKey[9]);
		for(int i = 8; i >= 0; i--) {
			out_blk = transformLReverse(out_blk);
			out_blk = transformS(out_blk, true);
			out_blk = xor(iterKey[i], out_blk);
		}
		return out_blk;
	}
	
	// Функция расшифрования блока
	public byte[] decrypt(byte[] blk, int numberOfRounds) {
		byte[] out_blk = blk;
		
		if (numberOfRounds == 9) {
			out_blk = xor(out_blk, iterKey[9]);
			numberOfRounds --;
		}
		
		for(int i = numberOfRounds; i >= 0; i--) {
			out_blk = transformLReverse(out_blk);
			out_blk = transformS(out_blk, true);
			out_blk = xor(iterKey[i], out_blk);
		}
		return out_blk;
	}
	// Функция шифрования блока на numberOfRounds раундов
	public byte[] encrypt(byte[] blk, int numberOfRounds) {
		if (numberOfRounds < 1 || numberOfRounds > 9) {
			System.err.println("Invalid number of rounds. For full encrypt call encryptFull(byte[] blk)");
			return blk;
		}
		byte[] out_blk = blk;
		for(int i = 0; i < numberOfRounds; i++) {
			out_blk = xor(iterKey[i], out_blk);
			out_blk = transformS(out_blk, false);
			out_blk = transformL(out_blk);
		}
		return out_blk;
	}
	
	public byte[][] decryptPerRound(byte[] blk, int numberOfRounds) {
		if (numberOfRounds < 1 || numberOfRounds > 9) {
			System.err.println(numberOfRounds + "is invalid number of rounds");
			return new byte[][]{blk};
		}
		
		byte[][] out_blk = new byte[numberOfRounds + 1][BLOCK_SIZE];
		
		if (numberOfRounds == 9) {
			out_blk[numberOfRounds] = xor(out_blk[numberOfRounds], iterKey[numberOfRounds]);
		}
		
		out_blk[0] = blk;
		for(int i = 1; i < numberOfRounds + 1; i++) {
			out_blk[i] = transformLReverse(out_blk[i - 1]);
			out_blk[i] = transformS(out_blk[i], true);
			out_blk[i] = xor(iterKey[i], out_blk[i]);
		}

		return out_blk;
	}
	
	
	// Функция шифрования блока на numberOfRounds раундов
	public byte[][] encryptPerRound(byte[] blk, int numberOfRounds) {
		if (numberOfRounds < 1 || numberOfRounds > 9) {
			System.err.println(numberOfRounds + "is invalid number of rounds");
			return new byte[][]{blk};
		}
		byte[][] out_blk = new byte[numberOfRounds + 1][BLOCK_SIZE];
		out_blk[0] = blk;
		for(int i = 1; i < numberOfRounds + 1; i++) {
			out_blk[i] = xor(iterKey[i], out_blk[i - 1]);
			out_blk[i] = transformS(out_blk[i], false);
			out_blk[i] = transformL(out_blk[i]);
		}
		if (numberOfRounds == 9) {
			out_blk[numberOfRounds] = xor(out_blk[numberOfRounds], iterKey[numberOfRounds]);
		}
		return out_blk;
	}
}
