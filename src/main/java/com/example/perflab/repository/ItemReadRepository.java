package com.example.perflab.repository;

import com.example.perflab.dto.read.ItemFlatRow;
import com.example.perflab.entity.Item;
import com.example.perflab.entity.ItemSpec;
import com.example.perflab.entity.ItemStat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemReadRepository extends Repository<Item, Long> {
    @Query("""
    select new com.example.perflab.dto.read.ItemFlatRow(
      a.id,
      a.name,
      a.priceCents,
      s.keyName,
      s.valueText,
      st.metricName,
      st.metricValue
    )
    from Item a
    left join a.specs s
    left join a.stats st
    where a.id = :id
  """)
    List<ItemFlatRow> readFlattened(@Param("id") Long id);

    interface BaseProj {
        Long getId();
        String getName();
        Integer getPriceCents();
    }

    interface SpecProj {
        String getKeyName();
        String getValueText();
    }

    interface StatProj {
        String getMetricName();
        Long getMetricValue();
    }

    @Query("select a.id as id, a.name as name, a.priceCents as priceCents from Item a where a.id = :id")
    Optional<BaseProj> readBase(@Param("id") Long id);

    @Query("select s.keyName as keyName, s.valueText as valueText from ItemSpec s where s.item.id = :id")
    List<SpecProj> readSpecs(@Param("id") Long id);

    @Query("select st.metricName as metricName, st.metricValue as metricValue from ItemStat st where st.item.id = :id")
    List<StatProj> readStats(@Param("id") Long id);
}
