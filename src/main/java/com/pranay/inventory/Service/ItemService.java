package com.pranay.inventory.Service;

import com.pranay.inventory.DTO.ItemRequestDTO;
import com.pranay.inventory.DTO.ItemResponseDTO;
import com.pranay.inventory.Repository.ItemRepo;
import com.pranay.inventory.Repository.PiecesPerBoxRepo;
import com.pranay.inventory.model.Category;
import com.pranay.inventory.model.Items;
import com.pranay.inventory.model.PiecesPerBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private PiecesPerBoxRepo piecesPerBoxRepo;


    public ItemResponseDTO addItem(ItemRequestDTO req) {

        int num = piecesPerBoxRepo.findBySizes(req.size()).getPieces();
        int initialBoxes;

        if (req.category() == Category.TILES) {
            initialBoxes = (req.initialBoxes() * num) + req.pieces();

        } else {
            initialBoxes = req.pieces();
        }


        Items entry = new Items();
        entry.setCategory(req.category());
        entry.setDesignName(req.designName());
        entry.setSize(req.size());
        entry.setType(req.type());
        entry.setInitialStock(initialBoxes);
        entry.setSoldStock(0);
        entry.setInitialUnitPrice(0);

        Items saved = itemRepo.save(entry);
        System.out.println(initialBoxes);


        int boxes;
        int initialPieces;
        int availableBoxes;
        int availablePieces;
        int soldBoxes;
        int soldPieces;
        if (saved.getCategory() == Category.TILES) {

            boxes = saved.getInitialStock() / num;
            initialPieces = saved.getInitialStock() % num;

            soldBoxes = saved.getSoldStock() / num;
            soldPieces = saved.getSoldStock() % num;

            availableBoxes = (saved.getInitialStock() - saved.getSoldStock()) / num;
            availablePieces = (saved.getInitialStock() - saved.getSoldStock()) % num;

        } else {
            boxes = 0;
            initialPieces = saved.getInitialStock();

            soldBoxes = 0;
            soldPieces = saved.getSoldStock();

            availableBoxes = saved.getInitialStock() - saved.getSoldStock();
            availablePieces = saved.getInitialStock() - saved.getSoldStock();
        }

        ItemResponseDTO dto = new ItemResponseDTO(
                saved.getId(),
                saved.getCategory(),
                saved.getDesignName(),
                saved.getSize(),
                saved.getType(),
                boxes,
                initialPieces,
                availableBoxes,
                availablePieces,
                soldBoxes,
                soldPieces

        );
        return dto;


    }

    public ItemResponseDTO updateItem(ItemRequestDTO req, int id) {

        int num;
        int initialBoxes;

        if (req.category() == Category.TILES) {
            PiecesPerBox piecesPerBox = piecesPerBoxRepo.findBySizes(req.size());
            num = piecesPerBox.getPieces();
            initialBoxes = (req.initialBoxes() * num) + req.pieces();
        } else {
            initialBoxes = req.pieces();
            num = 1;
        }

        System.out.println("Category received: " + req.category());
        System.out.println("Is TILES: " + (req.category() == Category.TILES));

        Items entry = itemRepo.findById(id).orElseThrow();
        entry.setCategory(req.category());
        entry.setDesignName(req.designName());
        entry.setSize(req.size());
        entry.setType(req.type());
        entry.setInitialStock(initialBoxes);
        entry.setSoldStock(0);
        entry.setInitialUnitPrice(0);

        Items saved = itemRepo.save(entry);
        System.out.println(initialBoxes);


        int boxes;
        int initialPieces;
        int availableBoxes;
        int availablePieces;
        int soldBoxes;
        int soldPieces;
        if (saved.getCategory() == Category.TILES) {

            boxes = saved.getInitialStock() / num;
            initialPieces = saved.getInitialStock() % num;

            soldBoxes = saved.getSoldStock() / num;
            soldPieces = saved.getSoldStock() % num;

            availableBoxes = (saved.getInitialStock() - saved.getSoldStock()) / num;
            availablePieces = (saved.getInitialStock() - saved.getSoldStock()) % num;

        } else {
            boxes = 0;
            initialPieces = saved.getInitialStock();

            soldBoxes = 0;
            soldPieces = saved.getSoldStock();

            availableBoxes = saved.getInitialStock() - saved.getSoldStock();
            availablePieces = saved.getInitialStock() - saved.getSoldStock();
        }

        ItemResponseDTO dto = new ItemResponseDTO(
                saved.getId(),
                saved.getCategory(),
                saved.getDesignName(),
                saved.getSize(),
                saved.getType(),
                boxes,
                initialPieces,
                availableBoxes,
                availablePieces,
                soldBoxes,
                soldPieces

        );

        return dto;

    }

    public void deleteItem(int id) {
        itemRepo.deleteById(id);
    }

    public ItemResponseDTO searchByName(String designName, Category category) {
        Items item = itemRepo.findByDesignNameAndCategory(designName,category);
        System.out.println(designName);

        int num = piecesPerBoxRepo.findBySizes(item.getSize()).getPieces();
        int initialBoxes;
        int initialPieces;
        int availableBoxes;
        int availablePieces;
        int soldBoxes;
        int soldPieces;

        if (item.getCategory() == Category.TILES) {

            initialBoxes = item.getInitialStock() / num;
            initialPieces = item.getInitialStock() % num;

            soldBoxes = item.getSoldStock() / num;
            soldPieces = item.getSoldStock() % num;

            availableBoxes = (item.getInitialStock() - item.getSoldStock()) / num;
            availablePieces = (item.getInitialStock() - item.getSoldStock()) % num;

        } else {
            initialBoxes = 0;
            initialPieces = item.getInitialStock();

            soldBoxes = 0;
            soldPieces = item.getSoldStock();

            availableBoxes = item.getInitialStock() - item.getSoldStock();
            availablePieces = item.getInitialStock() - item.getSoldStock();
        }

        ItemResponseDTO dto = new ItemResponseDTO(
                item.getId(),
                item.getCategory(),
                item.getDesignName(),
                item.getSize(),
                item.getType(),
                initialBoxes,
                initialPieces,
                availableBoxes,
                availablePieces,
                soldBoxes,
                soldPieces
        );
        return dto;
    }

    public List<ItemResponseDTO> searchByCategory(Category category) {
        List<Items> items = itemRepo.findByCategory(category);
        List<ItemResponseDTO> res = new ArrayList<>();
        for(Items item : items) {
            int num = piecesPerBoxRepo.findBySizes(item.getSize()).getPieces();
            int initialBoxes;
            int initialPieces;
            int availableBoxes;
            int availablePieces;
            int soldBoxes;
            int soldPieces;

            if (item.getCategory() == Category.TILES) {

                initialBoxes = item.getInitialStock() / num;
                initialPieces = item.getInitialStock() % num;

                soldBoxes = item.getSoldStock() / num;
                soldPieces = item.getSoldStock() % num;

                availableBoxes = (item.getInitialStock() - item.getSoldStock()) / num;
                availablePieces = (item.getInitialStock() - item.getSoldStock()) % num;

            } else {
                initialBoxes = 0;
                initialPieces = item.getInitialStock();

                soldBoxes = 0;
                soldPieces = item.getSoldStock();

                availableBoxes = item.getInitialStock() - item.getSoldStock();
                availablePieces = item.getInitialStock() - item.getSoldStock();
            }

            ItemResponseDTO dto = new ItemResponseDTO(
                    item.getId(),
                    item.getCategory(),
                    item.getDesignName(),
                    item.getSize(),
                    item.getType(),
                    initialBoxes,
                    initialPieces,
                    availableBoxes,
                    availablePieces,
                    soldBoxes,
                    soldPieces
            );
            res.add(dto);
        }
         return res;
    }

    public List<ItemResponseDTO> searchBySize(String size) {
        List<Items> items = itemRepo.findBySize(size);
        List<ItemResponseDTO> res = new ArrayList<>();
        for(Items item : items) {
            int num = piecesPerBoxRepo.findBySizes(item.getSize()).getPieces();
            int initialBoxes;
            int initialPieces;
            int availableBoxes;
            int availablePieces;
            int soldBoxes;
            int soldPieces;

            if (item.getCategory() == Category.TILES) {

                initialBoxes = item.getInitialStock() / num;
                initialPieces = item.getInitialStock() % num;

                soldBoxes = item.getSoldStock() / num;
                soldPieces = item.getSoldStock() % num;

                availableBoxes = (item.getInitialStock() - item.getSoldStock()) / num;
                availablePieces = (item.getInitialStock() - item.getSoldStock()) % num;

            } else {
                initialBoxes = 0;
                initialPieces = item.getInitialStock();

                soldBoxes = 0;
                soldPieces = item.getSoldStock();

                availableBoxes = item.getInitialStock() - item.getSoldStock();
                availablePieces = item.getInitialStock() - item.getSoldStock();
            }

            ItemResponseDTO dto = new ItemResponseDTO(
                    item.getId(),
                    item.getCategory(),
                    item.getDesignName(),
                    item.getSize(),
                    item.getType(),
                    initialBoxes,
                    initialPieces,
                    availableBoxes,
                    availablePieces,
                    soldBoxes,
                    soldPieces
            );
            res.add(dto);
        }
        return res;
    }

    public List<ItemResponseDTO> searchByType(String type) {
        List<Items> items = itemRepo.findByType(type);
        List<ItemResponseDTO> res = new ArrayList<>();
        for(Items item : items) {
            int num = piecesPerBoxRepo.findBySizes(item.getSize()).getPieces();
            int initialBoxes;
            int initialPieces;
            int availableBoxes;
            int availablePieces;
            int soldBoxes;
            int soldPieces;

            if (item.getCategory() == Category.TILES) {

                initialBoxes = item.getInitialStock() / num;
                initialPieces = item.getInitialStock() % num;

                soldBoxes = item.getSoldStock() / num;
                soldPieces = item.getSoldStock() % num;

                availableBoxes = (item.getInitialStock() - item.getSoldStock()) / num;
                availablePieces = (item.getInitialStock() - item.getSoldStock()) % num;

            } else {
                initialBoxes = 0;
                initialPieces = item.getInitialStock();

                soldBoxes = 0;
                soldPieces = item.getSoldStock();

                availableBoxes = item.getInitialStock() - item.getSoldStock();
                availablePieces = item.getInitialStock() - item.getSoldStock();
            }

            ItemResponseDTO dto = new ItemResponseDTO(
                    item.getId(),
                    item.getCategory(),
                    item.getDesignName(),
                    item.getSize(),
                    item.getType(),
                    initialBoxes,
                    initialPieces,
                    availableBoxes,
                    availablePieces,
                    soldBoxes,
                    soldPieces
            );
            res.add(dto);
        }
        return res;
    }
}
