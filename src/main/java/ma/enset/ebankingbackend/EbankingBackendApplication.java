package ma.enset.ebankingbackend;

import ma.enset.ebankingbackend.entities.*;
import ma.enset.ebankingbackend.enums.AccountStatus;
import ma.enset.ebankingbackend.enums.OperationType;
import ma.enset.ebankingbackend.repositories.AccountOperationRepository;
import ma.enset.ebankingbackend.repositories.BankAccountRepository;
import ma.enset.ebankingbackend.repositories.CustomerRepository;
import ma.enset.ebankingbackend.services.BankAccountService;
import ma.enset.ebankingbackend.services.BankService;
import ma.enset.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.enset.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(BankAccountService bankAccountService) {
		return args -> {
			/*
			// Create 3 customers
			Stream.of("Amine", "Redone", "Aicha").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name.toLowerCase() + "@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			
			// Get list of customers and create accounts
			List<Customer> customers = bankAccountService.listCustomers();
			Random random = new Random();
			
			for(Customer customer : customers) {
				// Create current account
				try {
					bankAccountService.saveCurrentBankAccount(1000 + random.nextDouble() * 9000, 
							100 + random.nextDouble() * 900, 
							customer.getId());
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
				
				// Create saving account
				try {
					bankAccountService.saveSavingBankAccount(1000 + random.nextDouble() * 9000, 
							3.5 + random.nextDouble() * 2, 
							customer.getId());
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			// Create operations for each bank account
			List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
			for(BankAccount account : bankAccounts) {
				for(int i = 0; i < 10; i++) {
					try {
						if(i % 2 == 0) {
							bankAccountService.credit(account.getId(), 100 + random.nextDouble() * 400, "Credit operation");
						} else {
							bankAccountService.debit(account.getId(), 50 + random.nextDouble() * 200, "Debit operation");
						}
					} catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
						e.printStackTrace();
					}
				}
			}

			 */
		};
	}


	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository) {
		return args -> {
			// Create 3 customers
			Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name.toLowerCase() + "@gmail.com");
				customerRepository.save(customer);
			});

			// Create bank accounts (3 of each type)
			customerRepository.findAll().forEach(customer -> {
				// Create CurrentAccount
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random() * 9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.Activated);
				currentAccount.setCustomer(customer);
				currentAccount.setOverDraft(Math.random() * 1000);
				bankAccountRepository.save(currentAccount);

				// Create SavingAccount
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random() * 9000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.Created);
				savingAccount.setCustomer(customer);
				savingAccount.setInterestRate(3.5 + Math.random() * 2);
				bankAccountRepository.save(savingAccount);
			});

			// Create operations for each bank account
			bankAccountRepository.findAll().forEach(account -> {
				for (int i = 0; i < 3; i++) {
					AccountOperation operation = new AccountOperation();
					operation.setOperationDate(new Date());
					operation.setAmount(Math.random() * 500);
					operation.setType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
					operation.setBankAccount(account);
					accountOperationRepository.save(operation);
				}
			});
		};
	}
}
