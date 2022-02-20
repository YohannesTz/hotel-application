package menu;

import api.AdminResource;
import exceptions.UnknownInputException;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getAdminResourceSingleton();

    public static void adminMenu() {
        String input = "";
        Scanner scanner = new Scanner(System.in);

        printAdminMenu();

        try{
            do {
                input = scanner.nextLine();

                switch (input.charAt(0)) {
                    case '1' -> seeAllCustomers();
                    case '2' -> seeAllRooms();
                    case '3' -> seeAllReservations();
                    case '4' -> addRoom();
                    case '5' -> MainMenu.printMainMenu();
                    default -> System.out.println("Unknown input\n");
                }
            } while(input.charAt(0) != '5' );
        } catch (Exception ex) {
            System.out.println("Oops! Empty or Unknown input...");
            printAdminMenu();
        }
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if(!rooms.isEmpty()) {
            adminResource.getAllRooms().forEach(System.out::println);
        } else {
            System.out.println("No rooms were found.");
        }
    }

    private static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();

        if (!customers.isEmpty()) {
            adminResource.getAllCustomers().forEach(System.out::println);
        } else {
            System.out.println("No customers were found.");
        }
    }

    private static void printAdminMenu() {
        System.out.print("\nAdmin Menu\n" +
                "1. See all Customers\n" +
                "2. See all Rooms\n" +
                "3. See all Reservations\n" +
                "4. Add a Room\n" +
                "5. Back to Main Menu\n" +
                "--------- Select an option -------\n");
    }

    private static void addRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter room number:");
        boolean isValidRoomNumber = false;
        String roomNumber = null;
        while(!isValidRoomNumber){
            try{
                roomNumber = scanner.nextLine();
                int rn = Integer.parseInt(roomNumber);

                if(rn < 0) {
                    throw new UnknownInputException("Input valid room number !!");
                }
                isValidRoomNumber = true;
            }catch(UnknownInputException ex){
                System.out.println(ex.getLocalizedMessage());
            }
        }

        double roomPrice = 0.0;
        RoomType roomType = null;

        boolean priceIsCorrect = false;
        while(!priceIsCorrect){
            try{
                System.out.println("Enter price per night:");
                roomPrice = Double.parseDouble(scanner.nextLine());
                priceIsCorrect = true;
            }catch(NumberFormatException e){
                System.out.println("Invalid room price! please input a double value!");
            }
        }

        boolean roomTypeCorrect = false;
        while (!roomTypeCorrect) {
            try{
                System.out.println("Enter 1 for Single bed, 2 for double bed: ");

                String roomTypeInput = scanner.nextLine();
                 switch (roomTypeInput) {
                    case "1" -> {
                        roomTypeCorrect = true;
                        roomType = RoomType.SINGLE;
                    }
                    case "2" -> {
                        roomTypeCorrect = true;
                        roomType = RoomType.DOUBLE;
                    }
                     default -> {
                        throw new UnknownInputException("Unknown input");
                     }
                }
            } catch (Exception ex) {
                System.out.println("Invalid room type! Input must be either 1 or 2.");
            }
        }

        Room room = new Room(roomNumber, roomPrice, roomType);
        adminResource.addRoom(Collections.singletonList(room));
        System.out.println("Success!");

        System.out.println("Would you like to add another room? y/n");
        String addRoom = scanner.nextLine();

        if (!(addRoom.equals("y") || addRoom.equals("n"))) {
            System.out.println("Invalid Input! exiting to main menu...");
            printAdminMenu();
            return;
        }


        if("y".equals(addRoom)) {
            addRoom();
        } else {
            printAdminMenu();
        }
    }
}
