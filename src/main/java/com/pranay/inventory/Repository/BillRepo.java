package com.pranay.inventory.Repository;

import com.pranay.inventory.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepo extends JpaRepository<Bill, Integer> {
    @Query(value = "SELECT bill.* " +
            " FROM bill " +
            "INNER JOIN sale_entry ON bill.id = sale_entry.bill_id " +
            "INNER JOIN items ON items.id = sale_entry.items_id " +
            "WHERE items.id = :id",
            nativeQuery = true)
    List<Bill> findBillByItemId(@Param("id") int id);

}
