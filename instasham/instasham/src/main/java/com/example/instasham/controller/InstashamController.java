package com.example.instasham.controller;

import com.example.instasham.entity.Instasham;
import com.example.instasham.service.InstashamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping(value = "/instagram")
@RestController
public class InstashamController {
    @Autowired
    private InstashamService instashamService;

    @PostMapping
    public Instasham addInstasham (@RequestBody Instasham instasham) {
        return instashamService.addinstasham(instasham);
    }
    @GetMapping
    public List<Instasham> fetchInstasham(){
        return instashamService.fetchInstasham();
    }
    @GetMapping("/{id}")
    public Instasham fetchInstagramByID(@PathVariable int id){
        return instashamService.fetchInstashamById(id);
    }
}
