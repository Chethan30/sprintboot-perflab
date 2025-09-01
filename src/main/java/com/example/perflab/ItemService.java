package com.example.perflab;

import com.example.perflab.dto.ItemDto;
import com.example.perflab.dto.read.ItemFlatRow;
import com.example.perflab.entity.Item;
import com.example.perflab.repository.ItemReadRepository;
import com.example.perflab.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {

    @Autowired
    private final ItemRepository items;

    @Autowired
    private final ItemReadRepository itemReads;

    @Transactional(readOnly = true) // baseline read-only
    public ItemDto getItemNaive(Long id) {
        Item i = items.findById(id).orElseThrow();
        return new ItemDto(
                i.getId(), i.getName(), i.getPriceCents(),
                i.getSpecs().stream()
                        .map(s -> new ItemDto.SpecDto(s.getKeyName(), s.getValueText()))
                        .collect(Collectors.toList()),
                i.getStats().stream()
                        .map(s -> new ItemDto.StatDto(s.getMetricName(), s.getMetricValue()))
                        .collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    public ItemDto getItemLazyFetching(Long id) {
        var base = items.findBase(id).orElseThrow();
        var specs = items.findSpecs(id);
        var stats = items.findStats(id);
        return new ItemDto(
                base.getId(), base.getName(), base.getPriceCents(),
                specs.stream().map(s -> new ItemDto.SpecDto(s.getKeyName(), s.getValueText())).toList(),
                stats.stream().map(s -> new ItemDto.StatDto(s.getMetricName(), s.getMetricValue())).toList()
        );
    }

    @Transactional(readOnly = true)
    public ItemDto getItemFlatRead(Long id) {
        List<ItemFlatRow> rows = itemReads.readFlattened(id);
        if (rows.isEmpty()) throw new NoSuchElementException("Item not found: " + id);

        var r0 = rows.getFirst();
        // use LinkedHashMap to preserve stable order
        Map<String, String> specMap = new LinkedHashMap<>();
        Map<String, Long> statMap = new LinkedHashMap<>();

        for (var r : rows) {
            if (r.specKey() != null) {
                // last write wins; change policy if needed
                specMap.putIfAbsent(r.specKey(), r.specVal());
            }
            if (r.statName() != null) {
                statMap.putIfAbsent(r.statName(), r.statVal() == null ? 0L : r.statVal());
            }
        }
        var specs = specMap.entrySet().stream()
                .map(e -> new ItemDto.SpecDto(e.getKey(), e.getValue()))
                .toList();
        var stats = statMap.entrySet().stream()
                .map(e -> new ItemDto.StatDto(e.getKey(), e.getValue()))
                .toList();

        return new ItemDto(r0.itemId(), r0.itemName(), r0.priceCents(), specs, stats);
    }

    @Transactional(readOnly = true)
    public ItemDto getItemFlatReadNoCartesianProduct(Long id) {
        var base = itemReads.readBase(id).orElseThrow(() -> new NoSuchElementException("Item not found: " + id));

        var specs = itemReads.readSpecs(id).stream()
                .map(s -> new ItemDto.SpecDto(s.getKeyName(), s.getValueText()))
                .toList();

        var stats = itemReads.readStats(id).stream()
                .map(st -> new ItemDto.StatDto(st.getMetricName(), st.getMetricValue() == null ? 0L : st.getMetricValue()))
                .toList();

        int priceCents = base.getPriceCents() == null ? 0 : base.getPriceCents();

        return new ItemDto(base.getId(), base.getName(), priceCents, specs, stats);
    }
}
