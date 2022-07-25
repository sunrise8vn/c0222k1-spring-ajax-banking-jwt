package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.LocationRegion;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerDTO;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import com.cg.repository.LocationRegionRepository;
import com.cg.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private LocationRegionRepository locationRegionRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<CustomerDTO> findAllCustomerDTO() {
        return customerRepository.findAllCustomerDTO();
    }

    @Override
    public List<CustomerDTO> getRecipientsWithOutSenderById(long id) {
        return customerRepository.getRecipientsWithOutSenderById(id);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findAllByIdNot(Long id) {
        return customerRepository.findAllByIdNot(id);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.getById(id);
    }

    @Override
    public Boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByEmailAndIdIsNot(String email, Long id) {
        return customerRepository.existsByEmailAndIdIsNot(email, id);
    }

    @Override
    public Customer save(Customer customer) {
        LocationRegion locationRegion = locationRegionRepository.save(customer.getLocationRegion());
        customer.setLocationRegion(locationRegion);
        return customerRepository.save(customer);
    }

    @Override
    public void deposit(Customer customer, Deposit deposit) {

        customerRepository.incrementBalance(customer.getId(), deposit.getTransactionAmount());

        depositRepository.save(deposit);
    }

    @Override
    public Map<String, CustomerDTO> doTransfer(Transfer transfer) {

        Map<String, CustomerDTO> result = new HashMap<>();

        customerRepository.reduceBalance(transfer.getSender().getId(), transfer.getTransactionAmount());

        customerRepository.incrementBalance(transfer.getRecipient().getId(), transfer.getTransferAmount());

        transferRepository.save(transfer);

        CustomerDTO sender = customerRepository.getCustomerDTOById(transfer.getSender().getId());
        CustomerDTO recipient = customerRepository.getCustomerDTOById(transfer.getRecipient().getId());

        result.put("sender", sender);
        result.put("recipient", recipient);

        return result;
    }

    @Override
    public void remove(Long id) {

    }
}
