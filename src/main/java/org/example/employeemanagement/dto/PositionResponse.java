package org.example.employeemanagement.dto;

import lombok.Data;
import org.example.employeemanagement.domain.people.Position;

@Data
public class PositionResponse {
    private Long id;
    private String name;
    private String description;
    private Long unitId;


    public PositionResponse() {}
    public PositionResponse(String name, String description, Long unitId) {
        this.name = name;
        this.description = description;
        this.unitId = unitId;
    }
    public PositionResponse(Position position) {
        this.name = position.getName();
        this.description = position.getDescription();
        this.unitId = position.getUnit_id().getUnitId();
        this.id = position.getId();
    }
}