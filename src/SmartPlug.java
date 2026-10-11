import java.time.temporal.ChronoUnit;

public class SmartPlug  extends SmartDevice{

    private double ampere;
    private String plugStatus = "Out";
    private double totalConsumedWatt = 0;

    public SmartPlug(String name, String status, double ampere){
        super(name, status);
        this.ampere = ampere;

        if(ampere > 0){this.plugStatus = "In";}
        else if (ampere == 0){this.plugStatus = "Out";}
    }

    @Override
    public void setStatus(String newStatus){
        super.setStatus(newStatus);
        if (newStatus.equals("Off")){
            calculateTotalConsumedWatt();
        }
    }
    @Override
    public void switchDevice(){
        super.switchDevice();
        if (getStatus().equals("Off")){
            calculateTotalConsumedWatt();
        }
    }

    public double getAmpere(){
        return this.ampere;
    }
    public void setAmpere(double newAmpere){
        this.ampere = newAmpere;
        if(this.ampere>0){setPlugStatus("In");}
        else if(this.ampere == 0){setPlugStatus("Out");}

    }

    public String getPlugStatus(){
        return this.plugStatus;
    }
    private void setPlugStatus(String newPlugStatus){
        this.plugStatus = newPlugStatus;
    }

    private void calculateTotalConsumedWatt(){
            //Calculates consumed energy between on-off time
            if (this.onTime.isBefore(offTime)) {
                double passingMinutes = ChronoUnit.MINUTES.between(onTime, offTime);
                double passingHours = passingMinutes / 60.00;
                double consumedWatt = 220 * this.ampere * passingHours;
                this.totalConsumedWatt += consumedWatt;
            }
            else if (this.offTime.isBefore(onTime)) {}

    }
    public double getTotalConsumedWatt(){
        return this.totalConsumedWatt;
    }
}