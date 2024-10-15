package com.example.instasham.service;

import com.example.instasham.entity.Instasham;
import com.example.instasham.repository.InstashamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InstashamService {
    @Autowired
    private InstashamRepository instashamRepository;

    public Instasham addinstasham (Instasham instasham){
        return instashamRepository.save(instasham);
    }
    public List<Instasham> fetchInstasham(){
        return instashamRepository.findAll();
    }
    public Instasham fetchInstashamById(int id){
        return instashamRepository.findById(id).orElse(null);
    }
}
