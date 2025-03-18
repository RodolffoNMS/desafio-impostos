package com.zup.desafio_imposto.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
@Entity
public class TaxType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
    private String name;
    @Column(nullable = false)
    private String description;
    @NotNull(message = "A alíquota não pode ser nula")
    @DecimalMin(value = "0.0", inclusive = false, message = "A alíquota deve ser maior que 0")
    private Double rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "O nome não pode ser nulo") @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "O nome não pode ser nulo") @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres") String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull(message = "A alíquota não pode ser nula") @DecimalMin(value = "0.0", inclusive = false, message = "A alíquota deve ser maior que 0") Double getRate() {
        return rate;
    }

    public void setRate(@NotNull(message = "A alíquota não pode ser nula") @DecimalMin(value = "0.0", inclusive = false, message = "A alíquota deve ser maior que 0") Double rate) {
        this.rate = rate;
    }

    public TaxType(Long id, String name, String description, Double rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rate = rate;
    }

    public TaxType() {
    }
}