package com.pranay.inventory.Service;

import com.pranay.inventory.DTO.SaleEntryRequestDTO;
import com.pranay.inventory.DTO.SaleEntryResponseDTO;
import com.pranay.inventory.Repository.ItemRepo;
import com.pranay.inventory.Repository.PiecesPerBoxRepo;
import com.pranay.inventory.Repository.SaleEntryRepo;
import com.pranay.inventory.model.Category;
import com.pranay.inventory.model.Items;
import com.pranay.inventory.model.SaleEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleEntryService {
    @Autowired
    private SaleEntryRepo saleEntryRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private PiecesPerBoxRepo piecesPerBoxRepo;

    public SaleEntryResponseDTO addSale(SaleEntryRequestDTO req) {
        double totalAmount;
        Items items = itemRepo.findById(req.itemId()).orElseThrow();
        int num = piecesPerBoxRepo.findBySizes(items.getSize()).getPieces();
        SaleEntry entry = new SaleEntry();

        entry.setItems(items);
        entry.setBill(null);

        if (items.getCategory() == Category.TILES) {
            entry.setQuantity((req.boxes() * num) + req.pieces());
            entry.setUnitPrice(req.unitPrice());
        } else {
            entry.setQuantity(req.pieces());
            entry.setUnitPrice(req.unitPrice());
        }

        if (items.getCategory() == Category.TILES) {
            double boxPrice = (req.boxes() * req.unitPrice());
            double piecePrice = (req.unitPrice() / num) * req.pieces();
            totalAmount = boxPrice + piecePrice;
        } else {
            totalAmount = req.pieces() * req.unitPrice();
        }
        entry.setTotalPrice(totalAmount);

        int sold = items.getSoldStock() + (req.boxes() * num) + req.pieces();

        items.setSoldStock(sold);
        itemRepo.save(items);

        SaleEntry saved = saleEntryRepo.save(entry);

        int boxes = saved.getQuantity() / num;
        int pieces = saved.getQuantity() % num;

        SaleEntryResponseDTO dto = new SaleEntryResponseDTO(
                saved.getId(),
                items.getCategory(),
                items.getDesignName(),
                items.getSize(),
                items.getType(),
                items.getId(),
                boxes,
                pieces,
                saved.getUnitPrice(),
                saved.getTotalPrice());
        return dto;
    }


    public SaleEntryResponseDTO updateSale(SaleEntryRequestDTO req, int id) {
        SaleEntry saleEntry = saleEntryRepo.findById(id).orElseThrow();

        Items items = itemRepo.findById(saleEntry.getItems().getId()).orElseThrow();
        int soldStock = items.getSoldStock() - saleEntry.getQuantity();
        int num = piecesPerBoxRepo.findBySizes(items.getSize()).getPieces();
        items.setSoldStock(soldStock);
        Items i = itemRepo.save(items);

        saleEntry.setItems(i);
        int totalBoxes = (req.boxes() * num) + req.pieces();
        saleEntry.setQuantity(totalBoxes);
        saleEntry.setUnitPrice(req.unitPrice());
        int iSold = i.getSoldStock() + totalBoxes;
        i.setSoldStock(iSold);
        itemRepo.save(i);

        double price = req.unitPrice() / num;
        double finalAmount = price * totalBoxes;
        saleEntry.setTotalPrice(finalAmount);
        SaleEntry saved = saleEntryRepo.save(saleEntry);

        int boxes = saved.getQuantity() / num;
        int pieces = saved.getQuantity() % num;

        double piecePrice = saved.getUnitPrice() / num;
        double totalPrice = piecePrice * saved.getQuantity();

        SaleEntryResponseDTO dto = new SaleEntryResponseDTO(
                saved.getId(),
                saved.getItems().getCategory(),
                saved.getItems().getDesignName(),
                saved.getItems().getSize(),
                saved.getItems().getType(),
                saved.getItems().getId(),
                boxes,
                pieces,
                saleEntry.getUnitPrice(),
                totalPrice
        );
        return dto;
    }

    public List<SaleEntryResponseDTO> getAllSaleEntries() {
        List<SaleEntry> saleEntries = saleEntryRepo.findByBill_IdIsNull();
        List<SaleEntryResponseDTO> res = new ArrayList<>();

        for (SaleEntry entry : saleEntries) {
            int num = piecesPerBoxRepo.findBySizes(entry.getItems().getSize()).getPieces();
            int boxes = entry.getQuantity() / num;
            int pieces = entry.getQuantity() % num;
            double amount = (boxes * entry.getUnitPrice()) + (pieces * entry.getUnitPrice() / num);
            SaleEntryResponseDTO dto = new SaleEntryResponseDTO(
                    entry.getId(),
                    entry.getItems().getCategory(),
                    entry.getItems().getDesignName(),
                    entry.getItems().getSize(),
                    entry.getItems().getType(),
                    entry.getItems().getId(),
                    boxes,
                    pieces,
                    entry.getUnitPrice(),
                    amount
            );
            res.add(dto);
        }
        return res;
    }

    public void deleteSaleEntry(int id) {
        SaleEntry saleEntry = saleEntryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale Entry is not Found"));

        Items i = itemRepo.findById(saleEntry.getItems().getId())
                .orElseThrow(() -> new RuntimeException("Item is not Found"));
        int l = i.getSoldStock() - saleEntry.getQuantity();
        i.setSoldStock(l);
        itemRepo.save(i);

        saleEntryRepo.deleteById(id);
    }
    public List<SaleEntryResponseDTO>  fetchSaleEntriesByItemId(int id){
        List<SaleEntry> saleEntries = saleEntryRepo.findByItems_IdAndBill_IdIsNull(id);
        List<SaleEntryResponseDTO> saleRes = new ArrayList<>();

        for(SaleEntry se : saleEntries){
            int num = piecesPerBoxRepo.findBySizes(se.getItems().getSize()).getPieces();
            SaleEntryResponseDTO dto = new SaleEntryResponseDTO(
                    se.getId(),
                    se.getItems().getCategory(),
                    se.getItems().getDesignName(),
                    se.getItems().getSize(),
                    se.getItems().getType(),
                    se.getItems().getId(),
                    se.getQuantity()/num,
                    se.getQuantity()%num,
                    se.getUnitPrice(),
                    se.getTotalPrice()
            );
            saleRes.add(dto);
        }
        return saleRes;
    }
}
