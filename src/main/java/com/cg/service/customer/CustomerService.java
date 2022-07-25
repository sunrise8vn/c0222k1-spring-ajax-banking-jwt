package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerDTO;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CustomerService extends IGeneralService<Customer> {

    List<CustomerDTO> findAllCustomerDTO();

    List<CustomerDTO> getRecipientsWithOutSenderById(long id);

    List<Customer> findAllByIdNot(Long id);

    Boolean existsById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndIdIsNot(String email, Long id);

    void deposit(Customer customer, Deposit deposit);

    Map<String, CustomerDTO> doTransfer(Transfer transfer);
}
