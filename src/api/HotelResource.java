package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {

    private static final HotelResource HOTEL_RESOURCE_SINGLETON = new HotelResource();

    private final CustomerService customerService = CustomerService.getCustomerServiceSinglton();
    private final ReservationService reservationService = ReservationService.getReservationServiceSinglton();

    public static HotelResource getHotelResourceSingleton() {
        return HOTEL_RESOURCE_SINGLETON;
    }

    public Customer getCustomer(String customerEmail) {
        return customerService.getCustomer(customerEmail);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        return reservationService.reserveARoom(getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    public Collection<IRoom> findAlternativeRooms(final Date checkIn, final Date checkOut) {
        return reservationService.findAlternativeRooms(checkIn, checkOut);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = getCustomer(customerEmail);
        return customer == null ? Collections.emptyList() : reservationService.getCustomerReservation(getCustomer(customerEmail));
    }

    public Collection<Reservation> findAllReservations() {
        return reservationService.getAllReservations();
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }
}
