package com.newcore.orbps.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.util.IOUtils;
import com.halo.core.exception.BusinessException;

public class FileZip {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(FileZip.class);
	static final Integer BUFFER = 8192;

	private File zipFile;

	public FileZip(String pathName) {
		zipFile = new File(pathName);
	}

	public void compress(String srcPathName) throws IOException {
		File file = new File(srcPathName);
		if (!file.exists()){
			return;
		}
		FileOutputStream fileOutputStream = null;
		ZipOutputStream out = null;
		try {
			fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			out= new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			IOUtils.close(out);
			IOUtils.close(fileOutputStream);
		} finally {
			IOUtils.close(out);
			IOUtils.close(fileOutputStream);
		}
		
	}

	private void compress(File file, ZipOutputStream out, String basedir)
			throws IOException {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			System.out.println("压缩：" + basedir + file.getName());
			this.compressDirectory(file, out, basedir);
		} else {
			System.out.println("压缩：" + basedir + file.getName());
			this.compressFile(file, out, basedir);
		}
	}

	/**
	 * 压缩一个目录
	 * 
	 * @throws IOException
	 */
	private void compressDirectory(File dir, ZipOutputStream out, String basedir)
			throws IOException {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/**
	 * 压缩一个文件
	 * 
	 * @throws IOException
	 */
	private void compressFile(File file, ZipOutputStream out, String basedir)
			throws IOException {
		if (!file.exists()) {
			return;
		}
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
		} catch (IOException e) {
			logger.error("压缩文件失败",e);
			throw new BusinessException("");
		}
	}
}
