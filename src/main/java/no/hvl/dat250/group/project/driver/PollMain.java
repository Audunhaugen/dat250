package no.hvl.dat250.group.project.driver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.*;

import java.util.Set;

public class PollMain {

  static final String PERSISTENCE_UNIT_NAME = "group project";

  public static void main(String[] args) {
    try (EntityManagerFactory factory = Persistence.createEntityManagerFactory(
        PERSISTENCE_UNIT_NAME); EntityManager em = factory.createEntityManager()) {
      em.getTransaction().begin();
      createObjects(em);
      em.getTransaction().commit();
    }

  }

  private static void createObjects(EntityManager em) {
//    // Create a Pincode
//    Answer pincode = new Answer();
//    pincode.setCode("123");
//    pincode.setCount(1);
//
//// Create a CreditCard
//    Poll creditCard = new Poll();
//    creditCard.setNumber(12345);
//    creditCard.setBalance(-5000);
//    creditCard.setCreditLimit(-10000);
//    creditCard.setPincode(pincode);
//    // Create a second CreditCard
//    Poll sndCreditCard = new Poll();
//    sndCreditCard.setNumber(123);
//    sndCreditCard.setBalance(1);
//    sndCreditCard.setCreditLimit(2000);
//    sndCreditCard.setPincode(pincode);
//
//    // Create a Bank
//    Bank bank = new Bank();
//    bank.setName("Pengebank");
//    bank.setOwnedCards(Set.of(creditCard,sndCreditCard));
//
//    // Set the owning bank for the CreditCard
//    creditCard.setOwningBank(bank);
//    sndCreditCard.setOwningBank(bank);
//
//    // Create a Customer
//    User customer = new User();
//    customer.setName("Max Mustermann");
//
//    // Associate the creditcards with the Customer
//    customer.setCreditCards(Set.of(creditCard,sndCreditCard));
//
//    // Create an Address
//    Result address = new Result();
//    address.setStreet("Inndalsveien");
//    address.setNumber(28);
//    address.setOwners(Set.of(customer));
//
//    // Associate the Address with the Customer
//    customer.setAddresses(Set.of(address));
//
//    // Persist the entities to the database
//    em.persist(bank);
//    em.persist(customer);
//    em.persist(address);
//    em.persist(creditCard);
//    em.persist(sndCreditCard);
//    em.persist(pincode);
  }
}
