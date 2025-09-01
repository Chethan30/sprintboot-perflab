package com.example.perflab.dto.read;

public record ItemFlatRow(
        Long itemId,
        String itemName,
        int priceCents,
        String specKey,
        String specVal,
        String statName,
        Long statVal
) {}