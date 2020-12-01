package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.TransferEntity;
import ru.ixec.easyfinance.repositories.TransferRepository;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository traR;
    private final AccountService accS;

    public TransferEntity save(TransferEntity transfer){
        transfer.getIn().addAmount(transfer.getAmount());
        accS.save(transfer.getIn());
        transfer.getOut().subAmount(transfer.getAmount());
        accS.save(transfer.getOut());
        return traR.save(transfer);
    }
}
