package menu;

import api.AdminResource;
import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    public static HotelResource hotelResource = HotelResource.getHotelResourceSingleton();

    public static void displayMainMenu() {
        String input = "";
        Scanner scanner = new Scanner(System.in);

        printMainMenu();

        try{
            do {
                input = scanner.nextLine();
                switch (input.charAt(0)) {
                    case '1' -> findAndReserveRoom();
                    case '2' -> seeReservations();
                    case '3' -> createAccount();
                    case '4' -> AdminMenu.adminMenu();
                    case '5' -> exitSystem();
                    default -> System.out.println("Unknown action\n");
                }
            } while (input.charAt(0) != '5' || input.length() > 1);
        } catch (Exception ex) {
            System.out.println("Oops! Empty or Unknown input...");
        }
    }

    private static void exitSystem() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    private static void findAndReserveRoom(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter check in date. example 15/02/2022");
        Date checkInDate = getDateFromInput(scanner);

        System.out.println("Enter check out date. example 17/02/2022");
        Date checkOutDate = getDateFromInput(scanner);

        if(areNotNull(checkInDate, checkOutDate)) {
            Collection<IRoom> unreservedRooms = hotelResource.findARoom(checkInDate, checkOutDate);

            if(unreservedRooms.isEmpty()) {
                Collection<IRoom> alternativeUnreservedRooms = hotelResource.findAlternativeRooms(checkInDate, checkOutDate);
                if (alternativeUnreservedRooms.isEmpty()) {
                    System.out.println("No rooms were found.");
                } else {
                    Date checkInAlternative = addSevenDays(checkInDate);
                    Date checkOutAlternative = addSevenDays(checkOutDate);

                    alternativeUnreservedRooms.forEach(System.out::println);

                    System.out.println("We found the above rooms on alternative dates:" +
                            "\nCheck-In Date:" + checkInAlternative +
                            "\nCheck-Out Date:" + checkOutAlternative);

                    reserveAlternativeRoom(scanner, checkInAlternative, checkOutAlternative);
                }
            } else {
                reserveRoom(scanner, checkInDate, checkOutDate, unreservedRooms);
            }
        }
    }

    static Date addSevenDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);

        return calendar.getTime();
    }

    private static void reserveAlternativeRoom(Scanner scanner, Date checkInDate, Date checkOutDate) {

        System.out.println("Would you like to reserve alternative rooms? y/n");
        String yesOrNo = scanner.nextLine();

        if (!(yesOrNo.equals("n") || yesOrNo.equals("y"))) {
            System.out.println("Please enter a valid input");
            reserveAlternativeRoom(scanner, checkInDate, checkOutDate);
        }

        if(yesOrNo.equals("n")) {
            System.out.println("Okay! thanks for your time!");
            return;
        }

        System.out.println("Please Enter an Email.");
        String customerEmail = scanner.nextLine();
        System.out.println("Customer Email: "); // I don't know why, but it doesn't work if we didn't out put something
        if (hotelResource.getCustomer(customerEmail) == null) {
            System.out.println("Oops! we didn't found that. \n You may need to create a new one.");
        } else {
            /*for (IRoom r : unreservedRooms) {
                if (r.getRoomNumber().equals(reservationNumber)) {
                    IRoom roomToBeBooked = hotelResource.getRoom(reservationNumber);
                    Reservation reservation = hotelResource.bookARoom(customerEmail, roomToBeBooked, checkInDate, checkOutDate);
                    System.out.println("Success!");
                    System.out.println(reservation);
                    break;
                }
            }*/

            Collection<Reservation> allReservations = HotelResource.getHotelResourceSingleton().findAllReservations();

            for (Reservation reservation : allReservations) {
                if(!(checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate()))) {
                    IRoom room = hotelResource.getRoom(reservation.getRoom().getRoomNumber());
                    Reservation altReservation = hotelResource.bookARoom(customerEmail, room, checkInDate, checkOutDate);
                    System.out.println("Success!");
                    System.out.println(altReservation);
                    break;
                }
            }
        }

        printMainMenu();
    }

    private static void reserveRoom(Scanner scanner, Date checkInDate, Date checkOutDate, Collection<IRoom> unreservedRooms) {

        if(unreservedRooms.isEmpty()) {
            System.out.println("No rooms found. ");
        } else {
            unreservedRooms.forEach(System.out::println);
        }

        System.out.println("Would you like to reserve? y/n");
        String yesOrNo = scanner.nextLine();
        System.out.println("Okay!");

        if (!(yesOrNo.equals("n") || yesOrNo.equals("y"))) {
            System.out.println("Please enter a valid input");
            reserveRoom(scanner, checkInDate, checkOutDate, unreservedRooms);
        }

        if(yesOrNo.equals("n")) {
            System.out.println("Okay! thanks for your time!");
            return;
        }

        System.out.println("Please Enter an Email.");
        String customerEmail = scanner.nextLine();
        System.out.println("Customer Input: " + customerEmail);
        if (hotelResource.getCustomer(customerEmail) == null) {
            System.out.println("Oops! we didn't found that. \n You may need to create a new one.");
        } else {
            System.out.println("What room number would you like to reserve?");
            String reservationNumber = scanner.nextLine();
            System.out.println("Okay reserving " + reservationNumber);
            for (IRoom r : unreservedRooms) {
                if (r.getRoomNumber().equals(reservationNumber)) {
                    IRoom roomToBeBooked = hotelResource.getRoom(reservationNumber);
                    Reservation reservation = hotelResource.bookARoom(customerEmail, roomToBeBooked, checkInDate, checkOutDate);
                    System.out.println("Success!");
                    System.out.println(reservation);
                    printMainMenu();
                    return;
                }
            }
        }
    }

    public static void printMainMenu() {
        System.out.print("\nWelcome! we have been waiting for you!\n" +
                "1. Find and reserve a room\n" +
                "2. See my reservations\n" +
                "3. Create an Account\n" +
                "4. Admin\n" +
                "5. Exit\n" +
                "------- Select an option ---------\n");
    }

    private static void createAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your Email: ");
        String email = scanner.nextLine();

        System.out.println("Enter your FirstName: ");
        String firstName = scanner.nextLine();

        System.out.println("Enter your LastName: ");
        String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Success!");

            printMainMenu();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            createAccount();
        }
    }

    private static boolean areNotNull(Date d1, Date d2) {
        return (d1 != null && d2 != null);
    }

    private static Date getDateFromInput(final Scanner scanner) {
        try {
            return new SimpleDateFormat("dd/MM/yy").parse(scanner.nextLine());
        } catch (ParseException ex) {
            System.out.println("Error: Invalid date.");
            findAndReserveRoom();
        }

        return null;
    }

    private static void seeReservations(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your email: ");
        String userEmail = scanner.nextLine();

        Collection<Reservation> reservations = hotelResource.getCustomersReservations(userEmail);
        if(reservations == null ||reservations.isEmpty()) {
            System.out.println("You have no reservations");
        } else {
            reservations.forEach(res -> System.out.println("\n" + res));
        }
    }



}
