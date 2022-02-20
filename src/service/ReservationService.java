package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {

    private static final ReservationService RESERVATION_SERVICE_SINGLTON = new ReservationService();
    private final HashMap<String, IRoom> roomHashMap = new HashMap<>();
    private final HashMap<String, Collection<Reservation>> reservationCollectionHashMap = new HashMap<>();

    private ReservationService(){}

    public static ReservationService getReservationServiceSinglton() {
        return RESERVATION_SERVICE_SINGLTON;
    }

    public void addRoom(IRoom iRoom) {
        roomHashMap.put(iRoom.getRoomNumber(), iRoom);
    }

    public IRoom getARoom(String roomId) {
        return roomHashMap.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return roomHashMap.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        Collection<Reservation> customerReservations = getCustomerReservation(customer);

        if (customerReservations == null) {
            customerReservations = new LinkedList<>();
        }

        customerReservations.add(reservation);
        reservationCollectionHashMap.put(customer.getEmail(), customerReservations);

        return reservation;
    }

    public Collection<IRoom> findAlternativeRooms(Date checkInDate, Date checkOutDate) {
        return findRooms(addSevenDaysToDate(checkInDate), addSevenDaysToDate(checkOutDate));
    }

    Date addSevenDaysToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);

        return calendar.getTime();
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<Reservation> allReservations = new LinkedList<>();
        Collection<IRoom> reservedRooms = new LinkedList<>();

        for(Collection<Reservation> res: reservationCollectionHashMap.values()) {
            allReservations.addAll(res);
        }

        for(Reservation r: allReservations) {
            if (doesOverlap(r, checkInDate, checkOutDate)) {
                reservedRooms.add(r.getRoom());
            }
        }

        return roomHashMap.values().stream().filter(room -> reservedRooms.stream()
                        .noneMatch(rRooms -> rRooms.equals(room)))
                .collect(Collectors.toList());
    }

    private boolean doesOverlap( Reservation reservation,  Date checkInDate,
                                         Date checkOutDate){

        return checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate());
    }

    public Collection<Reservation> getCustomerReservation(Customer customer) {
        return reservationCollectionHashMap.get(customer.getEmail());
    }

    public void printAllReservation() {
        Collection<Reservation> allReservations = new LinkedList<>();

        for(Collection<Reservation> res: reservationCollectionHashMap.values()) {
            allReservations.addAll(res);
        }

        if (allReservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : allReservations) {
                System.out.println(reservation + "\n");
            }
        }
    }

    public Collection<Reservation> getAllReservations() {
        final Collection<Reservation> allReservations = new LinkedList<>();

        for(Collection<Reservation> reservations : reservationCollectionHashMap.values()) {
            allReservations.addAll(reservations);
        }

        return allReservations;
    }
}
