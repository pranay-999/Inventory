package com.pranay.inventory.Repository;

import com.pranay.inventory.model.PiecesPerBox;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PiecesPerBoxRepo extends JpaRepository<PiecesPerBox,Integer> {
    PiecesPerBox findBySizes(String size);
}
