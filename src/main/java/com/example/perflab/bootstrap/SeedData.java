package com.example.perflab.bootstrap;

import com.example.perflab.entity.Item;
import com.example.perflab.entity.ItemSpec;
import com.example.perflab.entity.ItemStat;
import com.example.perflab.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class SeedData {
    @Bean
    CommandLineRunner init(ItemRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            Random r = new Random(1);
            List<Item> batch = new ArrayList<>();
            for (int i = 1; i <= 5000; i++) {
                Item it = new Item();
                it.setName("Item-" + i);
                it.setPriceCents(r.nextInt(10_000) + 100);

                List<ItemSpec> specs = new ArrayList<>();
                for (int s = 0; s < 5; s++) {
                    ItemSpec sp = new ItemSpec();
                    sp.setItem(it);
                    sp.setKeyName("k" + s);
                    sp.setValueText("v" + r.nextInt(1000));
                    specs.add(sp);
                }
                it.setSpecs(specs);

                List<ItemStat> stats = new ArrayList<>();
                for (int s = 0; s < 3; s++) {
                    ItemStat st = new ItemStat();
                    st.setItem(it);
                    st.setMetricName("m" + s);
                    st.setMetricValue(r.nextInt(10_000));
                    stats.add(st);
                }
                it.setStats(stats);
                batch.add(it);
                if (batch.size() == 200) { repo.saveAll(batch); batch.clear(); }
            }
            if (!batch.isEmpty()) repo.saveAll(batch);
        };
    }
}