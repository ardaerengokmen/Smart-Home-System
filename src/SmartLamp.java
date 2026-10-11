public class SmartLamp extends SmartDevice{

    private int kelvin;
    private int brightness;

    public SmartLamp(String name, String status, int kelvin, int brightness){
        super(name, status);
        this.kelvin = kelvin;
        this.brightness = brightness;
    }

    public int getKelvin(){
        return this.kelvin;
    }
    public void setKelvin(int amount){
        this.kelvin = amount;
    }

    public int getBrightness(){
        return this.brightness;
    }
    public void setBrightness(int amount){
        this.brightness = amount;
    }

}


class SmartColorLamp extends SmartDevice{

    private String colorCode;
    private int kelvin;
    private int brightness;
    private String mode;

    //White mode
    public SmartColorLamp(String name, String status, int kelvin, int brightness){
        super(name, status);
        this.kelvin = kelvin;
        this.brightness= brightness;
        this.mode = "White";
    }

    //Color mode
    public SmartColorLamp(String name, String status, String colorCode, int brightness){
        super(name, status);
        setColorCode(colorCode);
        this.brightness = brightness;
        this.mode = "Color";
    }

    public int getKelvin(){
        return this.kelvin;
    }
    public void setKelvin(int amount){
        this.kelvin = amount;
    }

    public int getBrightness(){
        return this.brightness;
    }
    public void setBrightness(int amount){
        this.brightness = amount;
    }

    public String getColorCode(){
        return this.colorCode;
    }
    public void setColorCode(String newColorCode){
        this.mode = "Color";
        if(newColorCode.length()==8){
        this.colorCode = newColorCode;}
        else{this.colorCode = formatColorCode(newColorCode);}

    }

    public String getMode() {
        return this.mode;
    }

    private String formatColorCode(String colorCode){
        //If the color code is valid but its length is less than 8, formats it by adding 0
        String firstPart = colorCode.substring(0,2);
        String secondPart = colorCode.substring(2);

        String result = "0x";
        int length = secondPart.length();

        for(int i=1; i<=6-length; i++){
            result += "0";
        }
        result += secondPart;
        return result;
    }
}
