package com.newcore.orbps.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author wangxiao
 * 创建时间：2016年9月20日上午9:40:29
 */
public class DecompressUtils {
	private DecompressUtils(){
	}
	public static File unZip(String sourceDirectory,String targetDirectory,String applNo) throws IOException{
		//目标文件
		String targetFile = applNo + ".csv";
		ZipInputStream zis = null;
		File target = null;
		BufferedOutputStream dest = null;
		try {
			zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File(sourceDirectory))));
			target = new File(targetDirectory,targetFile);
			while (zis.getNextEntry() != null) {
				if (!target.exists()) {
					target.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(target);
				dest = new BufferedOutputStream(fos);
				IOUtils.copy(zis, dest);
				dest.flush();
				dest.close();
			}
			zis.close();			
			return target;
			
		} finally {
			if (null != zis) {
				zis.close();				
			}
			if (null != dest) {
				dest.close();
			}
		}
	
	}
}
