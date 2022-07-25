package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.function.BiFunction;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferDTO {

    private Long id;

    @NotBlank(message = "Sender is required")
    @Pattern(regexp = "^[0-9]$", message = "Sender ID only digit")
    private String senderId;

//    private String email;
//    private String senderName;

    @NotBlank(message = "Recipient is required")
    @Pattern(regexp = "^[0-9]$", message = "Recipient ID only digit")
    private String recipientId;

//    private String balance;

    @NotBlank(message = "Transfer Amount is required")
    @Size(min = 1, max = 7, message = "length of transfer amount in between 50 to 100.000")
    private String transferAmount;

    private Long fees;

    private BigDecimal feesAmount;

    private BigDecimal transactionAmount;

    public Transfer toTransfer(Customer sender, Customer recipient) {
        return new Transfer()
                .setSender(sender)
                .setRecipient(recipient)
                .setTransferAmount(new BigDecimal(Long.parseLong(transferAmount)))
                .setFees(fees)
                .setFeesAmount(feesAmount)
                .setTransactionAmount(transactionAmount);
    }

}
