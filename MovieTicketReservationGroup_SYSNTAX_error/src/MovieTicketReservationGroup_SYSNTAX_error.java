import java.io.IOException;
/*Functional Requirements

1. Movie data loading: The system should read movie details(movie code, show time, available seats, total seats, ticket prices, language, genre) from a CSV file.
2. Movie Selection: Users should be able to select a movie by inputting the relevant movie code.
3. Date and Showtime Selection: Users should be able to select a date and showtime by inputting the data and showtime that user wish to watch the movie.
4. Ticket Booking: Users should be able to input the number of tickets they wish to book.
5. Error handling: The system must give an error invalid movie codes, incorrect showtimes and invalid number of tickets
5. Total Price Calculation: The system should calculate the total price of the tickets.
6. Email Notification: Generate the PDF including all the data and automatically email the PDF invoice to the user.
7. CSV File as Database: system must properly parse the CSV and store movie data in memory for rapid lookup and validation. Each column contains moviecode, moviename, date, showtime, totalsseats, availableseats, ticketprices, language, genre.  
8. Payment Integration: Simulate a payment process before completing the booking.

Non-Functional Requirements

1. Performance: system must be able to process moderate number of movies or a booking request in less than 2 seconds under average load. Response time and search time must be quick.
2. Security: Sensitive data like personal or payment details(e.g., user email) should be encrypted before it is stored or transmitted.
3. Scalability: The system should be design to handle larger numbers of movies, showtimes, and bookings records  without reducing performance and without significant slowdowns.
4. Usability: The system should have a minimal, easy-to-use command-line user friendly interface for easy interaction. Input prompts should be clear and error messages should be understandable.
5. Reliability: The system must be reliable, if there are no errors then it works correctly. When generate any exception, should handle without crashing the system. for a example, If an email fails to send, the user should be notified and allowed to re-enter their email.
6. Maintainability: The code should be modula and easy to maintain the system. Support to add different movie details. The code has to be OOP compliant to improve readabality and reusability.
7. Portability: The below system should be able to run in different interfaces(Windows, MacOS, Linux) without significant change in the code. */
import java.util.Scanner;

public class MovieTicketReservationGroup_SYSNTAX_error {
    public static void main(String[] args) throws IOException, InvalidMovieCodeException, InvalidDateTimeException, InvalidTicketQuantityException, OverbookingException {
        BookingSystem system = new BookingSystem();
        system.loadMovies("F:\\acadamic\\Semester 02\\Programme Contruction\\InClassLab - Online movie ticket reservation system\\MovieTicketReservationGroup_SYSNTAX_error\\src\\Movie Reservation Dataset.csv");
        boolean flag = true;
        Scanner scanner = new Scanner(System.in);
        while (flag) 
        {
            ScreenTimeOut timeout = new ScreenTimeOut(10);
            Thread thread = new Thread(timeout);
            thread.start();

            system.makebooking(scanner);
                
            System.out.println("Do you want to continue? (Y/N)");
            String ch = scanner.nextLine().trim().toUpperCase();
            if ("N".equals(ch))
            {
                flag = false;
            }
            timeout.stop();
        }
        scanner.close();
    }

}
