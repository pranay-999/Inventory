package com.pranay.inventory.Controller;

import com.pranay.inventory.DTO.ItemRequestDTO;
import com.pranay.inventory.DTO.ItemResponseDTO;
import com.pranay.inventory.Service.ItemService;
import com.pranay.inventory.model.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class itemController {

    @Autowired
    private ItemService service;

    @PostMapping("/ItemEntry")
    public ResponseEntity<ItemResponseDTO> addItem(@RequestBody ItemRequestDTO req){
        ItemResponseDTO dto = service.addItem(req);
        return  new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PutMapping("/ItemEntry/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable int id, @RequestBody ItemRequestDTO req){
        ItemResponseDTO dto = service.updateItem(req,id);
        return  new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @DeleteMapping("/ItemEntry/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id){
        service.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/ItemEntry/search/{DesignName}/{category}")
    public ResponseEntity<ItemResponseDTO> search(@PathVariable  String DesignName, @PathVariable String category){
        Category cat = Category.valueOf(category.toUpperCase());
        ItemResponseDTO dto = service.searchByName(DesignName,cat);
        return  new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/ItemEntry/search/category/{category}")
    public ResponseEntity<List<ItemResponseDTO>> fetchByCategory(@PathVariable String category){
        Category cat = Category.valueOf(category.toUpperCase());
        List<ItemResponseDTO> dto = service.searchByCategory(cat);
        return  new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/ItemEntry/search/size/{size}")
    public ResponseEntity<List<ItemResponseDTO>> fetchBySize(@PathVariable String size){

        List<ItemResponseDTO> dto = service.searchBySize(size);
        return  new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/ItemEntry/search/type/{type}")
    public ResponseEntity<List<ItemResponseDTO>> fetchByType(@PathVariable String type){
        List<ItemResponseDTO> dto = service.searchByType(type);
        return  new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
