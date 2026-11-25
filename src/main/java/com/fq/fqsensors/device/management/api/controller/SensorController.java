package com.fq.fqsensors.device.management.api.controller;

import com.fq.fqsensors.device.management.api.client.impl.SensorMonitoringClientImpl;
import com.fq.fqsensors.device.management.api.model.SensorDetails;
import com.fq.fqsensors.device.management.api.model.SensorMonitoringOutput;
import com.fq.fqsensors.device.management.domain.model.SensorId;
import com.fq.fqsensors.device.management.api.model.SensorOutput;
import com.fq.fqsensors.device.management.common.IdGenerator;
import com.fq.fqsensors.device.management.api.model.SensorInput;
import com.fq.fqsensors.device.management.domain.model.Sensor;
import com.fq.fqsensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository repository;

    private final SensorMonitoringClientImpl sensorMonitoringClient;

    @PostMapping
    public ResponseEntity<SensorOutput> create(@RequestBody @NonNull SensorInput input){
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

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(sensor.getId())
                .toUri();

        return  ResponseEntity.created(uri).body(SensorOutput.create(sensor));
    }

    @GetMapping("{sensorId}")
    public ResponseEntity<SensorOutput> getOne(@PathVariable TSID sensorId){
     Sensor sensor =   repository.findById(new SensorId(sensorId))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(SensorOutput.create(sensor));
    }

    @GetMapping
    public ResponseEntity<Page<SensorOutput>> search(Pageable pageable){
        Page<Sensor> sensors = repository.findAll(pageable);
        return ResponseEntity.ok(sensors.map(SensorOutput::create));
    }

    @GetMapping("{sensorId}/detail")
    public ResponseEntity<SensorDetails> getOneWithDetail(@PathVariable TSID sensorId){
        Sensor sensor =   repository.findById(new SensorId(sensorId))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        SensorMonitoringOutput sensorOutput = sensorMonitoringClient.getDetails(sensorId);
        SensorDetails details = SensorDetails.builder().monitoring(sensorOutput)
                .sensor(SensorOutput.create(sensor)).build();


        return ResponseEntity.ok(details);
    }

    @PutMapping("{sensorId}")
    public ResponseEntity<SensorOutput> update(@PathVariable TSID sensorId,@Valid @RequestBody SensorInput input){
        Sensor sensor = repository.findById(new SensorId(sensorId))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sensor.setName(input.name());
        sensor.setIp(input.ip());
        sensor.setLocation(input.location());
        sensor.setProtocol(input.protocol());
        sensor.setModel(input.model());

       return ResponseEntity.ok(SensorOutput.create(repository.saveAndFlush(sensor)));
    }

    @DeleteMapping("{sensorId}")
    public ResponseEntity<SensorOutput> delete(@PathVariable TSID sensorId){
        Sensor sensor = repository.findById(new SensorId(sensorId))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        repository.delete(sensor);

        sensorMonitoringClient.disableMonitoring(sensorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{sensorId}/enable")
    public ResponseEntity<SensorOutput> enable(@PathVariable TSID sensorId){
        Sensor sensor = repository.findById(new SensorId(sensorId))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sensor.setEnebled(true);
        repository.saveAndFlush(sensor);
        sensorMonitoringClient.enableMonitoring(sensorId);


        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{sensorId}/enable")
    public ResponseEntity<SensorOutput> disable(@PathVariable TSID sensorId){
        Sensor sensor = repository.findById(new SensorId(sensorId))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sensor.setEnebled(false);
        repository.saveAndFlush(sensor);
        sensorMonitoringClient.disableMonitoring(sensorId);
        return ResponseEntity.noContent().build();
    }
}
