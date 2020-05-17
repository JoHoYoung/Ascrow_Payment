package com.sharescrow.sample.service;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import com.sharescrow.sample.common.exception.ApplicationError;
import com.sharescrow.sample.common.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobExecuteService {
  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  @Qualifier("syncJobLauncher")
  private JobLauncher jobLauncher;
  public String executeJob(String jobName, Map<String, String> parameters) {
    Job job = (Job)applicationContext.getBean(jobName);
    JobParametersBuilder builder = new JobParametersBuilder();
    parameters.forEach(builder::addString);
    JobParameters jobParameters = builder.toJobParameters();
    try {
      JobExecution jobExecution = jobLauncher.run(job, jobParameters);
      return String.format("JobName:[%s], executionId:[%s], instanceId:[%s], exitStatus[%s]"
        , jobName, jobExecution.getId(), jobExecution.getJobInstance().getInstanceId(), jobExecution.getExitStatus().getExitCode());
    } catch (Exception e) {
      log.error(String.format("job 실행 실패 JobName:[%s]", jobName));
      throw new ApplicationException(ApplicationError.FAIL_TO_START_JOB);
    }
  }
}
