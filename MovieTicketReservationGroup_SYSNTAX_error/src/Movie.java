public class Movie {
    public String Movie_code;
    public String Movie_name;
    public String date;
    public String Available_time;
    public int total_seats;
    public int available_seats;
    public double ticket_price;
    public String language;
    public String genre;

    Movie(String Movie_code, String Movie_name, String date, String Available_time, int total_seats, int available_seats, double ticket_price, String language, String genre) {
        this.Movie_code = Movie_code;
        this.Movie_name = Movie_name;
        this.date = date;
        this.Available_time = Available_time;
        this.total_seats = total_seats;
        this.available_seats = available_seats;
        this.ticket_price = ticket_price;
        this.language = language;
        this.genre = genre;
    }

    

    public void book(int required_seats) {
        available_seats -= required_seats;
    }
}
