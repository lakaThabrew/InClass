public class ScreenTimeOut implements Runnable{
    public boolean Status;
    public int time;

    public ScreenTimeOut(int time) {
        this.time = time;
        Status = true;
    }

    @Override
    public void run() {
        while(Status)
        {
            try {
                Thread.sleep(1000*60);
                time--;
                if(time == 0)
                {
                    Status = false;
                    System.out.println("Your Time Out");
                }
            } catch (InterruptedException ex) {
                System.out.println("Error in ScreenTimeOut");
            }
        }    
}
}
