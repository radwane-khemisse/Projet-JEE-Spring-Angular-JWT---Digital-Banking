package ma.enset.ebankingbackend.services;

import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.entities.CurrentAccount;
import ma.enset.ebankingbackend.entities.SavingAccount;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankService {
    private final BankAccountRepository bankAccountRepository;

    public BankService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Transactional
    public void consulter(String accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        System.out.println("===========================================");
        System.out.println("Account ID: " + bankAccount.getId());
        System.out.println("Balance: " + bankAccount.getBalance());
        System.out.println("Created At: " + bankAccount.getCreatedAt());
        System.out.println("Status: " + bankAccount.getStatus());
        System.out.println("Customer: " + bankAccount.getCustomer().getName());
        
        if (bankAccount instanceof CurrentAccount) {
            System.out.println("Account Type: Current Account");
            System.out.println("Overdraft: " + ((CurrentAccount) bankAccount).getOverDraft());
        } else if (bankAccount instanceof SavingAccount) {
            System.out.println("Account Type: Saving Account");
            System.out.println("Interest Rate: " + ((SavingAccount) bankAccount).getInterestRate());
        }
        
        System.out.println("===========================================");
        System.out.println("Operations:");
        System.out.println("===========================================");
        
        if (bankAccount.getAccountOperations() != null && !bankAccount.getAccountOperations().isEmpty()) {
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println("ID: " + op.getId());
                System.out.println("Date: " + op.getOperationDate());
                System.out.println("Type: " + op.getType());
                System.out.println("Amount: " + op.getAmount());
                System.out.println("-------------------------------------------");
            });
        } else {
            System.out.println("No operations found for this account");
        }
        System.out.println("===========================================");
    }
} 