package model;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer {
    private final String fistName;
    private final String lastName;
    private final String email;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public Customer(String fistName, String lastName, String email) {
        this.fistName = fistName;
        this.lastName = lastName;

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if(matcher.find()) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Please Enter a Valid email");
        }
    }

    public String getFistName() {
        return fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(fistName, customer.fistName) && Objects.equals(lastName, customer.lastName) && email.equals(customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fistName, lastName, email);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "fistName='" + fistName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
