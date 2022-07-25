package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT new com.cg.model.dto.CustomerDTO (" +
                "c.id, " +
                "c.fullName, " +
                "c.email, " +
                "c.phone, " +
                "c.balance, " +
                "c.locationRegion" +
            ") " +
            "FROM Customer AS c"
    )
    List<CustomerDTO> findAllCustomerDTO();

    @Query("SELECT new com.cg.model.dto.CustomerDTO (" +
                "c.id, " +
                "c.fullName, " +
                "c.email, " +
                "c.phone, " +
                "c.balance, " +
                "c.locationRegion" +
            ") " +
            "FROM Customer AS c " +
            "WHERE c.id = :id"
    )
    CustomerDTO getCustomerDTOById(@Param("id") Long id);


    @Query("SELECT new com.cg.model.dto.CustomerDTO (" +
                "c.id, " +
                "c.fullName, " +
                "c.email, " +
                "c.phone, " +
                "c.balance, " +
                "c.locationRegion" +
            ") " +
            "FROM Customer AS c " +
            "WHERE c.id <> :id"
    )
    List<CustomerDTO> getRecipientsWithOutSenderById(@Param("id") long id);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndIdIsNot(String email, Long id);

//    @Modifying(clearAutomatically=true, flushAutomatically = true)
    @Modifying
    @Query("" +
            "UPDATE Customer AS c " +
            "SET c.balance = c.balance + :transactionAmount " +
            "WHERE c.id = :id"
    )
    void incrementBalance(@Param("id") Long id, @Param("transactionAmount") BigDecimal transactionAmount);


//    @Modifying(clearAutomatically=true, flushAutomatically = true)
    @Modifying
    @Query("" +
            "UPDATE Customer AS c " +
            "SET c.balance = c.balance - :transactionAmount " +
            "WHERE c.id = :id"
    )
    void reduceBalance(@Param("id") Long id, @Param("transactionAmount") BigDecimal transactionAmount);


    List<Customer> findAllByIdNot(Long id);
}
