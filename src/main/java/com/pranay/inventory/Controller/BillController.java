package com.pranay.inventory.Controller;

import com.pranay.inventory.DTO.BillRequestDTO;
import com.pranay.inventory.DTO.BillResponseDTO;
import com.pranay.inventory.Service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillController {

    @Autowired
    private BillService service;

    @PostMapping("/bill")
    public ResponseEntity<BillResponseDTO> addBill(@RequestBody BillRequestDTO req) {
        BillResponseDTO dto = service.addBill(req);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @PutMapping("/bill/{id}")
    public ResponseEntity<BillResponseDTO> updateBill(@PathVariable int id, @RequestBody BillRequestDTO req) {
        BillResponseDTO dto = service.updateBill(req, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/bill/All")
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {
        List<BillResponseDTO> dto = service.getAllBills();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/bill/{id}")
    public ResponseEntity<BillResponseDTO> findBill(@PathVariable int id) {
        BillResponseDTO dto = service.findBill(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/bill/{id}")
    public ResponseEntity<Void> deleteBillById(@PathVariable int id) {
        service.deleteBill(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("api/bill/{id}")
    public ResponseEntity<List<BillResponseDTO>> fetchBillByItemId(@PathVariable int id){
        List<BillResponseDTO> dto = service.fetchBillByItemId(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


}
