package com.example.perflab.repository;

import com.example.perflab.entity.Item;
import com.example.perflab.entity.ItemSpec;
import com.example.perflab.entity.ItemStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.id = :id")
    Optional<Item> findBase(@Param("id") Long id);

    @Query("select s from ItemSpec s where s.item.id = :id")
    List<ItemSpec> findSpecs(@Param("id") Long id);

    @Query("select st from ItemStat st where st.item.id = :id")
    List<ItemStat> findStats(@Param("id") Long id);

}
