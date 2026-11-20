package com.fq.fqsensors.device.management.api.controller;

import com.fq.fqsensors.device.management.domain.model.SensorId;
import com.fq.fqsensors.device.management.api.model.SensorOutput;
import com.fq.fqsensors.device.management.common.IdGenerator;
import com.fq.fqsensors.device.management.api.model.SensorInput;
import com.fq.fqsensors.device.management.domain.model.Sensor;
import com.fq.fqsensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("{sensorId}")
    public SensorOutput getOne(@PathVariable TSID sensorId){
     Sensor sensor =   repository.findById(new SensorId(sensorId))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return SensorOutput.create(sensor);
    }

    @GetMapping
    public Page<SensorOutput> search(Pageable pageable){
        Page<Sensor> sensors = repository.findAll(pageable);
        return sensors.map(SensorOutput::create);
    }
}
