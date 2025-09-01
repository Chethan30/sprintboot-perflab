package com.example.perflab.dto;

import java.util.List;

public record ItemDto(Long id, String name, int priceCents, List<SpecDto> specs, List<StatDto> stats) {
    public record SpecDto(String key, String value) {}
    public record StatDto(String name, long value) {}
}