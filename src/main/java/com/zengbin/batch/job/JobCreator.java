package com.zengbin.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengbin
 */
@Component
public class JobCreator {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    public Job create(String jobName, List<Step> steps) {
        Flow flow = this.createFlow(jobName + "-Flow", steps);
        return jobBuilderFactory
                .get(jobName)
                .start(flow)
                .end()
                .build();
    }

    private Flow createFlow(String flowName, List<Step> steps) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(flowName);
        flowBuilder = flowBuilder.start(steps.get(0));
        for(int i=1;i<steps.size();i++) {
            flowBuilder = flowBuilder.next(steps.get(i));
        }
        return flowBuilder.build();
    }

}
