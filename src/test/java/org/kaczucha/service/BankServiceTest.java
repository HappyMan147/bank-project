package org.kaczucha.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kaczucha.org.Client;
import org.kaczucha.org.repository.InMemoryClientRepository;
import org.kaczucha.org.service.BankService;
import org.kaczucha.org.service.NoSuficientFundException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@SuppressWarnings("RedundantCast")
public class BankServiceTest {
    private BankService service;
    private List<Client> clients;


    @BeforeEach
    public void setup() {
        clients = new ArrayList<>();
        //noinspection RedundantCast
        service = new BankService(new InMemoryClientRepository((List<Client>) clients));

    }

    @Test
    public void transfer_allParamsOk_fundsTransferred() {

        // given
        final String emailFrom = "a@a.pl";
        final String emailTo = "b@b.pl";
        final Client clientFrom = new Client("Alek", emailFrom, 1000);
        final Client clientTo = new Client("Bartek", emailTo, 500);
        clients.add(clientFrom);
        clients.add(clientTo);
        final double amount = 100;

        // when
        service.transfer(emailFrom, emailTo, amount);
        // then
        final Client actualFromClient = service.findByEmail(emailFrom);
        final Client actualToClient = service.findByEmail(emailTo);

        Client expectedClientFrom = new Client("Alek", emailFrom, 900);
        Client expectedClientTo = new Client("Bartek", emailTo, 600);

        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions
                .assertThat(expectedClientFrom)
                .isEqualTo(actualFromClient);
        softAssertions
                .assertThat(expectedClientTo)
                .isEqualTo(actualToClient);

        softAssertions.assertAll();
    }

    @Test
    public void transfer_allFounds_fundsTransferred() {
        // given
        String emailFrom = "a@a.pl";
        String emailTo = "b@b.pl";
        Client clientFrom = new Client("Alek", emailFrom, 1000);
        Client clientTo = new Client("Bartek", emailTo, 500);
        clients.add(clientFrom);
        clients.add(clientTo);
        final double amount = 1000;

        // when
        service.transfer(emailFrom, emailTo, amount);
        // then
        final Client actualFromClient = service.findByEmail(emailFrom);
        final Client actualToClient = service.findByEmail(emailTo);

        final Client expectedClientFrom = new Client("Alek", emailFrom, 0);
        final Client expectedClientTo = new Client("Bartek", emailTo, 1500);

        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions
                .assertThat(expectedClientFrom)
                .isEqualTo(actualFromClient);
        softAssertions
                .assertThat(expectedClientTo)
                .isEqualTo(actualToClient);

        softAssertions.assertAll();
    }

    @Test
    public void transfer_notEnoughtFunds_thrownNoSuficientFundException() {
        // given
        String emailFrom = "a@a.pl";
        String emailTo = "b@b.pl";
        Client clientFrom = new Client("Alek", emailFrom, 100);
        Client clientTo = new Client("Bartek", emailTo, 500);
        clients.add(clientFrom);
        clients.add(clientTo);
        final double amount = 1000;

        // when / then
        Assertions.assertThrows(
                NoSuficientFundException.class,
                () -> service.transfer(emailFrom, emailTo, amount)
        );


    }

    @Test
    public void transfer_negativeAmount_thrownIllegalArgumentException() {
        // given
        String emailFrom = "a@a.pl";
        String emailTo = "b@b.pl";
        Client clientFrom = new Client("Alek", emailFrom, 100);
        Client clientTo = new Client("Bartek", emailTo, 500);
        clients.add(clientFrom);
        clients.add(clientTo);
        final double amount = -1000;

        // when / then
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(emailFrom, emailTo, amount)
        );


    }

    @Test
    public void transfer_toSameClient_thrownException() {
        //given
        String email = "a@a.pl";
        Client client = new Client("Alek", email, 100);
        clients.add(client);
        //when / then
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(email, email, 10)
        );
    }

    @Test
    public void withdraw_correctAmount_balanceChangedCorrectly() {
        //giveną
        String email = "a@a.pl";
        Client client = new Client("Alek", email, 100);
        clients.add(client);
        //when
        service.withdraw(email, 50);
        //then
        Client expectedClient = new Client("Alek", email, 50);
        Client actualClient = clients.get(0);
        Assertions.assertEquals(expectedClient, actualClient);
    }

    @Test
    public void withdraw_allBalance_balanceSetToZero() {
        //giveną
        final String email = "a@a.pl";
        final Client client = new Client("Alek", email, 100);
        clients.add(client);
        //when
        service.withdraw(email, 100);
        //then
        Client expectedClient = new Client("Alek", email, 0);
        Client actualClient = clients.get(0);
        Assertions.assertEquals(expectedClient, actualClient);
    }

    @Test
    public void withdraw_negativeAmount_throwsIlligalArgumentException() {
        //giveną
        String email = "a@a.pl";
        Client client = new Client("Alek", email, 100);
        clients.add(client);
        //when
        final int amount = -100;
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdraw(email, amount));
    }

    @Test
    public void withdraw_zeroAmount_throwsIllegalArgumentExceprion() {
        //giveną
        String email = "a@a.pl";
        Client client = new Client("Alek", email, 100);
        clients.add(client);
        //when
        final int amount = 0;
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> service.withdraw(email, amount)
        );
    }

    @Test
    public void withdraw_amountBiggerThenBalance_throwsNoSufficientFundException() {
        //giveną
        String email = "a@a.pl";
        Client client = new Client("Alek", email, 100);
        clients.add(client);
        final int amount = 1000;
        //when

        Assertions.assertThrows(
                NoSuficientFundException.class,
                () -> service.withdraw(email, amount)
        );
    }

    @Test
    public void withdraw_incorrectEmail_throwsNoSuchElementException() {
        //giveną
        String email = "incorect_email@a.pl";
        final int amount = 1000;
        //when
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> service.withdraw(email, amount)
        );
    }

    @Test
    public void withdraw_upperCaseEmail_balanceChangeCorrectly() {
        //giveną
        String email = "A@A.pl";
        Client client = new Client("Alek", "a@a.pl", 100);
        clients.add(client);
        //when
        service.withdraw(email, 50);
        //then
        Client expectedClient = new Client("Alek", "a@a.pl", 50);
        Client actualClient = clients.get(0);
        Assertions.assertEquals(expectedClient, actualClient);
    }


    @Test
    public void withdraw_nullEmail_throwsIllegalArgumentException() {
        //giveną
        final String email = null;
        final int amount = 1000;
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdraw(email, amount));
    }

    @Test
    public void transfer_correctFloatingPointAmount_balanceChangedCorrectly() {

        // given
        final String email = "a@a.pl";
        final Client client = new Client("Alek", email, 100);
        clients.add(client);

        // when
        service.withdraw(email, 50.5);
        // then
        Client expectedClient = new Client("Alek", email, 49.5);
        Client actualClient = clients.get(0);
        Assertions.assertEquals(expectedClient, actualClient);
    }
}

