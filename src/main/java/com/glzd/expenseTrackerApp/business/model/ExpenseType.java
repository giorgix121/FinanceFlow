package com.glzd.expenseTrackerApp.business.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseType {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "ExpenseCategory is not null")
    @Column(unique = true)
    private String expenseCategory;


}
