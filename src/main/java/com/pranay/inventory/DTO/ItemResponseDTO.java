package com.pranay.inventory.DTO;

import com.pranay.inventory.model.Category;

public record ItemResponseDTO(
        Integer id,
        Category category,
        String design_name,
        String size,
        String type,
        int initialBoxes,
        int initialPiece,
        int availableBoxes,
        int availablePieces,
        int soldBoxes,
        int soldPieces
) {
}
