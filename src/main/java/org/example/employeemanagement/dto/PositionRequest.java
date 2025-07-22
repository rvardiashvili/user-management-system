package org.example.employeemanagement.dto;

import lombok.Data;
import org.example.employeemanagement.domain.Position;

@Data
public class PositionRequest {
    private String name;
    private String description;

    public PositionRequest() {}
    public PositionRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public PositionRequest(Position position) {
        this.name = position.getName();
        this.description = position.getDescription();
    }
}