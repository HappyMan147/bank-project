package org.kaczucha.org;

import org.kaczucha.org.repository.InMemoryClientRepository;
import org.kaczucha.org.service.BankService;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private BankService bankService;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        final InMemoryClientRepository repository = new InMemoryClientRepository(new ArrayList<>());
        bankService = new BankService(repository);

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("1 - add user");
                System.out.println("2 - find user");
                System.out.println("3 - exit app");
                final String next = sc.next();

                switch (next) {
                    case "1": {
                        addUser(sc);
                        break;
                    }
                    case "2": {
                        printUser(sc);
                        break;
                    }
                    case "3": {
                        return;

                    }
                }
            }
        }


    }

    private void printUser(Scanner sc) {
        System.out.println("Enter user email:");
        String mail = sc.next();
        System.out.println(bankService.findByEmail(mail));

    }

    private void addUser(Scanner sc) {
        System.out.println("Enter name:");
        final String name = sc.next();
        System.out.println("Enter email:");
        final String email = sc.next();
        System.out.println("Enter balance:");
        final double balance = sc.nextDouble();
        bankService.save(new Client(name, email, balance));
    }
}
