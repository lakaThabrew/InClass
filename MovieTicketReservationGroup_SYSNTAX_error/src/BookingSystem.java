import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class BookingSystem {
    private ArrayList<Movie> movies = new ArrayList<>();
    private HashMap<String, Booking> Unsaved_bookings = new HashMap<>();
    
    public void loadMovies(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String header = reader.readLine();
            if (header == null) return;
            while ((line = reader.readLine()) != null) 
            {
                String[] data = line.split(",");
                if (data.length < 9) continue;

                Movie movie = new Movie(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]), Double.parseDouble(data[6]), data[7], data[8]);
                movies.add(movie);
            }
        }
    }

    public void DisplayMovies()
    {
        HashSet<String> uniqueMovies = new HashSet<>();

        for (Movie movie : movies) {
            String movieEntry = movie.Movie_code + " - " + movie.Movie_name;
            
            // Print only if it's not already printed
            if (!uniqueMovies.contains(movieEntry)) 
            {
                System.out.println(movieEntry);
                uniqueMovies.add(movieEntry);
            }
    }
    }

    public void displayDates(String movie_code)
    {
        HashSet<String> uniqueDates = new HashSet<>();    
        for (Movie movie : movies) {
            if (movie.Movie_code.equals(movie_code)) {
                uniqueDates.add(movie.date);  // Add unique dates
            }
        }

        for (String date : uniqueDates) {
            System.out.println(date);
        }
    }

    public void displayTimes(String movie_code, String date)
    {
        for (Movie movie1 : movies)
        {
            if (movie1.Movie_code.equals(movie_code) && movie1.date.equals(date))
            {
                System.out.println(movie1.Available_time);
            }
        }
    }

    public boolean isavailable(String Movie_code, String date, String time, int required_seats) {
        for (Movie movie1 : movies)
        {
            if (movie1.Movie_code.equals(Movie_code) && movie1.date.equals(date) && movie1.Available_time.equals(time) && movie1.available_seats >= required_seats)
            {
                return true;
            }
        }
        return false;
    }  
    
    public void getAvailableSeats(String movie_code, String date, String time) {
        for (Movie movie1 : movies)
        {
            if (movie1.Movie_code.equals(movie_code) && movie1.date.equals(date) && movie1.Available_time.equals(time))
            {
                System.out.println("Available Seats: " + movie1.available_seats);
            }
        }
    }
    
    public double getBill(String movie_code,String date,  String time,int tickets)
    {
        for (Movie movie1 : movies)
        {
            if (movie1.Movie_code.equals(movie_code) && movie1.date.equals(date) && movie1.Available_time.equals(time))
            {
                movie1.available_seats -= tickets;
                return movie1.ticket_price * tickets;
            }
        }
        return 0;
    }

    public void printPdf(String customer_name, String email, String movie_code, String date, String time, int tickets, double price) throws IOException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Start content stream
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        PDTrueTypeFont customFont = PDTrueTypeFont.load(document, new File("F:\\acadamic\\Semester 02\\Programme Contruction\\InClassLab - Online movie ticket reservation system\\MovieTicketReservationGroup_SYSNTAX_error\\src\\Arial.ttf"), WinAnsiEncoding.INSTANCE);
        contentStream.setFont(customFont, 14);
        contentStream.newLineAtOffset(100, 700); // Set text position

        // Write text into the PDF
        contentStream.showText("Movie Ticket Reservation System");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("==================================");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Name: " + customer_name);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Email: " + email);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Movie Code: " + movie_code);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Date: " + date);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Time: " + time);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Tickets: " + tickets);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText(String.format("Price: Rs. %.2f", price));

        // End text stream
        contentStream.endText();
        contentStream.close();


        String filename = customer_name + ".pdf";
        document.save(filename);
        document.close();
        System.out.println("Your bill has been saved as " + filename + " and sent to " + email);
}

    private boolean isValidMovieCode(String movie_code) {
        for (Movie movie : movies) {
            if (movie.Movie_code.equals(movie_code)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidDate(String movie_code, String date) {
        for (Movie movie : movies) {
            if (movie.Movie_code.equals(movie_code) && movie.date.equals(date)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidTime(String movie_code, String date, String time) {
        for (Movie movie : movies) {
            if (movie.Movie_code.equals(movie_code) && movie.date.equals(date) && movie.Available_time.equals(time)) {
                return true;
            }
        }
        return false;
    }

    public void makebooking(Scanner scanner) throws IOException 
    {
        Booking booking;

        System.out.println("\nWelcome to Movie Ticket Reservation System\n");
        System.out.println("=============================================\n");
        System.out.println("Please follow the instructions to book tickets\n");

        if (movies.isEmpty()) 
        {
            System.out.println("No movies available for booking!");
            scanner.close();
            return;
        }

        if (!Unsaved_bookings.isEmpty())
        {
            System.out.println("You have unsaved bookings. Do you want to continue? (Y/N)");
            String response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("Y")) 
            {
                for (String id: Unsaved_bookings.keySet()) 
                {
                    System.out.println("Unsaved booking found with ID: " + id);
                }

                System.out.println("Enter the booking ID to continue:");
                String id = scanner.nextLine().trim();
                booking = Unsaved_bookings.get(id);
                System.out.println("Continuing with booking ID: " + id);
            }
            else
            {
                booking = new Booking();
                Unsaved_bookings.put(booking.booking_id, booking);
            }
        }
        else
        {
            System.out.println("You haven't unsaved bookings.");
            booking = new Booking();
            Unsaved_bookings.put(booking.booking_id, booking);
        }

        System.out.println("Available Movies:");
        this.DisplayMovies();

        while (true) {
            try {
                System.out.println("Enter the Movie code to book tickets:");
                booking.movie_code = scanner.nextLine().trim();

                if (!isValidMovieCode(booking.movie_code)) {
                    throw new InvalidMovieCodeException("Invalid movie code! Please select a valid movie.");
                }
                break; 
            } catch (InvalidMovieCodeException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Available Dates:");
        displayDates(booking.movie_code);

        while (true) {
            try {
                System.out.println("Enter a date from the available options:");
                booking.date = scanner.nextLine().trim();

                if (!isValidDate(booking.movie_code, booking.date)) {
                    throw new InvalidDateTimeException("Invalid date! Please enter a valid date from the list.");
                }
                break;
            } catch (InvalidDateTimeException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Available Times:");
        displayTimes(booking.movie_code, booking.date);
        while (true) {
            try {
                System.out.println("Enter a time from the available options:");
                booking.time = scanner.nextLine().trim();

                if (!isValidTime(booking.movie_code, booking.date, booking.time)) {
                    throw new InvalidDateTimeException("Invalid time! Please enter a valid time from the list.");
                }
                break;
            } catch (InvalidDateTimeException e) {
                System.out.println(e.getMessage());
            }
        }

        getAvailableSeats(booking.movie_code, booking.date, booking.time);

        while (true) {
            try {
                System.out.println("Enter the number of tickets to book:");
                if (!scanner.hasNextInt()) {
                    scanner.next(); 
                    throw new InvalidTicketQuantityException("Invalid input! Please enter a valid number.");
                }
                booking.tickets = scanner.nextInt();
                scanner.nextLine();

                if (booking.tickets <= 0) {
                    throw new InvalidTicketQuantityException("Invalid number of tickets! Must be at least 1.");
                }

                if (!isavailable(booking.movie_code, booking.date, booking.time, booking.tickets)) {
                    throw new OverbookingException("Not enough seats available! Please select fewer tickets.");
                }
                break;
            } catch (InvalidTicketQuantityException | OverbookingException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Tickets are Booked");
        booking.price = getBill(booking.movie_code, booking.date, booking.time, booking.tickets);
        System.out.printf("Total Price: Rs. %.2f \n" , booking.price);

        System.out.println("Enter your name:");
        booking.customer_name = scanner.nextLine();

        System.out.println("Enter your email:");
        booking.email = scanner.nextLine();

        printPdf(booking.customer_name, booking.email, booking.movie_code, booking.date, booking.time, booking.tickets, booking.price);
        System.out.println("Booking Successful!");
        Unsaved_bookings.remove(booking.booking_id);
    }  
}


class InvalidMovieCodeException extends Exception {
    public InvalidMovieCodeException(String message) {
        super(message);
    }
}

class InvalidDateTimeException extends Exception {
    public InvalidDateTimeException(String message) {
        super(message);
    }
}

class InvalidTicketQuantityException extends Exception {
    public InvalidTicketQuantityException(String message) {
        super(message);
    }
}

class OverbookingException extends Exception {
    public OverbookingException(String message) {
        super(message);
    }
}