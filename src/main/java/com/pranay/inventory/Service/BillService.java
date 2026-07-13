package com.pranay.inventory.Service;

import com.pranay.inventory.DTO.BillRequestDTO;
import com.pranay.inventory.DTO.BillResponseDTO;
import com.pranay.inventory.DTO.SaleEntryRequestDTO;
import com.pranay.inventory.DTO.SaleEntryResponseDTO;
import com.pranay.inventory.Repository.BillRepo;
import com.pranay.inventory.Repository.ItemRepo;
import com.pranay.inventory.Repository.PiecesPerBoxRepo;
import com.pranay.inventory.Repository.SaleEntryRepo;
import com.pranay.inventory.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {
    @Autowired
    private SaleEntryRepo saleEntryRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private PiecesPerBoxRepo piecesPerBoxRepo;
    @Autowired
    private BillRepo billRepo;
    @Autowired
    private SaleEntryService saleEntryService;

    public BillResponseDTO addBill(BillRequestDTO req) {

        Bill entry = new Bill();
        entry.setCustomerName(req.customerName());
        entry.setPhoneNumber(req.phone());

        List<SaleEntry> saleEntries = new ArrayList<>();
        double grandTotal = 0;

        for (SaleEntryRequestDTO saleRequest : req.salesEntries()) {
            Items items = itemRepo.findById(saleRequest.itemId())
                    .orElseThrow(() -> new RuntimeException("Item Not Found"));

            int num = piecesPerBoxRepo.findBySizes(items.getSize()).getPieces();

            SaleEntry sale = new SaleEntry();
            sale.setBill(entry);
            sale.setItems(items);
            if (items.getCategory() == Category.TILES) {
                sale.setQuantity((saleRequest.boxes() * num) + saleRequest.pieces());
            } else {
                sale.setQuantity(saleRequest.pieces());
            }
            sale.setUnitPrice(saleRequest.unitPrice());
            double totalAmount;
            if (items.getCategory() == Category.TILES) {
                double boxPrice = (saleRequest.boxes() * saleRequest.unitPrice());
                double piecePrice = (saleRequest.unitPrice() / num) * saleRequest.pieces();
                totalAmount = boxPrice + piecePrice;
            } else {
                totalAmount = saleRequest.pieces() * saleRequest.unitPrice();
            }
            sale.setTotalPrice(totalAmount);
            int sold = items.getSoldStock() + ((saleRequest.boxes() * num) + saleRequest.pieces());
            items.setSoldStock(sold);
            itemRepo.save(items);
            grandTotal = grandTotal + totalAmount;
            saleEntries.add(sale);

        }

        entry.setSaleEntries(saleEntries);
        entry.setTotalAmount(grandTotal);
        Bill bill = billRepo.save(entry);
        String billId = "B-00 " + bill.getId();
        List<SaleEntryResponseDTO> response = new ArrayList<>();


        for (SaleEntry sr : bill.getSaleEntries()) {
            int num = piecesPerBoxRepo.findBySizes(sr.getItems().getSize()).getPieces();
            int boxes = sr.getQuantity() / num;
            int pieces = sr.getQuantity() % num;
            SaleEntryResponseDTO ser = new SaleEntryResponseDTO(
                    sr.getId(),
                    sr.getItems().getCategory(),
                    sr.getItems().getDesignName(),
                    sr.getItems().getSize(),
                    sr.getItems().getType(),
                    sr.getItems().getId(),
                    boxes,
                    pieces,
                    sr.getUnitPrice(),
                    sr.getTotalPrice()
            );
            response.add(ser);
        }

        BillResponseDTO dto = new BillResponseDTO(
                billId,
                bill.getCustomerName(),
                bill.getPhoneNumber(),
                response,
                bill.getTotalAmount(),
                LocalDateTime.now()

        );
        return dto;
    }

    public BillResponseDTO findBill(int id) {
        Bill bill = billRepo.findById(id).orElseThrow();

        List<SaleEntry> saleEntries = bill.getSaleEntries();
        List<SaleEntryResponseDTO> saleResponse = new ArrayList<>();
        double grandTotal = 0;

        for (SaleEntry sale : saleEntries) {
            int num = piecesPerBoxRepo.findBySizes(sale.getItems().getSize()).getPieces();
            int boxes = sale.getQuantity() / num;
            int pieces = sale.getQuantity() % num;
            SaleEntryResponseDTO dto = new SaleEntryResponseDTO(
                    sale.getId(),
                    sale.getItems().getCategory(),
                    sale.getItems().getDesignName(),
                    sale.getItems().getSize(),
                    sale.getItems().getType(),
                    sale.getItems().getId(),
                    boxes,
                    pieces,
                    sale.getUnitPrice(),
                    sale.getTotalPrice()
            );
            grandTotal = grandTotal+sale.getTotalPrice();
            saleResponse.add(dto);
        }

        String billId = "B-00 " + bill.getId();
        BillResponseDTO dto = new BillResponseDTO(
                billId,
                bill.getCustomerName(),
                bill.getPhoneNumber(),
                saleResponse,
                bill.getTotalAmount(),
                bill.getCreatedAt()

        );

        return dto;
    }


    public BillResponseDTO updateBill(BillRequestDTO req, int id) {

        Bill entry = billRepo.findById(id).orElseThrow(() -> new RuntimeException("Bill Not Found"));


        entry.setCustomerName(req.customerName());
        entry.setPhoneNumber(req.phone());

        List<SaleEntry> saleEntries = new ArrayList<>();

        for (SaleEntryRequestDTO saleRequest : req.salesEntries()) {
            for (SaleEntry sale : entry.getSaleEntries()) {
                SaleEntry s1 = saleEntryRepo.findById(sale.getId())
                        .orElseThrow(() -> new RuntimeException("Sale Entry Not Found"));

                Items items = itemRepo.findById(s1.getItems().getId())
                        .orElseThrow(() -> new RuntimeException("Item Not Found"));

                int soldStock = items.getSoldStock() - sale.getQuantity();
                items.setSoldStock(soldStock);
                itemRepo.save(items);

                int num = piecesPerBoxRepo.findBySizes(items.getSize()).getPieces();

                s1.setBill(entry);
                s1.setItems(items);
                if (items.getCategory() == Category.TILES) {
                    s1.setQuantity((saleRequest.boxes() * num) + saleRequest.pieces());
                } else {
                    s1.setQuantity(saleRequest.pieces());
                }
                s1.setUnitPrice(saleRequest.unitPrice());
                double totalAmount;
                if (items.getCategory() == Category.TILES) {
                    double boxPrice = (saleRequest.boxes() * saleRequest.unitPrice());
                    double piecePrice = (saleRequest.unitPrice() / num) * saleRequest.pieces();
                    totalAmount = boxPrice + piecePrice;
                } else {
                    totalAmount = saleRequest.pieces() * saleRequest.unitPrice();
                }
                s1.setTotalPrice(totalAmount);
                int sold = items.getSoldStock() + ((saleRequest.boxes() * num) + saleRequest.pieces());
                items.setSoldStock(sold);
                itemRepo.save(items);
                saleEntries.add(s1);
            }
        }

        entry.setSaleEntries(saleEntries);
        Bill bill = billRepo.save(entry);
        String billId = "B-00 " + bill.getId();
        List<SaleEntryResponseDTO> response = new ArrayList<>();
        double grandTotal =0;

        for (SaleEntry sr : bill.getSaleEntries()) {
            int num = piecesPerBoxRepo.findBySizes(sr.getItems().getSize()).getPieces();
            int boxes = sr.getQuantity() / num;
            int pieces = sr.getQuantity() % num;
            SaleEntryResponseDTO ser = new SaleEntryResponseDTO(
                    sr.getId(),
                    sr.getItems().getCategory(),
                    sr.getItems().getDesignName(),
                    sr.getItems().getSize(),
                    sr.getItems().getType(),
                    sr.getItems().getId(),
                    boxes,
                    pieces,
                    sr.getUnitPrice(),
                    sr.getTotalPrice()
            );
            grandTotal = grandTotal+sr.getTotalPrice();
            response.add(ser);
        }

        BillResponseDTO dto = new BillResponseDTO(
                billId,
                bill.getCustomerName(),
                bill.getPhoneNumber(),
                response,
                bill.getTotalAmount(),
                LocalDateTime.now()
        );
        return dto;
    }

    public List<BillResponseDTO> getAllBills() {
        List<Bill> bill = billRepo.findAll();
        List<BillResponseDTO> dto = new ArrayList<>();
        double grandTotal = 0;
        for (Bill b : bill) {
            List<SaleEntryResponseDTO> saleResponse = new ArrayList<>();

            for (SaleEntry res : b.getSaleEntries()) {
                int num = piecesPerBoxRepo.findBySizes(res.getItems().getSize()).getPieces();
                int boxes = res.getQuantity() / num;
                int pieces = res.getQuantity() % num;
                double totalPrice = (boxes * res.getUnitPrice()) + (pieces * (res.getUnitPrice() / num));
                SaleEntryResponseDTO se = new SaleEntryResponseDTO(
                        res.getId(),
                        res.getItems().getCategory(),
                        res.getItems().getDesignName(),
                        res.getItems().getSize(),
                        res.getItems().getType(),
                        res.getItems().getId(),
                        boxes,
                        pieces,
                        res.getUnitPrice(),
                        totalPrice
                );
                grandTotal = grandTotal + totalPrice;
                saleResponse.add(se);
            }
            String billId = "B-" + String.format("%03d,", b.getId());
            BillResponseDTO bt = new BillResponseDTO(
                    billId,
                    b.getCustomerName(),
                    b.getPhoneNumber(),
                    saleResponse,
                    b.getTotalAmount(),
                    b.getCreatedAt()
            );
            dto.add(bt);
        }
        return dto;
    }

    public void deleteBill(int id) {

        Bill bill = billRepo.findById(id).orElseThrow(() -> new RuntimeException("Bill not Found"));
        for (SaleEntry s : bill.getSaleEntries()) {
            saleEntryService.deleteSaleEntry(s.getId());
        }
        billRepo.save(bill);

        billRepo.deleteById(id);
        System.out.println("1 is getting called");


    }
    public List<BillResponseDTO> fetchBillByItemId(int id){
       List<Bill> bill =  billRepo.findBillByItemId(id);
       List<BillResponseDTO> res = new ArrayList<>();
       double grandTotal = 0;

       for(Bill oneBill: bill){
           List<SaleEntryResponseDTO> se = new ArrayList<>();
           for(SaleEntry saleEntry : oneBill.getSaleEntries()){
               if(id == saleEntry.getItems().getId()) {
                   int num = piecesPerBoxRepo.findBySizes(saleEntry.getItems().getSize()).getPieces();
                   SaleEntryResponseDTO saleRes = new SaleEntryResponseDTO(
                           saleEntry.getId(),
                           saleEntry.getItems().getCategory(),
                           saleEntry.getItems().getDesignName(),
                           saleEntry.getItems().getSize(),
                           saleEntry.getItems().getType(),
                           saleEntry.getItems().getId(),
                           saleEntry.getQuantity() / num,
                           saleEntry.getQuantity() % num,
                           saleEntry.getUnitPrice(),
                           saleEntry.getTotalPrice()
                   );
                   grandTotal = grandTotal + saleEntry.getTotalPrice();
                   se.add(saleRes);
               }
           }
           String billId = "B- " + String.format("%03d", oneBill.getId());
           BillResponseDTO dto = new BillResponseDTO(
                   billId,
                   oneBill.getCustomerName(),
                   oneBill.getPhoneNumber(),
                   se,
                   oneBill.getTotalAmount(),
                   oneBill.getCreatedAt()
           );
           res.add(dto);
       }
        System.out.println(res);
        return res;
    }


}
