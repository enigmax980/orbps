package com.newcore.orbps.service.business;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BatchLaunch {
	
	private static Logger logger = LoggerFactory.getLogger(BatchLaunch.class);

	public static void main(String[] args){
		// TODO Auto-generated method stub
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:applicationContext-springbatch.xml");
        JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("importIpsnListJob");
        try {
            // JOB实行
            JobExecution result = launcher.run(job, new JobParametersBuilder().toJobParameters());
            
            // 运行结果输出
            System.out.println(result.toString());
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
        }finally {
			context.close();
		}
    }

	}


