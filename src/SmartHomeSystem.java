import java.util.Locale;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SmartHomeSystem {

    public static LocalDateTime currentTime = null;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");

    public static boolean isValidDate(String date){
        // Checks if the given date is valid or not
        boolean flag = false;
        try{
            ArrayList<Integer> parts = separateDate(date);

            int year = parts.get(0);
            int month = parts.get(1);
            int day = parts.get(2);
            int hour = parts.get(3);
            int minute = parts.get(4);
            int second = parts.get(5);

            if ((second >= 0 && second<=59) && (minute>= 0 && minute <= 59) && (hour >= 0 && hour <= 23) && (day >= 1 && day<= 31) && (month >= 1 && month <= 12)){
                flag = true;
            }
        }catch (NumberFormatException e){}

        return flag;
    }

    public static ArrayList<Integer> separateDate(String date){
        // Separates the given date into year,month,day,hour,minute,second
        String[] parts = date.split("[-_:]");
        ArrayList<Integer> result = new ArrayList<>();

        for (String part: parts){
            result.add(Integer.parseInt(part));
        }
        return result;
    }

    public static boolean isValidStatus(String status){
        //Checks if the given status is valid (Off or On) or not
        boolean flag = false;
        if (status.equals("Off") || status.equals("On")){
            flag = true;
        }
        return flag;
    }

    public static boolean isValidKelvin(int kelvin){
        //Checks if the given kelvin value is in range of (2000-6500) or not
        boolean flag = false;
        if (kelvin >= 2000 && kelvin <= 6500){
            flag = true;
        }
        return flag;
    }

    public static boolean isValidBrightness(int brightness){
        //Checks if the given brightness amount is in range of (0-100) or not
        boolean flag = false;
        if (brightness >= 0 && brightness <= 100){
            flag = true;
        }
        return flag;
    }

    public static boolean isInteger(String number){
        //Checks if the given value is integer or not
        try{
            int result = Integer.parseInt(number);
            return true;
        }catch (Exception e){return false;}
    }

    public static boolean isValidColorCode1(String colorCode){
        //First step to check the validness of the color code
        //Checks if the given color code includes "0x" and chars that are in range of('A'-'F') or not
        boolean flag = true;

            if (colorCode.length()<3){
                flag= false;
            }else{
                String firstPart = colorCode.substring(0,2);
                String secondPart = colorCode.substring(2);


                if (!firstPart.equals("0x") ){
                    flag= false;
                }else{
                    // If any char is out of the range('A'-'F'), returns false
                    for (int i=0; i<secondPart.length(); i++){
                        char c = secondPart.charAt(i);
                        if(!((c>='0' && c<='9') || (c>='A' && c<='F'))){
                            flag=false;
                        }
                    }
                }
            }
        return flag;
    }
    public static boolean isValidColorCode2(String colorCode){
        //Second step to check the validness of the color code
        //If the color code is in range of (0x0-0xFFFFFF), returns true
        boolean flag = true;

        String firstPart = colorCode.substring(0,2);
        String secondPart = colorCode.substring(2);
        if(secondPart.length() > 6){flag = false;}

        return flag;
    }

    public static SmartDevice getDevice(String name, ArrayList<SmartDevice> smartDevices){
        //Returns the device whose name is given
        for (SmartDevice device: smartDevices){
            if (device.getName().equals(name)){
                return device;
            }
        }return null;
    }

    public static String giveInfo(SmartDevice device){
        //Returns string that includes information about the given device
        String type = device.getType();
        String info = "";

        //Smart Plug
        if(type.equals("SmartPlug")){
            SmartPlug plug = (SmartPlug) (device);
            info += String.format("Smart Plug %s is %s and consumed %.2fW so far (excluding present usage)", plug.getName(), plug.getStatus().toLowerCase(), plug.getTotalConsumedWatt());
        }
        //Smart Camera
        else if (type.equals("SmartCamera")){
            SmartCamera camera = (SmartCamera) (device);
            info += String.format("Smart Camera %s is %s and used %.2f MB of storage so far (excluding present usage)", camera.getName(), camera.getStatus().toLowerCase(), camera.getTotalUsedMegabyte());
        }
        //Smart Lamp
        else if (type.equals("SmartLamp")){
            SmartLamp lamp = (SmartLamp) (device);
            info += String.format("Smart Lamp %s is %s and its kelvin value is %dK with %d%% brightness", lamp.getName(), lamp.getStatus().toLowerCase(), lamp.getKelvin(), lamp.getBrightness());
        }
        //Smart Color Lamp
        else if (type.equals("SmartColorLamp")){
            SmartColorLamp colorLamp = (SmartColorLamp) (device);

            //White mode
            if(colorLamp.getMode().equals("White")){
            info += String.format("Smart Color Lamp %s is %s and its color value is %dK with %d%% brightness", colorLamp.getName(), colorLamp.getStatus().toLowerCase(), colorLamp.getKelvin(), colorLamp.getBrightness());
            }
            //Color mode
            else if (colorLamp.getMode().equals("Color")){
               info += String.format("Smart Color Lamp %s is %s and its color value is %s with %d%% brightness", colorLamp.getName(), colorLamp.getStatus().toLowerCase(), colorLamp.getColorCode(), colorLamp.getBrightness());
            }
        }
        String switchTime = "null";
        if(device.getSwitchTime()!=null){switchTime = device.getSwitchTime().format(formatter);}
        info += ", and its time to switch its status is " + switchTime + ".\n";
        return info;
    }

    public static ArrayList<SmartDevice> sortDevices(ArrayList<SmartDevice> smartDevices, ArrayList<LocalDateTime> switchTimes){
        //Sorts the devices respect to their switch times
        ArrayList<SmartDevice> devicesToSwitch = new ArrayList<>();

        //Extracts devices that have a switch time
        for(SmartDevice device: smartDevices){
            if(device.getSwitchTime() != null){
                devicesToSwitch.add(device);
            }
        }
        smartDevices.removeAll(devicesToSwitch);

        ArrayList<SmartDevice> sorted = new ArrayList<>();

        //Matches devices with sorted switch times
        for (LocalDateTime switchTime: switchTimes){
            for (SmartDevice device: devicesToSwitch){
                if (device.getSwitchTime().equals(switchTime)){
                    sorted.add(device);
                }
            }
        }
        //Pushes sorted devices back to the top of the main list
        for (int i= sorted.size() -1; i>=0; i--){
            smartDevices.add(0, sorted.get(i));
        }

        return smartDevices;
    }

    public static void smartHomeManager(String inputFileName, String outputFileName){
        //Manages all types of commands, stores data of devices, writes the output into a text file
        try {
            File inputFile = new File(inputFileName);
            Scanner scan = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(outputFileName);

            String initialTime = "";

            ArrayList<SmartDevice> smartDevices = new ArrayList<>();
            ArrayList<String> smartDeviceNames = new ArrayList<>();
            ArrayList<LocalDateTime> switchTimes = new ArrayList<>();

            ArrayList<String> commands = new ArrayList<>();

            while (scan.hasNext()) {
                String line = scan.nextLine().trim();

                if (line.isEmpty()){continue;}
                writer.println("COMMAND: " + line);
                commands.add(line);
                String[] words = line.split("\\t");
                try {
                    //Initial command
                    if (initialTime.length()==0) {
                        //First command must be SetInitialTime
                        if (words[0].equals("SetInitialTime") && words.length==2) {
                            initialTime = words[1];
                            if (isValidDate(initialTime)){

                                currentTime = LocalDateTime.parse(initialTime, formatter);
                                writer.println("SUCCESS: Time has been set to " + initialTime + "!");
                            }else{
                                throw new WrongInitialDateException();
                            }
                        }else{
                            throw new FirstCommandException();
                        }
                    }
                    else{
                        try {
                            // This try-catch handles NumberFormatException for all parsing operations below

                            //Nop command
                            if (words[0].equals("Nop") && words.length == 1) {
                                if (switchTimes.size() == 0) {
                                    throw new NothingToSwitchException();
                                } else {
                                    currentTime = switchTimes.get(0);
                                    for (SmartDevice device : smartDevices) {
                                        if (device.getSwitchTime() != null && device.getSwitchTime().equals(currentTime)) {
                                            device.switchDevice();
                                        }
                                    }
                                    switchTimes.remove(0);
                                    smartDevices = sortDevices(smartDevices, switchTimes);
                                }
                            }

                            //Add command
                            else if (words[0].equals("Add") && words.length >= 3 && words.length <= 6) {

                                //Smart Plug
                                if (words[1].equals("SmartPlug")) {

                                    String name = words[2];
                                    if (smartDeviceNames.contains(name)) {
                                        throw new SameNameException();
                                    }

                                    if (words.length == 3) {
                                        smartDevices.add(new SmartPlug(name, "Off", 0));
                                        smartDeviceNames.add(name);
                                    } else if (words.length == 4) {
                                        if (isValidStatus(words[3])) {
                                            String status = words[3];
                                            smartDevices.add(new SmartPlug(name, status, 0));
                                            smartDeviceNames.add(name);
                                        } else {
                                            throw new ErroneousCommandException();
                                        }
                                    } else if (words.length == 5) {
                                        if (isValidStatus(words[3])) {
                                            String status = words[3];

                                            double ampere = Double.parseDouble(words[4]);

                                            if (ampere > 0) {
                                                smartDevices.add(new SmartPlug(name, status, ampere));
                                                smartDeviceNames.add(name);
                                            } else {
                                                throw new InvalidAmpereException();
                                            }
                                        }
                                    }
                                }

                                //Smart Camera
                                else if (words[1].equals("SmartCamera") && words.length>=4 && words.length<=5) {

                                    String name = words[2];
                                    if (smartDeviceNames.contains(name)) {
                                        throw new SameNameException();
                                    }
                                    double megabytePerMinute = Double.parseDouble(words[3]);

                                    if (megabytePerMinute <= 0) {
                                        throw new InvalidMegabyteException();
                                    }

                                    if (words.length == 4) {
                                        smartDevices.add(new SmartCamera(name, "Off", megabytePerMinute));
                                        smartDeviceNames.add(name);

                                    } else if (words.length == 5) {
                                        String status = words[4];
                                        smartDevices.add(new SmartCamera(name, status, megabytePerMinute));
                                        smartDeviceNames.add(name);
                                    }
                                }

                                //Smart Lamp
                                else if (words[1].equals("SmartLamp")) {

                                    String name = words[2];
                                    if (smartDeviceNames.contains(name)) {
                                        throw new SameNameException();
                                    }
                                    String status = "Off";
                                    int kelvin = 4000;
                                    int brightness = 100;

                                    if (words.length == 3) {
                                        smartDevices.add(new SmartLamp(name, status, kelvin, brightness));
                                        smartDeviceNames.add(name);
                                    } else if (words.length == 4) {
                                        status = words[3];
                                        if (isValidStatus(status)) {
                                            smartDevices.add(new SmartLamp(name, status, kelvin, brightness));
                                            smartDeviceNames.add(name);
                                        } else {
                                            throw new ErroneousCommandException();
                                        }
                                    } else if (words.length == 6) {
                                        status = words[3];

                                        kelvin = Integer.parseInt(words[4]);
                                        brightness = Integer.parseInt(words[5]);

                                        if (isValidStatus(status)) {
                                            if (isValidKelvin(kelvin)) {
                                                if (isValidBrightness(brightness)) {
                                                    smartDevices.add(new SmartLamp(name, status, kelvin, brightness));
                                                    smartDeviceNames.add(name);
                                                } else {
                                                    throw new InvalidBrightnessException();
                                                }
                                            } else {
                                                throw new InvalidKelvinException();
                                            }
                                        } else {
                                            throw new ErroneousCommandException();
                                        }
                                    }
                                }

                                //Smart Color Lamp
                                else if (words[1].equals("SmartColorLamp")) {

                                    String name = words[2];
                                    if (smartDeviceNames.contains(name)) {
                                        throw new SameNameException();
                                    }
                                    String status = "Off";
                                    int kelvin = 4000;
                                    int brightness = 100;

                                    if (words.length == 3) {
                                        smartDevices.add(new SmartColorLamp(name, status, kelvin, brightness));
                                        smartDeviceNames.add(name);
                                    } else if (words.length == 4) {
                                        status = words[3];
                                        if (isValidStatus(status)) {
                                            smartDevices.add(new SmartColorLamp(name, status, kelvin, brightness));
                                            smartDeviceNames.add(name);
                                        } else {
                                            throw new ErroneousCommandException();
                                        }
                                    } else if (words.length == 6) {
                                        status = words[3];
                                        if (isValidStatus(status)) {

                                                //White mode
                                                if (isInteger(words[4])) {
                                                    kelvin = Integer.parseInt(words[4]);
                                                    brightness = Integer.parseInt(words[5]);

                                                    if (isValidKelvin(kelvin)) {
                                                        if (isValidBrightness(brightness)) {
                                                            smartDevices.add(new SmartColorLamp(name, status, kelvin, brightness));
                                                            smartDeviceNames.add(name);

                                                        } else {throw new InvalidBrightnessException();}
                                                    } else {throw new InvalidKelvinException();}

                                                }// Color mode
                                                else {
                                                    String colorCode = words[4];
                                                    brightness = Integer.parseInt(words[5]);

                                                    if (isValidColorCode1(colorCode)) {
                                                        if (isValidColorCode2(colorCode)) {
                                                            if (isValidBrightness(brightness)) {
                                                                smartDevices.add(new SmartColorLamp(name, status, colorCode, brightness));
                                                                smartDeviceNames.add(name);

                                                            } else {throw new InvalidBrightnessException();}
                                                        } else {throw new InvalidColorCodeException();}
                                                    } else {throw new ErroneousCommandException();}
                                                }
                                        }
                                    }
                                }
                                else{throw new ErroneousCommandException();}
                            }

                            //Switch command
                            else if (words[0].equals("Switch") && words.length == 3) {
                                String name = words[1];
                                String status = words[2];

                                if (smartDeviceNames.contains(name)) {
                                    if (isValidStatus(status)) {
                                        SmartDevice device = getDevice(name, smartDevices);
                                        String deviceStatus = device.getStatus();

                                        if (deviceStatus.equals("On") && status.equals("On")) {
                                            throw new AlreadySwitchedOnException();
                                        } else if (deviceStatus.equals("Off") && status.equals("Off")) {
                                            throw new AlreadySwitchedOffException();
                                        } else {
                                            device.setStatus(status);
                                        }

                                    } else {
                                        throw new ErroneousCommandException();
                                    }
                                } else {
                                    throw new NoSuchDeviceException();
                                }
                            }

                            //SetTime command
                            else if (words[0].equals("SetTime") && words.length == 2) {
                                if (isValidDate(words[1])) {
                                    LocalDateTime newTime = LocalDateTime.parse(words[1], formatter);

                                    if (currentTime.isBefore(newTime)) {
                                        //Executes pending switch events before jumping to the new time
                                        while (!switchTimes.isEmpty() && !switchTimes.get(0).isAfter(newTime)) {

                                            currentTime = switchTimes.get(0);

                                            for (SmartDevice device : smartDevices) {
                                                if (device.getSwitchTime() != null && device.getSwitchTime().equals(currentTime)) {
                                                    device.switchDevice();
                                                }
                                            }
                                            switchTimes.remove(0);
                                            smartDevices = sortDevices(smartDevices, switchTimes);
                                        }
                                        currentTime = newTime;

                                    } else if (currentTime.isAfter(newTime)) {
                                        throw new NegativeTimeException();
                                    } else if (currentTime.isEqual(newTime)) {
                                        throw new NoTimeChangeException();
                                    }
                                } else {
                                    throw new WrongTimeFormatException();
                                }
                            }

                            //SkipMinutes command
                            else if (words[0].equals("SkipMinutes") && words.length == 2) {
                                int minute = Integer.parseInt(words[1]);

                                //Minute must be positive integer
                                if (minute < 0) {
                                    throw new NegativeTimeException();
                                } else if (minute == 0) {
                                    throw new ZeroTimeException();

                                } else {
                                    currentTime = currentTime.plusMinutes(minute);
                                }
                            }

                            //SetSwitchTime command
                            else if (words[0].equals("SetSwitchTime") && words.length == 3) {
                                String name = words[1];

                                if (smartDeviceNames.contains(name)) {
                                    if (isValidDate(words[2])) {
                                        LocalDateTime switchTime = LocalDateTime.parse(words[2], formatter);
                                        if (switchTime.isBefore(currentTime)) {
                                            throw new PastSwitchTimeException();
                                        }
                                        else if (switchTime.equals(currentTime)) {
                                            //If the switch time is exactly now, executes it immediately
                                            SmartDevice device = getDevice(name, smartDevices);

                                            smartDevices.remove(device);
                                            smartDevices.add(0, device);

                                            device.switchDevice();}
                                        else{
                                        SmartDevice device = getDevice(name, smartDevices);
                                        device.setSwitchTime(switchTime);
                                        if (!switchTimes.contains(switchTime)){
                                        switchTimes.add(switchTime);}
                                        switchTimes.sort(null);}

                                    } else {
                                        throw new WrongTimeFormatException();
                                    }
                                } else {
                                    throw new NoSuchDeviceException();
                                }
                            }

                            //Remove command
                            else if (words[0].equals("Remove") && words.length == 2) {
                                String name = words[1];

                                if (smartDeviceNames.contains(name)) {
                                    SmartDevice device = getDevice(name, smartDevices);
                                    device.setStatus("Off");

                                    writer.print("SUCCESS: Information about removed smart device is as follows:\n");
                                    writer.print(giveInfo(device));

                                    smartDevices.remove(device);
                                    smartDeviceNames.remove(name);
                                } else {
                                    throw new NoSuchDeviceException();
                                }
                            }

                            //ChangeName command
                            else if (words[0].equals("ChangeName") && words.length == 3) {
                                if (words[1].equals(words[2])) {
                                    throw new NoNameChangeException();
                                } else if (smartDeviceNames.contains(words[2])) {
                                    throw new SameNameException();
                                }

                                String oldName = words[1];
                                String newName = words[2];
                                SmartDevice device = getDevice(oldName, smartDevices);

                                device.setName(newName);
                                smartDeviceNames.remove(oldName);
                                smartDeviceNames.add(newName);
                            }

                            //ZReport command
                            else if (words[0].equals("ZReport") && words.length == 1) {
                                String output = "";
                                output += "Time is:\t" + currentTime.format(formatter) + "\n";
                                smartDevices = sortDevices(smartDevices, switchTimes);

                                for (SmartDevice device : smartDevices) {
                                    output += giveInfo(device);
                                }

                                writer.print(output);
                            }

                            //PlugIn command
                            else if (words[0].equals("PlugIn") && words.length == 3) {
                                String name = words[1];

                                if (smartDeviceNames.contains(name)) {
                                    SmartDevice device = getDevice(name, smartDevices);
                                    if (!device.getType().equals("SmartPlug")) {
                                        throw new NotSmartPlugException();
                                    }
                                    SmartPlug plug = (SmartPlug) (device);
                                    if (plug.getPlugStatus().equals("In")) {
                                        throw new AlreadyPluggedInException();
                                    }

                                    double ampere = Double.parseDouble(words[2]);

                                    if (ampere > 0) {
                                        plug.setAmpere(ampere);

                                    } else {
                                        throw new InvalidAmpereException();
                                    }

                                } else {
                                    throw new NoSuchDeviceException();
                                }
                            }

                            //PlugOut command
                            else if (words[0].equals("PlugOut") && words.length == 2) {
                                String name = words[1];

                                if (smartDeviceNames.contains(name)) {
                                    SmartDevice device = getDevice(name, smartDevices);
                                    if (!device.getType().equals("SmartPlug")) {
                                        throw new NotSmartPlugException();
                                    }
                                    SmartPlug plug = (SmartPlug) (device);
                                    if (plug.getPlugStatus().equals("Out")) {
                                        throw new NoItemToPlugOutException();
                                    }
                                    plug.setAmpere(0);

                                } else {
                                    throw new NoSuchDeviceException();
                                }
                            }

                            //SetKelvin command
                            else if (words[0].equals("SetKelvin") && words.length == 3) {
                                String name = words[1];

                                if (!smartDeviceNames.contains(name)) {
                                    throw new NoSuchDeviceException();
                                }
                                SmartDevice device = getDevice(name, smartDevices);
                                if (!(device.getType().equals("SmartLamp") || device.getType().equals("SmartColorLamp"))) {
                                    throw new NotSmartLampException();
                                }
                                int kelvin = Integer.parseInt(words[2]);

                                if(isValidKelvin(kelvin)){
                                    //Smart Lamp
                                    if (device.getType().equals("SmartLamp")) {
                                        SmartLamp lamp = (SmartLamp) (device);
                                        lamp.setKelvin(kelvin);
                                    }
                                    //Smart Color Lamp
                                    else if (device.getType().equals("SmartColorLamp")) {
                                        SmartColorLamp colorLamp = (SmartColorLamp) (device);
                                        colorLamp.setKelvin(kelvin);
                                    }
                                }else{throw new InvalidKelvinException();}

                            }
                            //SetBrightness command
                            else if (words[0].equals("SetBrightness") && words.length == 3) {
                                String name = words[1];

                                if (!smartDeviceNames.contains(name)) {
                                    throw new NoSuchDeviceException();
                                }
                                SmartDevice device = getDevice(name, smartDevices);
                                if (!(device.getType().equals("SmartLamp") || device.getType().equals("SmartColorLamp"))) {
                                    throw new NotSmartLampException();
                                }
                                int brightness = Integer.parseInt(words[2]);

                                if (isValidBrightness(brightness)) {
                                    //Smart Lamp
                                    if (device.getType().equals("SmartLamp")) {
                                        SmartLamp lamp = (SmartLamp) (device);
                                        lamp.setBrightness(brightness);
                                    }
                                    //Smart Color Lamp
                                    else if (device.getType().equals("SmartColorLamp")) {
                                        SmartColorLamp colorLamp = (SmartColorLamp) (device);
                                        colorLamp.setBrightness(brightness);
                                    }
                                } else {
                                    throw new InvalidBrightnessException();
                                }
                            }

                            //SetColorCode command
                            else if (words[0].equals("SetColorCode") && words.length == 3) {
                                String name = words[1];

                                if (!smartDeviceNames.contains(name)) {
                                    throw new NoSuchDeviceException();
                                }
                                SmartDevice device = getDevice(name, smartDevices);
                                if (!(device.getType().equals("SmartColorLamp"))) {
                                    throw new NotSmartColorLampException();
                                }
                                if(isValidColorCode1(words[2])){
                                    if(isValidColorCode2(words[2])){
                                        String colorCode = words[2];
                                        SmartColorLamp colorLamp = (SmartColorLamp) (device);
                                        colorLamp.setColorCode(colorCode);

                                    }else{throw new InvalidColorCodeException();}
                                }else{throw new ErroneousCommandException();}
                            }

                            //SetWhite command
                            else if (words[0].equals("SetWhite") && words.length == 4) {
                                String name = words[1];

                                if (!smartDeviceNames.contains(name)) {
                                    throw new NoSuchDeviceException();
                                }
                                SmartDevice device = getDevice(name, smartDevices);
                                if (!(device.getType().equals("SmartLamp") || device.getType().equals("SmartColorLamp"))) {
                                    throw new NotSmartLampException();
                                }

                                int kelvin = Integer.parseInt(words[2]);
                                int brightness = Integer.parseInt(words[3]);

                                if(!isValidKelvin(kelvin)){throw new InvalidKelvinException();}
                                else if(!isValidBrightness(brightness)){throw new InvalidBrightnessException();}

                                //Smart Lamp
                                if (device.getType().equals("SmartLamp")) {
                                    SmartLamp lamp = (SmartLamp) (device);
                                    lamp.setKelvin(kelvin);
                                    lamp.setBrightness(brightness);
                                }
                                //Smart Color Lamp
                                else if (device.getType().equals("SmartColorLamp")) {
                                    SmartColorLamp colorLamp = (SmartColorLamp) (device);
                                    colorLamp.setKelvin(kelvin);
                                    colorLamp.setBrightness(brightness);
                                }
                            }

                            //SetColor command
                            else if (words[0].equals("SetColor") && words.length == 4) {
                                String name = words[1];

                                if (!smartDeviceNames.contains(name)) {
                                    throw new NoSuchDeviceException();
                                }

                                SmartDevice device = getDevice(name, smartDevices);
                                if (!(device.getType().equals("SmartColorLamp"))) {
                                    throw new NotSmartColorLampException();
                                }
                                String colorCode = words[2];
                                int brightness = Integer.parseInt(words[3]);

                                if(!isValidColorCode1(colorCode)){throw new ErroneousCommandException();}
                                else if(!isValidColorCode2(colorCode)){throw new InvalidColorCodeException();}
                                else if (!isValidBrightness(brightness)){throw new InvalidBrightnessException();}

                                SmartColorLamp colorLamp = (SmartColorLamp) (device);
                                colorLamp.setColorCode(colorCode);
                                colorLamp.setBrightness(brightness);
                            }
                            else{
                                throw new ErroneousCommandException();
                            }
                        }catch (NumberFormatException e){ throw new ErroneousCommandException();}
                    }

                }catch (FirstCommandException | WrongInitialDateException e){
                    //If these exceptions are detected, program terminates
                    writer.println(e.getMessage());
                    scan.close();
                    writer.close();
                    return;
                }
                catch (CommandExceptions e){
                    writer.println(e.getMessage());
                }
            }
        //Last command must be ZReport
        if (!commands.get(commands.size()-1).equals("ZReport")){
            String output = "ZReport:\n";
            output += "Time is:\t" + currentTime.format(formatter) + "\n";
            smartDevices = sortDevices(smartDevices, switchTimes);

            for (SmartDevice device : smartDevices) {
                output += giveInfo(device);
            }
            writer.print(output);
        }

        scan.close();
        writer.close();

        }catch (FileNotFoundException f){System.out.println(f.getMessage());}
    }


    public static void main(String[] args){
        Locale.setDefault(Locale.US);

        String inputFileName = args[0];
        String outputFileName = args[1];

        smartHomeManager(inputFileName, outputFileName);
    }

}