package javaapplication190;

import java.io.FileInputStream;
import java.security.MessageDigest;

class digest_calc extends Thread{

    private String path_to_file;
    private String algorithm;

    digest_calc(String path_to_file, String algorithm) {

        this.algorithm = algorithm;
        this.path_to_file = path_to_file;
    }

    private void hash_calc () throws Exception{

        String datafile = this.path_to_file;
        String algorithm = this.algorithm;

		MessageDigest md = MessageDigest.getInstance(algorithm);
		FileInputStream fis = new FileInputStream(datafile);
		byte[] dataBytes = new byte[1024];

		int nread = 0; 

		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}

		byte[] mdbytes = md.digest();

		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		System.out.println(this.path_to_file  + " digest:: " + sb.toString());

    }

    public void run() {
        System.out.println("You choosed file: " + this.path_to_file + 
						   "  Algorithm is: " + this.algorithm);
        try {
            hash_calc();
        } catch (Exception ex) {
			ex.printStackTrace();
        }
	}
}

public class JavaApplication190 {
    public static void main(String[] args) throws Exception {

        String alg = "MD5"; // or may be value SHA1
        String path_to_file = "D:\\1.txt";

		digest_calc dc = new digest_calc(path_to_file, alg);
		dc.start();

	}
}
