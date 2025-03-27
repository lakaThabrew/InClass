public class Booking {
    String movie_code;
    String date;
    String time;
    int tickets;
    String customer_name;
    String email;
    double price; 
    String booking_id;
    static int booking_count = 0;

    public Booking()
    {
        this.tickets = 0;
        this.booking_id = "B" + booking_count;
        booking_count++;
    }
}
