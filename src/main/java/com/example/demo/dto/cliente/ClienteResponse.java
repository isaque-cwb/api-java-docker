package com.example.demo.dto.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {

    private Long id;
    private String nome;
    private String cpf;
    private EnderecoDTO endereco;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
