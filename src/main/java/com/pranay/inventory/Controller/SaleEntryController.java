package com.pranay.inventory.Controller;

import com.pranay.inventory.DTO.SaleEntryRequestDTO;
import com.pranay.inventory.DTO.SaleEntryResponseDTO;
import com.pranay.inventory.Service.SaleEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SaleEntryController {

    @Autowired
    private SaleEntryService service;

    @PostMapping("/salesEntry/withoutBill")
    public ResponseEntity<SaleEntryResponseDTO> addSale(@RequestBody SaleEntryRequestDTO req) {
        SaleEntryResponseDTO dto = service.addSale(req);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/salesEntry/withoutBill/{id}")
    public ResponseEntity<SaleEntryResponseDTO> updateSale(@PathVariable int id,
                                                           @RequestBody SaleEntryRequestDTO req) {
        SaleEntryResponseDTO dto = service.updateSale(req, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/saleEntry/{id}")
    public ResponseEntity<String> deleteSaleEntry(@PathVariable int id) {
        service.deleteSaleEntry(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/saleEntry/WithoutBill/All")
    public ResponseEntity<List<SaleEntryResponseDTO>> getAllSaleEntriesWithoutBill() {
        List<SaleEntryResponseDTO> dto = service.getAllSaleEntries();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/saleEntry/WithoutBill/{id}")
    public ResponseEntity<List<SaleEntryResponseDTO>> getSaleEntriesByItemId(@PathVariable int id) {
        List<SaleEntryResponseDTO> dto = service.fetchSaleEntriesByItemId(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


}
