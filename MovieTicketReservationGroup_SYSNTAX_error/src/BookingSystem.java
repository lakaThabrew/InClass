import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class BookingSystem {
    private ArrayList<Movie> movies = new ArrayList<>();
    //private ArrayList<String []> bookings = new ArrayList<>();
    
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
            if (movie1.Movie_code.equals(Movie_code) && movie1.date.equals(date) && movie1.Available_time.equals(time) && movie1.available_seats > required_seats)
            {
                return true;
            }
        }
        return false;
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

    public void printPdf(String custemer_name,String email,String movie_code,String date,String time,int tickets,double price) throws IOException {
        String filename = custemer_name + ".pdf";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println("Movie Ticket Reservation System");
            writer.println("==================================");
            writer.println(String.format("Name: %s", custemer_name));
            writer.println(String.format("Email: %s", email));
            writer.println(String.format("Movie Code: %s", movie_code));
            writer.println(String.format("Date: %s", date));
            writer.println(String.format("Time: %s", time));
            writer.println(String.format("Tickets: %d", tickets));
            writer.println(String.format("Price: Rs. %.2f", price));
        }
        System.out.println("Your bill is Sent to your email " + email);
        
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

    public void makebooking() throws IOException 
    {
        System.out.println("Welcome to Movie Ticket Reservation System\n");
        ScreenTimeOut timeout = new ScreenTimeOut(10);
        Thread thread = new Thread(timeout);
        thread.start();

        System.out.println("Available Movies:");
        this.DisplayMovies();

        Scanner scanner = new Scanner(System.in);
        String movie_code;

        while (true) {
            try {
                System.out.println("Enter the Movie code to book tickets:");
                movie_code = scanner.nextLine().trim();

                if (!isValidMovieCode(movie_code)) {
                    throw new InvalidMovieCodeException("Invalid movie code! Please select a valid movie.");
                }
                break; 
            } catch (InvalidMovieCodeException e) {
                System.out.println(e.getMessage());
            }
        }

        String date;
        System.out.println("Available Dates:");
        displayDates(movie_code);

        while (true) {
            try {
                System.out.println("Enter a date from the available options:");
                date = scanner.nextLine().trim();

                if (!isValidDate(movie_code, date)) {
                    throw new InvalidDateTimeException("Invalid date! Please enter a valid date from the list.");
                }
                break;
            } catch (InvalidDateTimeException e) {
                System.out.println(e.getMessage());
            }
        }

        String time;
        System.out.println("Available Times:");
        displayTimes(movie_code, date);
        while (true) {
            try {
                System.out.println("Enter a time from the available options:");
                time = scanner.nextLine().trim();

                if (!isValidTime(movie_code, date, time)) {
                    throw new InvalidDateTimeException("Invalid time! Please enter a valid time from the list.");
                }
                break;
            } catch (InvalidDateTimeException e) {
                System.out.println(e.getMessage());
            }
        }

        int tickets = 0;
        while (true) {
            try {
                System.out.println("Enter the number of tickets to book:");
                if (!scanner.hasNextInt()) {
                    scanner.next(); 
                    throw new InvalidTicketQuantityException("Invalid input! Please enter a valid number.");
                }
                tickets = scanner.nextInt();
                scanner.nextLine();

                if (tickets <= 0) {
                    throw new InvalidTicketQuantityException("Invalid number of tickets! Must be at least 1.");
                }

                if (!isavailable(movie_code, date, time, tickets)) {
                    throw new OverbookingException("Not enough seats available! Please select fewer tickets.");
                }
                break;
            } catch (InvalidTicketQuantityException | OverbookingException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Tickets are Booked");
        double price = getBill(movie_code, date, time, tickets);
        System.out.println("Total Price: Rs. " + price);

        System.out.println("Enter your name:");
        String customer_name = scanner.nextLine();

        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        printPdf(customer_name, email, movie_code, date, time, tickets, price);
        System.out.println("Booking Successful!");
        scanner.close();
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