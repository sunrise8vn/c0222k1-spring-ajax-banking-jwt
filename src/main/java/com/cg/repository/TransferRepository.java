package com.cg.repository;

import com.cg.model.Transfer;
import com.cg.model.dto.ITransferHistoryDTO;
import com.cg.model.dto.TransferHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {


    @Query("SELECT new com.cg.model.dto.TransferHistoryDTO (" +
                "t.id, " +
                "t.sender.id, " +
                "t.sender.fullName, " +
                "t.recipient.id, " +
                "t.recipient.fullName, " +
                "t.createdAt, " +
                "t.createdAt, " +
                "t.transferAmount, " +
                "t.fees, " +
                "t.feesAmount, " +
                "t.transactionAmount" +
            ") " +
            "FROM Transfer AS t"
    )
    List<TransferHistoryDTO> getAllHistory();


    @Query("" +
            "SELECT " +
                "t.id AS id, " +
                "t.sender.id AS senderId, " +
                "t.sender.fullName AS senderName, " +
                "t.recipient.id AS recipientId, " +
                "t.recipient.fullName AS recipientName, " +
                "t.createdAt AS createdOn, " +
                "t.createdAt AS createdAt, " +
                "t.transferAmount AS transferAmount, " +
                "t.fees AS fees, " +
                "t.feesAmount AS feesAmount, " +
                "t.transactionAmount AS transactionAmount " +
            "FROM Transfer AS t"
    )
    List<ITransferHistoryDTO> getAllHistoryInf();
}
