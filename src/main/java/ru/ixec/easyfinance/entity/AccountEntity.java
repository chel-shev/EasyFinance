package ru.ixec.easyfinance.entity;

import lombok.Data;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.type.AccountType;
import ru.ixec.easyfinance.type.KeyboardType;

import javax.persistence.*;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Entity(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue
    private Long accountId;
    private String name;
    private Long accountBalance;
    private Long volume;
    private boolean main;
    private AccountType accountType;

    @ManyToOne
    private ClientEntity client;

    @OneToMany(mappedBy = "account")
    private List<ExpenseEntity> expenseList;

    @OneToMany(mappedBy = "account")
    private List<IncomeEntity> incomeList;

    @OneToMany(mappedBy = "account")
    private List<LoanEntity> loanList;

    @OneToMany(mappedBy = "in")
    private List<TransferEntity> inTransferList;

    @OneToMany(mappedBy = "out")
    private List<TransferEntity> outTransferList;

    @OneToMany(mappedBy = "in")
    private List<InquiryEntity> accountInList;

    @OneToMany(mappedBy = "out")
    private List<InquiryEntity> accountOutList;

    @OneToMany(mappedBy = "account")
    private List<AccountHistoryEntity> clientHistoryList;

    public void addAccountBalance(long difference) {
        if (!isNull(volume) && accountBalance + difference > volume)
            throw new BotException("Сумма пополнения слишком большая!", KeyboardType.CANCEL);
        this.accountBalance = accountBalance + difference;
    }

    public void subAccountBalance(long difference) {
        if (accountBalance - difference < 0)
            throw new BotException("Недостаточно средств на счете!", KeyboardType.CANCEL);
        this.accountBalance = accountBalance - difference;
    }

    public String getInfoString() {
        String amount = null;
        switch (this.getAccountType()) {
            case CASH:
            case DEBIT:
                amount = String.valueOf((long) (this.getAccountBalance() / 100d));
                break;
            case CREDIT:
                amount = String.valueOf((long) (this.getAccountBalance() / 100d - this.getVolume() / 100d));
                break;
        }
        return getAccountType().getIcon() + " " + getName() + " (" + amount + ")";
    }
}
