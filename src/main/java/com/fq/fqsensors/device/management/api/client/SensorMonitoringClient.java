package com.fq.fqsensors.device.management.api.client;

import com.fq.fqsensors.device.management.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;

public interface SensorMonitoringClient {

    void enableMonitoring(TSID sensorId);
    void disableMonitoring(TSID sensorId);

    SensorMonitoringOutput getDetails(TSID sensorId);
}
