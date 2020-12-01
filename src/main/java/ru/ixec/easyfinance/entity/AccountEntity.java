package ru.ixec.easyfinance.entity;

import lombok.Data;
import ru.ixec.easyfinance.exception.BotException;
import ru.ixec.easyfinance.type.AccountType;
import ru.ixec.easyfinance.type.KeyboardType;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue
    private Long accountId;
    private String name;
    private Long amount;
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
    private List<ClientHistoryEntity> clientHistoryList;

    public void addAmount(long difference) {
        if (amount + difference > volume)
            throw new BotException("Сумма пополнения слишком большая!", KeyboardType.CANCEL);
        this.amount = amount + difference;
    }

    public void subAmount(long difference) {
        if (amount - difference < 0)
            throw new BotException("Недостаточно средств на счете!", KeyboardType.CANCEL);
        this.amount = amount - difference;
    }

    public String getInfoString() {
        String amount = null;
        switch (this.getAccountType()) {
            case CASH:
            case DEBIT:
                amount = String.valueOf((long) (this.getAmount() / 100d));
                break;
            case CREDIT:
                amount = String.valueOf((long) (this.getAmount() / 100d - this.getVolume() / 100d));
                break;
        }
        return getAccountType().getIcon() + " " + getName() + " (" + amount + ")";
    }
}
