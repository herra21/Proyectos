package com.tfgdam.gestion_paqueteria.service.impl;

import com.tfgdam.gestion_paqueteria.repository.RolRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;


}
