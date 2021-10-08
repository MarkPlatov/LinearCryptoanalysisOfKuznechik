package kuznechik;

import utils.Utils;

public class Main {
    
    static byte[] key = {
            (byte) 0x77, (byte) 0x66, (byte) 0x55, (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11, (byte) 0x00,
            (byte) 0xff, (byte) 0xee, (byte) 0xdd, (byte) 0xcc, (byte) 0xbb, (byte) 0xaa, (byte) 0x99, (byte) 0x88,
            (byte) 0xef, (byte) 0xcd, (byte) 0xab, (byte) 0x89, (byte) 0x67, (byte) 0x45, (byte) 0x23, (byte) 0x01,
            (byte) 0x10, (byte) 0x32, (byte) 0x54, (byte) 0x76, (byte) 0x98, (byte) 0xba, (byte) 0xdc, (byte) 0xfe,
    };
    

    static byte[] blk = {
            (byte) 0x88, (byte) 0x99, (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff,
            (byte) 0x00, (byte) 0x77, (byte) 0x66, (byte) 0x55, (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11,
    };
    
    
    public static void main(String[] args)
    {
        Crypt crypt = new Crypt();
        if (!crypt.kuzSetKey(key)) {
            System.out.println("Invalid key");
            return;
        }
        System.out.printf("\n\nSource data: \n%s", Utils.byteArrToHexStr(blk, false));
        
        byte[] encryptBlock = crypt.kuzEncrypt(blk);
        System.out.printf("\n\nEncrypted data: \n%s", Utils.byteArrToHexStr(encryptBlock, false));
        
        byte[] decryptBlock = crypt.kuzDecrypt(encryptBlock);
        System.out.printf("\n\nDecrypted data: \n%s", Utils.byteArrToHexStr(decryptBlock, false));
    }
}
    /* __Valid Values__
    Key :
        {
        (byte) 0x77, (byte) 0x66, (byte) 0x55, (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11, (byte) 0x00,
        (byte) 0xff, (byte) 0xee, (byte) 0xdd, (byte) 0xcc, (byte) 0xbb, (byte) 0xaa, (byte) 0x99, (byte) 0x88,
        (byte) 0xef, (byte) 0xcd, (byte) 0xab, (byte) 0x89, (byte) 0x67, (byte) 0x45, (byte) 0x23, (byte) 0x01,
        (byte) 0x10, (byte) 0x32, (byte) 0x54, (byte) 0x76, (byte) 0x98, (byte) 0xba, (byte) 0xdc, (byte) 0xfe,
        };
    Source data:
        {0x88, 0x99, 0xaa, 0xbb, 0xcc, 0xdd, 0xee, 0xff, 0x0, 0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11}
        
    Encrypted data:
        {0xcd, 0xed, 0xd4, 0xb9, 0x42, 0x8d, 0x46, 0x5a, 0x30, 0x24, 0xbc, 0xbe, 0x90, 0x9d, 0x67, 0x7f}
    */