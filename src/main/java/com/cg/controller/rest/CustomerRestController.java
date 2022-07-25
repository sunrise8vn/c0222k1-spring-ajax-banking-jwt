package com.cg.controller.rest;


import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.DepositDTO;
import com.cg.model.dto.LocationRegionDTO;
import com.cg.model.dto.TransferDTO;
import com.cg.service.customer.CustomerService;
import com.cg.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {

        List<CustomerDTO> customerDTOS = customerService.findAllCustomerDTO();

//        List<CustomerDTO> customerDTOS = new ArrayList<>();
//
//        for (Customer customer : customers) {
//            customerDTOS.add(customer.toCustomerDTO());
//        }

        return new ResponseEntity<>(customerDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable long id) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            throw new ResourceNotFoundException("Invalid customer ID");
        }

        return new ResponseEntity<>(customerOptional.get().toCustomerDTO(), HttpStatus.OK);
    }

    @GetMapping("/recipients-without-sender/{id}")
    public ResponseEntity<?> getRecipientsWithOutSenderById(@PathVariable long id) {

        List<CustomerDTO> recipients = customerService.getRecipientsWithOutSenderById(id);

        return new ResponseEntity<>(recipients, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@Validated @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        customerDTO.setId(0L);
        customerDTO.setBalance(new BigDecimal(0L));
        customerDTO.getLocationRegion().setId(0L);

        Boolean exitsEmail = customerService.existsByEmail(customerDTO.getEmail());

        if (exitsEmail) {
            throw new EmailExistsException("Email already exists");
        }

        Customer newCustomer = customerService.save(customerDTO.toCustomer());

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> doUpdate(@Validated @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        Boolean existId = customerService.existsById(customerDTO.getId());

        if (!existId) {
            throw new ResourceNotFoundException("Customer ID invalid");
        }

        Boolean existEmail = customerService.existsByEmailAndIdIsNot(customerDTO.getEmail(), customerDTO.getId());

        if (existEmail) {
            throw new DataInputException("Email is exist");
        }

        customerDTO.getLocationRegion().setId(0L);

        Customer updatedCustomer = customerService.save(customerDTO.toCustomer());

        return new ResponseEntity<>(updatedCustomer.toCustomerDTO(), HttpStatus.ACCEPTED);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> doDeposit(@Validated @RequestBody DepositDTO depositDTO, BindingResult bindingResult) {

        new DepositDTO().validate(depositDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        Optional<Customer> customerOptional = customerService.findById(Long.parseLong(depositDTO.getCustomerId()));

        if (!customerOptional.isPresent()) {
            throw new ResourceNotFoundException("Invalid customer ID");
        }

        Customer customer = customerOptional.get();
        Deposit deposit = depositDTO.toDeposit(customer);

        try {
            customerService.deposit(customer, deposit);
            return new ResponseEntity<>("Deposit success", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataInputException("Please contact Administrator");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> doTransfer(@Validated @RequestBody TransferDTO transferDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        if (transferDTO.getSenderId().equals(transferDTO.getRecipientId())) {
            throw new DataInputException("Invalid sender and receiver information");
        }

        Optional<Customer> senderOptional = customerService.findById(Long.parseLong(transferDTO.getSenderId()));

        if (!senderOptional.isPresent()) {
            throw new ResourceNotFoundException("Sender invalid");
        }

        Optional<Customer> recipientOptional = customerService.findById(Long.parseLong(transferDTO.getRecipientId()));

        if (!recipientOptional.isPresent()) {
            throw new ResourceNotFoundException("Recipient invalid");
        }

        BigDecimal currentBalance = senderOptional.get().getBalance();
        BigDecimal transferAmount = new BigDecimal(Long.parseLong(transferDTO.getTransferAmount()));
        Long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(new BigDecimal(fees)).divide(new BigDecimal(100L));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        transferDTO.setFees(fees);
        transferDTO.setFeesAmount(feesAmount);
        transferDTO.setTransactionAmount(transactionAmount);

        if (transactionAmount.compareTo(currentBalance) > 0) {
            throw new DataInputException("The sender's balance is not enough to make the transfer");
        }
        else {
            try {
                Map<String, CustomerDTO> result = customerService.doTransfer(transferDTO.toTransfer(senderOptional.get(), recipientOptional.get()));

                return new ResponseEntity<>(result, HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataInputException("Please contact Administrator");
            }
        }
    }
}
