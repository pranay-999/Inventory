package com.pranay.inventory.DTO;

import com.pranay.inventory.model.Category;

public record ItemRequestDTO(
        Category category,
        String designName,
        String size,
        String type,
        int initialBoxes,
        int pieces

) {
}
