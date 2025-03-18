package com.zup.desafio_imposto.controllers;

import com.zup.desafio_imposto.dtos.request.CalculateTaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.request.TaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.response.CalculateTaxTypeResponseDTO;
import com.zup.desafio_imposto.dtos.response.TaxTypeResponseDTO;
import com.zup.desafio_imposto.services.TaxTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tax")
@Tag(name = "Impostos", description = "Gerencia e impostos e cálculos")
public class TaxTypeController {

    private final TaxTypeService taxTypeService;

    public TaxTypeController(TaxTypeService taxTypeService) {
        this.taxTypeService = taxTypeService;
    }

    @Operation(summary = "Listar todos os tipos de impostos", description = "Retorna uma lista com todos os tipos de impostos cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de impostos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/tipos")
    public ResponseEntity<List<TaxTypeResponseDTO>> getAllTaxTypes() {
        List<TaxTypeResponseDTO> taxTypes = taxTypeService.findAll();
        return ResponseEntity.ok(taxTypes);
    }

    @Operation(summary = "Buscar tipo de imposto por ID", description = "Retorna os detalhes de um tipo de imposto específico pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de imposto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de imposto não encontrado", content = @Content)
    })
    @GetMapping("/tipos/{id}")
    public ResponseEntity<TaxTypeResponseDTO> getTaxTypeById(@PathVariable Long id) {
        TaxTypeResponseDTO taxTypeResponse = taxTypeService.findById(id);
        return ResponseEntity.ok(taxTypeResponse);
    }

    @Operation(summary = "Adicionar um novo tipo de imposto", description = "Cadastra um novo tipo de imposto no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de imposto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PostMapping("/tipos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxTypeResponseDTO> addTaxType(@Valid @RequestBody TaxTypeRequestDTO taxTypeRequest) {
        TaxTypeResponseDTO taxTypeResponse = taxTypeService.addTax(taxTypeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(taxTypeResponse);
    }

    @Operation(summary = "Calcular tipo de imposto", description = "Realiza o cálculo de imposto com base nos dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cálculo realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PostMapping("/calculo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CalculateTaxTypeResponseDTO> calculateTaxType(@Valid @RequestBody CalculateTaxTypeRequestDTO calculateTaxTypeRequest) {
        CalculateTaxTypeResponseDTO calculateTaxTypeResponse = taxTypeService.calculateTaxType(calculateTaxTypeRequest);
        return ResponseEntity.ok(calculateTaxTypeResponse);
    }

    @Operation(summary = "Excluir um tipo de imposto", description = "Remove um tipo de imposto do sistema pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de imposto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de imposto não encontrado", content = @Content)
    })
    @DeleteMapping("/tipos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTaxType(@PathVariable Long id) {
        taxTypeService.deleteTaxById(id);
        return ResponseEntity.noContent().build();
    }
}