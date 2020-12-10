package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.AccountHistoryEntity;
import ru.ixec.easyfinance.entity.TransferEntity;
import ru.ixec.easyfinance.repositories.ClientHistoryRepository;
import ru.ixec.easyfinance.repositories.TransferRepository;
import ru.ixec.easyfinance.type.ActionType;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository traR;
    private final AccountService accS;
    private final ClientHistoryRepository cliHisR;

    @Transactional
    public TransferEntity save(TransferEntity transfer) {
        transfer.getIn().addAccountBalance(transfer.getAmount());
        accS.save(transfer.getIn());
        AccountHistoryEntity entityIn = new AccountHistoryEntity(null, transfer.getAmount(), transfer.getIn().getAccountBalance(), ActionType.TRANSFER, transfer.getDate(), transfer.getIn());
        cliHisR.save(entityIn);
        transfer.getOut().subAccountBalance(transfer.getAmount());
        accS.save(transfer.getOut());
        AccountHistoryEntity entityOut = new AccountHistoryEntity(null, transfer.getAmount(), transfer.getOut().getAccountBalance(), ActionType.TRANSFER, transfer.getDate(), transfer.getOut());
        cliHisR.save(entityOut);
        return traR.save(transfer);
    }
}
