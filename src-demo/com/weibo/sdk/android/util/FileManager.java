package com.weibo.sdk.android.util;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class FileManager {
	public static void clearFolder(String folder) {
		File temFile = new File(folder);
		if (temFile.exists()) {
			if (temFile.isDirectory()) {
				File[] files = temFile.listFiles();
				for (File file : files) {
					deleteFolderOrFile(file.getAbsolutePath());
				}
			}
		}
	}
	public static void makeFolder(String path) {
		File folder = new File(path);
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdirs();
		}
	}
	public static void deleteFolderOrFile(String filePath) {
		File temFile = new File(filePath);
		if (temFile.exists()) {
			deleteFile(temFile);
		}
	}
	private static void deleteFile(File temFile) {
		if (temFile.exists()) {
			if (temFile.isDirectory()) {
				File[] files = temFile.listFiles();
				for (File file : files) {
					deleteFile(file);
				}
			}
			temFile.delete();
		}
	}

	public static String saveBitmapToFile(Bitmap bitmap, String path) {
		if (bitmap == null)
			return null;
		FileOutputStream fileOutputStream = null;
		try {
			File file = new File(path);
			makeFolder(file.getPath().substring(0,
					file.getPath().length() - file.getName().length()));
			fileOutputStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			return path;
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}

	public static Bitmap loadBitmapFromFile(String path) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(path);
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		}
		return bitmap;
	}
}
