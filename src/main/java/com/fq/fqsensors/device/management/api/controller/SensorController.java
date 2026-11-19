package com.fq.fqsensors.device.management.api.controller;

import com.fq.fqsensors.device.management.api.model.SensorId;
import com.fq.fqsensors.device.management.api.model.SensorOutput;
import com.fq.fqsensors.device.management.common.IdGenerator;
import com.fq.fqsensors.device.management.api.model.SensorInput;
import com.fq.fqsensors.device.management.domain.model.Sensor;
import com.fq.fqsensors.device.management.domain.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput input){
        Sensor sensor = Sensor.builder()
                .id(new SensorId(IdGenerator.genereteTSID()))
                .ip(input.ip())
                .name(input.name())
                .location(input.location())
                .protocol(input.protocol())
                .model(input.model())
                .enebled(false)
                .build();

        repository.saveAndFlush(sensor);
        return  SensorOutput.create(sensor);
    }
}
