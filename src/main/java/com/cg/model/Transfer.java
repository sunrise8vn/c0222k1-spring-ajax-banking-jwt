package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transfers")
@Accessors(chain = true)
public class Transfer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "transfer_amount", nullable = false)
    private BigDecimal transferAmount;

    @Column(name = "fees", nullable = false)
    private long fees;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "fees_amount", nullable = false)
    private BigDecimal feesAmount;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal transactionAmount;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Customer sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Customer recipient;

}
