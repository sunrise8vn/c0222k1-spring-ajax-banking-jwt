package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.validation.constraints.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class DepositDTO implements Validator {

    private String id;

    @NotNull(message = "Customer Id is required")
    @Pattern(regexp = "^[0-9]+$", message = "Sender ID only digit")
    private String customerId;

//    @NotBlank(message = "Transaction Amount is required")
//    @Pattern(regexp = "^[0-9]+$", message = "Transaction Amount only digit")
//    @Length(min = 1, max = 7, message = "length of transaction amount in between 50 to 100.000")
    private String transactionAmount;

    public Deposit toDeposit(Customer customer) {
        return new Deposit()
                .setId(0L)
                .setTransactionAmount(new BigDecimal(Long.parseLong(transactionAmount)))
                .setCustomer(customer);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return DepositDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepositDTO depositDTO = (DepositDTO) target;

        String transactionAmountStr = depositDTO.getTransactionAmount();

        if (transactionAmountStr == null) {
            errors.rejectValue("transactionAmount", "transactionAmount.null", "The transaction amount not null");
            return;
        }

        if (transactionAmountStr.isEmpty()) {
            errors.rejectValue("transactionAmount", "transactionAmount.isEmpty", "The transaction amount not empty");
            return;
        }

        if (!transactionAmountStr.matches("(^$|[0-9]*$)")){
            errors.rejectValue("transactionAmount", "transactionAmount.matches", "The transaction amount only digit");
            return;
        }

        BigDecimal transactionAmount = new BigDecimal(Long.parseLong(transactionAmountStr));
        BigDecimal min = new BigDecimal(50L);
        BigDecimal max = new BigDecimal(1000000L);

        if (transactionAmount.compareTo(min) < 0) {
            errors.rejectValue("transactionAmount", "transactionAmount.min", "The transaction amount min 50");
            return;
        }

        if (transactionAmount.compareTo(max) > 0) {
            errors.rejectValue("transactionAmount", "transactionAmount.max", "The transaction amount max 1.000.000");
        }

//        if (transactionAmountStr == null) {
//            errors.rejectValue("transactionAmount", "transactionAmount.null", "The transaction amount not null");
//        }
//        else {
//            if (transactionAmountStr.isEmpty()) {
//                errors.rejectValue("transactionAmount", "transactionAmount.isEmpty", "The transaction amount not empty");
//            }
//            else {
//                if (!transactionAmountStr.matches("(^$|[0-9]*$)")){
//                    errors.rejectValue("transactionAmount", "transactionAmount.matches", "The transaction amount only digit");
//                }
//                else {
//                    BigDecimal transactionAmount = new BigDecimal(Long.parseLong(transactionAmountStr));
//                    BigDecimal min = new BigDecimal(50L);
//                    BigDecimal max = new BigDecimal(1000000L);
//
//                    if (transactionAmount.compareTo(min) >= 0) {
//                        if (transactionAmount.compareTo(max) > 0) {
//                            errors.rejectValue("transactionAmount", "transactionAmount.max", "The transaction amount max 1.000.000");
//                        }
//                    }
//                    else {
//                        errors.rejectValue("transactionAmount", "transactionAmount.min", "The transaction amount min 50");
//                    }
//                }
//            }
//        }
    }
}
