package util;

import beans.models.Auditorium;
import beans.models.Event;
import beans.models.Rate;
import beans.models.Ticket;
import beans.models.User;
import beans.services.AuditoriumService;
import beans.services.BookingService;
import beans.services.DiscountService;
import beans.services.EventService;
import beans.services.UserService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class InitUtil implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        AuditoriumService auditoriumService = (AuditoriumService) ctx.getBean("auditoriumServiceImpl");
        BookingService bookingService = (BookingService) ctx.getBean("bookingServiceImpl");
        EventService eventService = (EventService) ctx.getBean("eventServiceImpl");
        UserService userService = (UserService) ctx.getBean("userServiceImpl");
        DiscountService discountService = (DiscountService) ctx.getBean("discountServiceImpl");

        String email = "dmitriy.vbabichev@gmail.com";
        String name = "Dmytro Babichev";
        String pass = "pass";
        String eventName = "The revenant";
        String auditoriumName = "Blue hall";
        Auditorium blueHall = auditoriumService.getByName(auditoriumName);
        Auditorium yellowHall = auditoriumService.getByName("Yellow hall");
        Auditorium redHall = auditoriumService.getByName("Red hall");
        LocalDateTime dateOfEvent = LocalDateTime.of(LocalDate.of(2016, 2, 5), LocalTime.of(15, 45, 0));

        //userService.register(new User(email, name, LocalDate.now(), pass));
        //userService.register(new User("laory@yandex.ru", name, LocalDate.of(1992, 4, 29), "pass123"));

        userService.register(new User(email, name, LocalDate.now(), pass,"REGISTERED_USER"));
        userService.register(new User("laory@yandex.ru", name, LocalDate.of(1992, 4, 29),"123","REGISTERED_USER"));
        userService.register(new User("user@gmail.com", "user", LocalDate.of(1992, 4, 29),"124","REGISTERED_USER"));
        userService.register(new User("admin@gmail.com", "admin", LocalDate.of(1992, 5, 29),"125","REGISTERED_USER,BOOKING_MANAGER"));
        userService.register(new User("admin1@gmail.com", "admin1", LocalDate.of(1992, 6, 29),"126","REGISTERED_USER,BOOKING_MANAGER"));
        userService.register(new User("admin2@gmail.com", "admin2", LocalDate.of(1992, 7, 29),"127","REGISTERED_USER,BOOKING_MANAGER"));

        User userByEmail = userService.getUserByEmail(email);
        userService.getUsersByName(name).forEach(System.out:: println);
        Event event1 = eventService.create(
                new Event(eventName, Rate.HIGH, 60, LocalDateTime.of(LocalDate.of(2016, 2, 5), LocalTime.of(9, 0, 0)),
                        blueHall));
        eventService.create(new Event(eventName, Rate.HIGH, 60, dateOfEvent, blueHall));
        Event event2 = eventService.create(
                new Event(eventName, Rate.HIGH, 60, LocalDateTime.of(LocalDate.of(2016, 2, 5), LocalTime.of(21, 18, 0)),
                        blueHall));
        eventService.create(
                new Event(eventName, Rate.HIGH, 90, LocalDateTime.of(LocalDate.of(2016, 2, 5), LocalTime.of(21, 18, 0)),
                        redHall));
        Event event = new Event(eventName, Rate.HIGH, 150,
                LocalDateTime.of(LocalDate.of(2016, 2, 5), LocalTime.of(21, 18, 0)), yellowHall);
        event = eventService.create(event);

        eventService.getAll().forEach(System.out:: println);

        eventService.remove(event2);
        eventService.getAll().forEach(System.out:: println);

        List<Integer> seats = Arrays.asList(23, 24, 25, 26);
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), seats, userByEmail);

        List<Integer> seats2 = Arrays.asList(27, 28, 29, 30);
        List<Integer> seats3 = Arrays.asList(2, 8, 9, 3);
        bookingService.bookTicket(userByEmail, new Ticket(event, LocalDateTime.now(), seats, userByEmail, ticketPrice));
        bookingService.bookTicket(userByEmail, new Ticket(event, LocalDateTime.now(), seats2, userByEmail,
                bookingService.getTicketPrice(event.getName(),
                        event.getAuditorium().getName(),
                        event.getDateTime(), seats2,
                        userByEmail)));
        bookingService.bookTicket(userByEmail, new Ticket(event, LocalDateTime.now(), seats3, userByEmail,
                bookingService.getTicketPrice(event.getName(),
                        event.getAuditorium().getName(),
                        event.getDateTime(), seats3,
                        userByEmail)));

        List<Ticket> ticketsForEvent = bookingService.getTicketsForEvent(event.getName(),
                event.getAuditorium().getName(),
                event.getDateTime());
        IntStream.range(0, ticketsForEvent.size()).forEach(
                i -> System.out.println("" + i + ") " + ticketsForEvent.get(i)));

        eventService.getByName("testName1");
        eventService.getByName("testName2");
        eventService.getByName("testName2");
        eventService.getByName("testName3");
        eventService.getByName(eventName);
        eventService.getEvent(event.getName(), event.getAuditorium(), event.getDateTime());

        bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats,
                userByEmail);
        bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats,
                userByEmail);
    }
}
