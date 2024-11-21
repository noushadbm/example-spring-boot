package com.koyeb.rayshan.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("user_expense_data")
public class ExpenseEntity {
    @Id
    private Long id;
    @Column("metadata_id")
    private Long metaDataId;
    @Column("title")
    private String title;
    @Column("amount")
    private String amount;
}
