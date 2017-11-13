package com.newcore.orbps.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.halo.core.exception.FileAccessException;
import com.halo.core.filestore.api.FileStoreService;
import com.newcore.orbps.service.api.ResourceReaderFactory;

/**
 * 读resourceid，创建BufferedReader.
 * 
 * @author liushuaifeng
 *
 *         创建时间：2016年9月19日下午9:22:39
 */

public class FileStoreReaderFactory implements ResourceReaderFactory {

	private static Logger logger = LoggerFactory.getLogger(FileStoreReaderFactory.class);

	@Resource
	FileStoreService fileStoreService;
	
	@Override
	public BufferedReader create(String resourceId, String encoding) {

		BufferedReader bufferedReader = null;
		try {
			File file = fileStoreService.getFile(resourceId);
			FileInputStream in=new FileInputStream(file);
			bufferedReader = new BufferedReader(new InputStreamReader(in, encoding));
			
		} catch (FileAccessException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return bufferedReader;
	}
}
