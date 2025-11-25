package com.example.demo.repository;

import com.example.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByCpf(String cpf);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByCpfAndIdNot(String cpf, Long id);
}
