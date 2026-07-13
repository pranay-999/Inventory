package com.pranay.inventory.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record BillResponseDTO(
        String BillId,
        String customerNamee,
        String phone,
        List<SaleEntryResponseDTO> salesEntries,
        double grandTotal,
        LocalDateTime dateAndTime
) {
}
