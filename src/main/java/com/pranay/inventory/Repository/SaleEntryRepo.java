package com.pranay.inventory.Repository;

import com.pranay.inventory.model.SaleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleEntryRepo extends JpaRepository<SaleEntry,Integer> {
    List<SaleEntry> findByBill_IdIsNull();
    List<SaleEntry> findByItems_IdAndBill_IdIsNull(int id);
}
