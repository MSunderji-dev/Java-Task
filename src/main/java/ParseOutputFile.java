import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ParseOutputFile {

    public static List<Vehicle> readVehiclefromFile(String filePath){
        List<Vehicle> vehicles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 4) {
                    String registrationNumber = tokens[0].trim().replaceAll("\\s+", "");
                    String make = tokens[1].trim();
                    String model = tokens[2].trim();
                    String year = tokens[3].trim();

                    vehicles.add(new Vehicle(registrationNumber, make, model, year));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return vehicles;
    }
}
