public class CommandExceptions extends Exception {
    //Base exception class
    public CommandExceptions(String message){
        super(message);
    }
}

    class ErroneousCommandException extends CommandExceptions{
        public ErroneousCommandException(){
            super("ERROR: Erroneous command!");
        }
    }

    class FirstCommandException extends CommandExceptions{
        public FirstCommandException(){
            super("ERROR: First command must be set initial time! Program is going to terminate!");
        }
    }
    class WrongInitialDateException extends CommandExceptions{
        public WrongInitialDateException(){
            super("ERROR: Format of the initial date is wrong! Program is going to terminate!");
        }
    }

    class InvalidMegabyteException extends CommandExceptions{
        public InvalidMegabyteException(){
            super("ERROR: Megabyte value must be a positive number!");
        }
    }
    class InvalidAmpereException extends CommandExceptions{
        public InvalidAmpereException(){
            super("ERROR: Ampere value must be a positive number!");
        }
    }

    class InvalidKelvinException extends CommandExceptions{
        public InvalidKelvinException(){
            super("ERROR: Kelvin value must be in range of 2000K-6500K!");
        }
    }
    class InvalidBrightnessException extends CommandExceptions{
        public InvalidBrightnessException(){
            super("ERROR: Brightness must be in range of 0%-100%!");
        }
    }
    class InvalidColorCodeException extends CommandExceptions{
        public InvalidColorCodeException(){
            super("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
        }
    }

    class NegativeTimeException extends CommandExceptions{
        public NegativeTimeException(){
            super("ERROR: Time cannot be reversed!");
        }
    }
    class PastSwitchTimeException extends CommandExceptions{
        public PastSwitchTimeException(){
            super("ERROR: Switch time cannot be in the past!");
        }
    }
    class ZeroTimeException extends CommandExceptions{
        public ZeroTimeException(){
            super("ERROR: There is nothing to skip!");
        }
    }
    class NoTimeChangeException extends CommandExceptions{
        public NoTimeChangeException(){
            super("ERROR: There is nothing to change!");
        }
    }
    class WrongTimeFormatException extends CommandExceptions{
        public WrongTimeFormatException(){
            super("ERROR: Time format is not correct!");
        }
    }

    class NoSuchDeviceException extends CommandExceptions{
        public NoSuchDeviceException(){
            super("ERROR: There is not such a device!");
        }
    }
    class AlreadySwitchedOnException extends CommandExceptions{
        public AlreadySwitchedOnException(){
            super("ERROR: This device is already switched on!");
        }
    }
    class AlreadySwitchedOffException extends CommandExceptions{
        public AlreadySwitchedOffException(){
            super("ERROR: This device is already switched off!");
        }
    }
    class NothingToSwitchException extends CommandExceptions{
        public NothingToSwitchException(){
            super("ERROR: There is nothing to switch!");
        }
    }

    class SameNameException extends CommandExceptions{
        public SameNameException(){
            super("ERROR: There is already a smart device with same name!");
        }
    }
    class NoNameChangeException extends CommandExceptions{
        public NoNameChangeException(){
            super("ERROR: Both of the names are the same, nothing changed!");
        }
    }

    class NotSmartLampException extends CommandExceptions{
        public NotSmartLampException(){
            super("ERROR: This device is not a smart lamp!");
        }
    }
    class NotSmartColorLampException extends CommandExceptions{
        public NotSmartColorLampException(){
            super("ERROR: This device is not a smart color lamp!");
        }
    }
    class NotSmartPlugException extends CommandExceptions{
        public NotSmartPlugException(){
            super("ERROR: This device is not a smart plug!");
        }
    }
    class NotSmartCameraException extends CommandExceptions{
        public NotSmartCameraException(){
            super("ERROR: This device is not a smart camera!");
        }
    }

    class AlreadyPluggedInException extends CommandExceptions{
        public AlreadyPluggedInException(){
            super("ERROR: There is already an item plugged in to that plug!");
        }
    }
    class NoItemToPlugOutException extends CommandExceptions{
        public NoItemToPlugOutException(){
            super("ERROR: This plug has no item to plug out from that plug!");
        }
    }
