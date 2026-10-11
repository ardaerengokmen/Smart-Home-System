import java.time.temporal.ChronoUnit;

public class SmartCamera extends SmartDevice{

    private double megabytePerMinute;
    private double totalUsedMegabyte = 0;

    public SmartCamera(String name, String status, double megabytePerMinute){
        super(name, status);
        this.megabytePerMinute = megabytePerMinute;
    }

    @Override
    public void setStatus(String newStatus){
        super.setStatus(newStatus);
        if (newStatus.equals("Off")){
            calculateTotalUsedMegabyte();
        }
    }
    @Override
    public void switchDevice(){
        super.switchDevice();
        if (getStatus().equals("Off")){
            calculateTotalUsedMegabyte();
        }
    }

    public double getMegabytePerMinute(){
        return this.megabytePerMinute;
    }
    public void setMegabytePerMinute(int amount){
        this.megabytePerMinute = amount;
    }

    private void calculateTotalUsedMegabyte(){
        //Calculates used megabyte between on-off time
        if (this.onTime.isBefore(offTime)) {
            double passingMinutes = ChronoUnit.MINUTES.between(onTime, offTime);
            double usedMegabyte = this.megabytePerMinute * passingMinutes;
            this.totalUsedMegabyte += usedMegabyte;
        }
        else if (this.offTime.isBefore(onTime)) {}
    }

    public double getTotalUsedMegabyte(){
        return this.totalUsedMegabyte;
    }
}
