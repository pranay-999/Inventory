package com.pranay.inventory.Repository;

import com.pranay.inventory.model.Category;
import com.pranay.inventory.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Items, Integer> {

    Items findByDesignNameAndCategory(String designName, Category category);
    List<Items> findByCategory(Category category);
    List<Items> findBySize(String size);
    List<Items> findByType(String type);

}
