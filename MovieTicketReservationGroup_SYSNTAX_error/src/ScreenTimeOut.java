public class ScreenTimeOut implements Runnable{
    private boolean Status;
    private int time;

    public ScreenTimeOut(int time) {
        this.time = time;
        this.Status = true;
    }

    public void stop() {
        Status = false;
    }
    
    @Override
    public void run() {
        while(Status)
        {
            try {
                Thread.sleep(1000*60);
                time--;
                if(time <= 0)
                {
                    Status = false;
                    System.exit(0);
                }
            } catch (InterruptedException ex) {
                System.out.println("Error in ScreenTimeOut");
                Thread.currentThread().interrupt();
            }
        }    
    }
}
