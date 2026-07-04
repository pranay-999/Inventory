package com.pranay.inventory.DTO;


public record SaleEntryRequestDTO(
        int itemId,
        int boxes,
        int pieces,
        double unitPrice
) {
}
