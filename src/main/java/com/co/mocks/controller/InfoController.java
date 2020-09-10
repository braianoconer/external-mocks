package com.co.mocks.controller;


import com.co.mocks.config.ServiceConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class InfoController {


    @NonNull private final ServiceConfig serviceConfig;


    @GetMapping(path = "/env")
    public String getEnv() {

        return serviceConfig.getEnv();
    }

    @GetMapping(path = "/chart-version")
    public String getChartVersion() {

        return serviceConfig.getChartVersion();
    }

}
