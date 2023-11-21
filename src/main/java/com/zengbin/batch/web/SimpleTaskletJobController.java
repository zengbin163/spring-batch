package com.zengbin.batch.web;

import com.alibaba.fastjson.JSON;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/simpleTasklet")
public class SimpleTaskletJobController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    int flag=1;

    private Tasklet tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("-------------------->" + "第" + flag + "次" + System.currentTimeMillis());
                System.out.println("-------------------->" + "第" + flag + "次" + JSON.toJSONString(stepContribution));
                System.out.println("-------------------->" + "第" + flag + "次" + JSON.toJSONString(chunkContext));
                //return RepeatStatus.CONTINUABLE;  //循环执行
                while(flag < 10) {
                    flag = flag + 1;
                    return RepeatStatus.CONTINUABLE;
                }
                return RepeatStatus.FINISHED;
            }
        };
    }

    private Step step(String stepName){
        return stepBuilderFactory.get(stepName)
                .tasklet(tasklet())
                .build();
    }

    public Job job(String jobName, String stepName){
        return jobBuilderFactory.get(jobName)
                .start(step(stepName))
                .build();
    }

    @GetMapping("/run")
    public String run(@RequestParam(name="jobName") String jobName, @RequestParam(name="stepName") String stepName) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(job(jobName, stepName), new JobParameters());
        return "success";
    }

}
