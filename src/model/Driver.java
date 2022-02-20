package model;

public class Driver {
    public static void main(String[] args) {
        Customer customer = new Customer("first", "second", "j@domain.com");
        System.out.println(customer);

        /*Customer customer2 = new Customer("first", "second", "email");
        System.out.println(customer2);*/
    }
}
