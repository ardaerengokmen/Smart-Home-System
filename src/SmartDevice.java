import java.time.LocalDateTime;

public class SmartDevice {

    private String name;
    private String status;

    private LocalDateTime switchTime = null;
    protected LocalDateTime onTime = null;
    protected LocalDateTime offTime = null;

    public SmartDevice(String name, String status){
        this.name= name;
        this.status= status;

        setStatusTimes(this.status);
    }

    public String getName(){
        return this.name;
    }
    public void setName(String newName){
        this.name = newName;
    }

    public String getStatus(){
        return this.status;
    }
    public void setStatusTimes(String status ){
        //Saves switching on/off times of the device
        if(this.status.equals("Off")){
            this.offTime = SmartHomeSystem.currentTime;
        }
        else if(this.status.equals("On")){
            this.onTime = SmartHomeSystem.currentTime;
        }
    }
    public void setStatus(String newStatus){
        this.status= newStatus;
        setStatusTimes(this.status);
    }

    public LocalDateTime getSwitchTime(){
        return this.switchTime;
    }

    public void setSwitchTime(LocalDateTime newSwitchTime){
        this.switchTime = newSwitchTime;
    }

    public void switchDevice(){
        if (this.status.equals("Off")){
            this.status ="On";
        }
        else if(this.status.equals("On")) {
            this.status = "Off";
        }
        setSwitchTime(null);
        setStatusTimes(this.status);
    }

    public String getType(){
        return this.getClass().getSimpleName();
    }

}