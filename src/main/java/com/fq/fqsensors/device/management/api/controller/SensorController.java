package com.fq.fqsensors.device.management.api.controller;

import com.fq.fqsensors.device.management.common.IdGenerator;
import com.fq.fqsensors.device.management.api.model.SensorInput;
import com.fq.fqsensors.device.management.domain.model.Sensor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sensors")
public class SensorController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sensor create(@RequestBody SensorInput sensor){
        return Sensor.builder()
                .id(IdGenerator.genereteTSID())
                .ip(sensor.ip())
                .name(sensor.name())
                .location(sensor.location())
                .protocol(sensor.protocol())
                .model(sensor.model())
                .enebled(false)
                .build();
    }
}
