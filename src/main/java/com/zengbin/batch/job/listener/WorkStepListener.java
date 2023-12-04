package com.zengbin.batch.job.listener;

import com.zengbin.batch.job.config.JobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class WorkStepListener implements StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(WorkStepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Long threadId = Thread.currentThread().getId();
        logger.info("------WorkStepListener.beforeStep begin execute, threadId={}", threadId);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        Long threadId = Thread.currentThread().getId();
        logger.info("------WorkStepListener.afterStep begin execute, threadId={}", threadId);
        return ExitStatus.COMPLETED;
    }

}
