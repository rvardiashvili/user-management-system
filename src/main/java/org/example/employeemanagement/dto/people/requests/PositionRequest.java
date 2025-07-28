package org.example.employeemanagement.dto.people.requests;

import lombok.Data;
import org.example.employeemanagement.domain.people.Position;

@Data
public class PositionRequest {
    private String name;
    private String description;
    private Long unitId;

    public PositionRequest() {}
    public PositionRequest(String name, String description, Long unitId) {
        this.name = name;
        this.description = description;
        this.unitId = unitId;
    }
    public PositionRequest(Position position) {
        this.name = position.getName();
        this.description = position.getDescription();
        this.unitId = position.getUnit_id().getUnitId();
    }
}