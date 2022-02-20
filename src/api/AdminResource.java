package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    private static final AdminResource ADMIN_RESOURCE_SINGLETON = new AdminResource();

    private final CustomerService customerService = CustomerService.getCustomerServiceSinglton();
    private final ReservationService reservationService = ReservationService.getReservationServiceSinglton();

    public static AdminResource getAdminResourceSingleton() {
        return ADMIN_RESOURCE_SINGLETON;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {
        rooms.forEach(iRoom -> {
            reservationService.addRoom(iRoom);
        });
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }
}
