package com.fq.fqsensors.device.management.api.model;

import lombok.Builder;

@Builder
public record SensorDetails(
        SensorOutput sensor,
        SensorMonitoringOutput monitoring
) {
}
