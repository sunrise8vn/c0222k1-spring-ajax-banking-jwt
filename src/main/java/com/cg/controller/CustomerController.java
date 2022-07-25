package com.cg.controller;


import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.dto.TransferDTO;
import com.cg.service.customer.CustomerService;
import com.cg.service.deposit.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DepositService depositService;

    @GetMapping
    public ModelAndView showListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/list");
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreatePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/create");

        modelAndView.addObject("customer", new Customer());

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditPage(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/update");

        Optional<Customer> customer = customerService.findById(id);

        if (customer.isPresent()) {
            modelAndView.addObject("customer", customer);
        }
        else {
            modelAndView.addObject("customer", new Customer());
        }

        return modelAndView;
    }

    @GetMapping("/deposit/{id}")
    public ModelAndView showDepositPage(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/deposit");

        Optional<Customer> customer = customerService.findById(id);

        if (customer.isPresent()) {
            modelAndView.addObject("customer", customer);
        }
        else {
            modelAndView.addObject("customer", new Customer());
        }

        modelAndView.addObject("deposit", new Deposit());

        return modelAndView;
    }

    @GetMapping("/transfer/{id}")
    public ModelAndView showTransferPage(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/transfer");

        Optional<Customer> sender = customerService.findById(id);

        if (sender.isPresent()) {

            TransferDTO transferDTO = new TransferDTO();
            transferDTO.setSenderId(sender.get().getId().toString());
//            transferDTO.setSenderName(sender.get().getFullName());
//            transferDTO.setEmail(sender.get().getEmail());
//            transferDTO.setBalance(sender.get().getBalance().toString());


            modelAndView.addObject("transferDTO", transferDTO);

            List<Customer> recipients = customerService.findAllByIdNot(sender.get().getId());

            modelAndView.addObject("recipients", recipients);
        }
        else {
            modelAndView.addObject("transferDTO", new TransferDTO());
            modelAndView.addObject("recipients", null);
        }

        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView doCreate(@ModelAttribute Customer customer) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/create");

        try {
            customer.setBalance(BigDecimal.ZERO);
            customerService.save(customer);

            modelAndView.addObject("success", "New customer add success");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("error", "Bad data");
        }

        modelAndView.addObject("customer", new Customer());

        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView doUpdate(@PathVariable Long id, @ModelAttribute Customer customer) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/update");

        try {
            customer.setId(id);
            customerService.save(customer);

            modelAndView.addObject("customer", customer);
            modelAndView.addObject("success", "Update customer success");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("error", "Bad data");
            modelAndView.addObject("customer", new Customer());
        }

        return modelAndView;
    }

    @PostMapping("/deposit/{id}")
    public ModelAndView doDeposit(@PathVariable Long id, @ModelAttribute Deposit deposit) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/deposit");

        Optional<Customer> optionalCustomer = customerService.findById(id);

        if (optionalCustomer.isPresent()) {
            try {
                customerService.deposit(optionalCustomer.get(), deposit);

                modelAndView.addObject("customer", optionalCustomer.get());
                modelAndView.addObject("success", "Deposit success");
            } catch (Exception e) {
                e.printStackTrace();
                modelAndView.addObject("error", "Bad data");
                modelAndView.addObject("customer", new Customer());
            }
        }
        else {
            modelAndView.addObject("error", "Invalid Id");
            modelAndView.addObject("customer", new Customer());
        }

        modelAndView.addObject("deposit", new Deposit());

        return modelAndView;
    }

    @PostMapping("/transfer/{senderId}")
    public ModelAndView doTransfer(@PathVariable Long senderId, @Validated @ModelAttribute TransferDTO transferDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/customer/transfer");

        if (bindingResult.hasFieldErrors()) {
            modelAndView.addObject("hasError", true);
            modelAndView.addObject("recipients", customerService.findAllByIdNot(senderId));
            return modelAndView;
        }

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (senderOptional.isPresent()) {

            Long recipientId = Long.parseLong(transferDTO.getRecipientId());

            Optional<Customer> recipientOptional = customerService.findById(recipientId);

            if (recipientOptional.isPresent()) {
                if (senderOptional.get().getId().equals(recipientOptional.get().getId())) {
                    modelAndView.addObject("error", "Invalid Sender and Recipient information");
                    modelAndView.addObject("sender", new Customer());
                }
                else {
                    BigDecimal currentBalance = senderOptional.get().getBalance();
                    BigDecimal transferAmount = new BigDecimal(transferDTO.getTransferAmount());
                    int fees = 10;
                    BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
                    BigDecimal transactionAmount = transferAmount.add(feesAmount);

                    if (currentBalance.compareTo(transactionAmount) >= 0) {

                        Transfer transfer = new Transfer();

                        transfer.setId(0l);
                        transfer.setSender(senderOptional.get());
                        transfer.setRecipient(recipientOptional.get());
                        transfer.setTransferAmount(transferAmount);
                        transfer.setFees(10);
                        transfer.setFeesAmount(feesAmount);
                        transfer.setTransactionAmount(transactionAmount);

                        try {
                            customerService.doTransfer(transfer);

                            TransferDTO newTransferDTO = new TransferDTO();
                            transferDTO.setSenderId(senderOptional.get().getId().toString());
//                            transferDTO.setSenderName(senderOptional.get().getFullName());
//                            transferDTO.setEmail(senderOptional.get().getEmail());
//                            transferDTO.setBalance(senderOptional.get().getBalance().toString());

                            Optional<Customer> sender = customerService.findById(senderId);

                            modelAndView.addObject("transferDTO", newTransferDTO);

                            List<Customer> recipients = customerService.findAllByIdNot(sender.get().getId());

                            modelAndView.addObject("recipients", recipients);
                        } catch (Exception e) {
                            modelAndView.addObject("error", "hệ thống bị lỗi");
                        }
                    }
                    else {
                        modelAndView.addObject("error", "Sender balance not enough to transfer transaction");
                        modelAndView.addObject("transferDTO", new TransferDTO());
                    }
                }
            }
            else {
                modelAndView.addObject("error", "Invalid Recipient information");
                modelAndView.addObject("transferDTO", new TransferDTO());
            }
        }
        else {
            modelAndView.addObject("error", "Invalid Sender information");
            modelAndView.addObject("transferDTO", new TransferDTO());
        }

        return modelAndView;
    }

}
