package com.zengbin.batch.quartz;

import com.zengbin.batch.job.JobCreator;
import com.zengbin.batch.step.StepCreator;
import com.zengbin.batch.vo.Sharding;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodQuartzJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(FoodQuartzJob.class);
    @Autowired
    private StepCreator stepCreator;
    @Autowired
    private JobCreator jobCreator;
    @Autowired
    private JobLauncher jobLauncher;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Long threadId = Thread.currentThread().getId();
        logger.info("----------------------------Quartz Job execute begin, threadId={}", threadId);
        try {
            String beginTimeStr = "20221230000000";
            String endTimeStr = "20231230235959";
            String jobName = "QuarzJobName-" + "{" + beginTimeStr + "}->{" + endTimeStr + "}";
            List<Step> stepList = stepCreator.createList(beginTimeStr, endTimeStr);
            Job job = jobCreator.create(jobName, stepList);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
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
        logger.info("----------------------------Quartz Job execute end, threadId={}", threadId);
    }

}
