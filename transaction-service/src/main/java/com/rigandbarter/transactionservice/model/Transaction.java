package com.rigandbarter.transactionservice.model;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_transaction")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uniqueId;
    private String title;
    private String buyerId;
    private String sellerId;
    private String listingId;
    private LocalDateTime creationDate;
    private LocalDateTime completionDate;
    private TransactionState state;
}
