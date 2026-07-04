package com.pranay.inventory.DTO;

import com.pranay.inventory.model.Category;

public record SaleEntryResponseDTO(
        int id,
        Category category,
        String designName,
        String size,
        String type,
        int itemId,
        int boxes,
        int pieces,
        double unitPrice,
        double totalPrice
) {
}
