package com.example.demo.controller;

import com.example.demo.dto.cliente.ClienteRequest;
import com.example.demo.dto.cliente.ClienteResponse;
import com.example.demo.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Clientes", description = "CRUD de clientes (requer autenticação)")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Criar novo cliente")
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.criarCliente(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes")
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<ClienteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request
    ) {
        return ResponseEntity.ok(clienteService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
