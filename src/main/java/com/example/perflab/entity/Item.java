package com.example.perflab.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Table
@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int priceCents;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemSpec> specs;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemStat> stats;
}
