package com.example.perflab;

import com.example.perflab.dto.ItemDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private final ItemService svc;

    @GetMapping("/{id}")
    public ItemDto getOne(@PathVariable Long id) { return svc.getItemFlatRead(id); }
}
