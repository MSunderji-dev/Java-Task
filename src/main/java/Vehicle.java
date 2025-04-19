public class Vehicle {

    String registrationNumber;
    String make;
    String model;
    String year;



    public Vehicle(String registrationNumber, String make, String model, String year)
    {
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.year = year;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }
}
