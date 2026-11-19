package com.fq.fqsensors.device.management.api.model;

import com.fq.fqsensors.device.management.domain.model.Sensor;
import io.hypersistence.tsid.TSID;

public record SensorOutput (
        TSID id,
        String name,
        String ip,
        String location,
        String protocol,
        String model,
        Boolean enebled
){
    public static SensorOutput create(Sensor sensor){
        return new SensorOutput(SensorId.getTsidBySensorId(sensor.getId()), sensor.getName(),
                sensor.getIp(), sensor.getLocation(), sensor.getProtocol(), sensor.getProtocol(), sensor.getEnebled());
    }
}
