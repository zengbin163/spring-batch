package com.zengbin.batch.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ItemListQuartzJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ItemListQuartzJob.class);
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("itemListJob")  //将spring容器中名为： csvToDBJob job对象注入当前变量中
    private Job itemListJob;
    @Autowired
    private JobExplorer jobExplorer;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Long threadId = Thread.currentThread().getId();
        logger.info("------ItemListQuartzJob begin execute, threadId={}", threadId);
        try {
            JobParameters parameters = new JobParametersBuilder(new JobParameters(), jobExplorer)
                    .addLong("time", System.currentTimeMillis())
                    .getNextJobParameters(itemListJob).toJobParameters();
            JobExecution run = jobLauncher.run(itemListJob, parameters);
            logger.info("------ItemListQuartzJob end execute, jobRunId={}", run.getId().toString());
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
