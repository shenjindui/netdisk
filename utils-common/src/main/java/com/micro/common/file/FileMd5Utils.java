package com.micro.common.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileMd5Utils {
	private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
	
	protected static String calcMD5(File file) {
		try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] buf = new byte[1024];
			int len;
			while ((len = stream.read(buf)) > 0) {
				digest.update(buf, 0, len);
			}
			return toHexString(digest.digest());
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String toHexString(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(hexCode[(b >> 4) & 0xF]);
			r.append(hexCode[(b & 0xF)]);
		}
		return r.toString();
	}
}
