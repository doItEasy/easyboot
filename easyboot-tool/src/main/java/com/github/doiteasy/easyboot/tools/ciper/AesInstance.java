package com.github.doiteasy.easyboot.tools.ciper;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AESUtil instance with one key and one iv for preformance.
 * mode: AESUtil/CBC/PKCS5Padding
 * text input encoding: utf-8
 * text output encoding: base64
 *
 */
public class AesInstance {
		private static byte[] defualtkey = { 1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6 };
		private static byte[] defualtiv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		private AesInstance(byte[] key,byte[] iv) throws Exception {
			this.encCipher = Cipher.getInstance("AESUtil/CBC/PKCS5Padding");
			this.decCipher = Cipher.getInstance("AESUtil/CBC/PKCS5Padding");
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AESUtil");
			IvParameterSpec ivc =  new IvParameterSpec(iv);
			this.encCipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivc);
			this.decCipher.init(Cipher.DECRYPT_MODE, skeySpec, ivc);
		}
    	private Cipher encCipher =  null;
    	private Cipher decCipher =  null;
    	
    	public byte[] encBytes(byte[] srcBytes) throws Exception{
    		byte[] encrypted = encCipher.doFinal(srcBytes);
			return encrypted;
    	}
    	public byte[] decBytes(byte[] srcBytes) throws Exception{
    		byte[] decrypted = decCipher.doFinal(srcBytes);
			return decrypted;
    	}
    	public String encText(String srcStr) throws Exception {
			byte[] srcBytes = srcStr.getBytes("utf-8");
			byte[] encrypted = encBytes(srcBytes);
			return Base64.encode(encrypted);
	  	}
    	public String decText(String srcStr) throws Exception {
			byte[] srcBytes = Base64.decode(srcStr);
			byte[] decrypted = decBytes(srcBytes);
			return new String(decrypted,"utf-8");
	  	}

		public static final AesInstance getInstance() throws Exception{
			AesInstance sub = new AesInstance(AesInstance.defualtkey,AesInstance.defualtiv);
			return sub;
		}
		public static final AesInstance getInstance(byte[] key,byte[] iv) throws Exception{
			AesInstance sub = new AesInstance(key,iv);
			return sub;
		}
  }
    
   
