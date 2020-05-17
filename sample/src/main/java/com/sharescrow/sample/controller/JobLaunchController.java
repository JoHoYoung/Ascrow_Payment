package com.sharescrow.sample.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharescrow.sample.model.response.BaseResponse;
import com.sharescrow.sample.service.JobExecuteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/batch/jobs")
public class JobLaunchController extends BaseRestController {

  @Autowired
  private JobExecuteService jobExecuteService;

  @PostMapping("/{jobName}/launch")
  public BaseResponse<String> startJobByJobName(
    @PathVariable String jobName,
    @RequestParam Map<String, String> parameters
  ) {
    return new BaseResponse<>(jobExecuteService.executeJob(jobName, parameters));
  }
}
