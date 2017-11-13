package com.newcore.orbps.service.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcOperations;

import com.alibaba.fastjson.util.IOUtils;
import com.halo.core.filestore.api.FileStoreService;
import com.newcore.orbpsutils.validation.XLS2CSV;
import com.newcore.orbpsutils.validation.XLSX2CSV;



/*
 * 功能：对过来的excel,判断文件类型
 * 1.csv文件不做处理；
 * 2.xls、xlsx文件分别调用不同的函数处理，并且调用FileStoreService.updateResource 做文件更新
 * 
 * */
public class ExcelToCsvTasklet implements Tasklet{
	
	@Resource
    FileStoreService fileStoreService;
	
	@Resource
	JdbcOperations jdbcTemplate;
	
	String resourceId;

	private final String SQL = "UPDATE LIST_IMPORT_TASK_QUEUE SET EXT_KEY5 = ? WHERE LIST_PATH = ?";

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		File file = fileStoreService.getFile(resourceId);
		
		if(file.getName().endsWith("csv")){
			getColNum(file);
			return RepeatStatus.FINISHED;
		}
		
		String csvPath = file.getPath().substring(0, file.getPath().lastIndexOf("."))+".csv";
		if (file.getName().endsWith("xlsx")){
			XLSX2CSV xlsx2csv = new XLSX2CSV(file.getPath(), csvPath);
			xlsx2csv.process();
		} else {
			XLS2CSV xls2csv = new XLS2CSV(file.getPath(), csvPath);
			xls2csv.process();
		}
		File csvFile = new File(csvPath);
        
		fileStoreService.updateResource(resourceId, csvFile);			
		getColNum(csvFile);
		csvFile.delete();
		return RepeatStatus.FINISHED;
	}


	/**
	 * @return the resourceId
	 */
	public String getResourceId() {
		return resourceId;
	}


	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	private void getColNum(File file) throws IOException{
		int i = 0;
		try(BufferedReader in = new BufferedReader(new InputStreamReader( new FileInputStream(file)))){
			String line = null;
			while((line = in.readLine())!=null){
				if(!line.trim().matches(",+")){
					i++;
				}
			}
			if(i>0){
				i--;
			}
			IOUtils.close(in);
		}catch(IOException e){
			throw e;
		}
		jdbcTemplate.update(SQL,i,resourceId);
	}
}
