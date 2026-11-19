package com.fq.fqsensors.device.management.domain.repository;

import com.fq.fqsensors.device.management.api.model.SensorId;
import com.fq.fqsensors.device.management.domain.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, SensorId> {
}
