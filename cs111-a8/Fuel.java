public class Fuel {

    // Values used for fuelType
    public static final int    GAS      = 1;  // denotes a car that uses gas
    public static final int    ELECTRIC = 2;  // denotes a car that uses electricity

    // Values used to compute CO2 emission
    public static final double CO2EMITTED_GASCOMBUSTION = 8.887; // 8.887 kg of CO2 released with the combustion of each gallon of gasoline
    public static final double CO2EMITTED_GENERATEmWh = 998.4;   // 998.4 lbs of CO2 are emitted to generate 1 mWh on average

    // Instance variables
    private int    type;         // GAS or ELECTRIC
    private double usage;        // Value is in miles per gallon if fuelType is GAS, or
                                 // in miles per kWh/charge if fuelType is ELECTRIC
    private double kWhPerCharge; // kWh per charge

    // Constructor for gas-powered cars
    public Fuel (double usage) {
        this.type = GAS;
        this.usage = usage;
    }

    // Constructor for electric cars
    public Fuel (double usage, double kWhPerCharge) {
        this.type = ELECTRIC;
        this.usage = usage;
        this.kWhPerCharge = kWhPerCharge;
    }

    // Accessor methods
    public int getType () {
        return type;
    }
    public double getUsage () {
        return usage;
    }
    public double getKWhPerCharge () {
        return kWhPerCharge;
    }

    // String representation of Fuel
    public String toString () {
        String str = "\nFuel";
        String fuel = (type == GAS) ? "Gas" : "Electric";
        str += "\n\tType: " + fuel;
        str += "\n\tUsage: " + usage;
        if (type == ELECTRIC) {
            str += "\n\tkWhPerCharge: " + kWhPerCharge;
        }
        return str;
    }
}