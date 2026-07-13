package com.pranay.inventory.DTO;

import java.util.List;

public record BillRequestDTO(
        String customerName,
        String phone,
        List<SaleEntryRequestDTO> salesEntries

) {
}
