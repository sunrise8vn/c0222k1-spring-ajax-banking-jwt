package com.cg.service.transfer;


import com.cg.model.Transfer;
import com.cg.model.dto.ITransferHistoryDTO;
import com.cg.model.dto.TransferHistoryDTO;
import com.cg.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Override
    public List<Transfer> findAll() {
        return null;
    }

    @Override
    public List<TransferHistoryDTO> getAllHistory() {
        return transferRepository.getAllHistory();
    }

    @Override
    public List<ITransferHistoryDTO> getAllHistoryInf() {
        return transferRepository.getAllHistoryInf();
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Transfer getById(Long id) {
        return null;
    }

    @Override
    public Transfer save(Transfer transfer) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
