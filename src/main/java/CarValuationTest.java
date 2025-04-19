
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Year;
import java.util.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CarValuationTest {
    private WebDriver driver;
    private final String INPUT_FILE = "src/test/resources/car_input - V6.txt";
    private final String OUTPUT_FILE = "src/test/resources/car_output - V6.txt";
    private Map<String, String> expectedResults;
    private Map<String, Vehicle> outputVehicleMap;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        expectedResults = loadExpectedOutput(OUTPUT_FILE);
    }

    @Test
    public void testCar() throws IOException {
        List<Vehicle> outputFileVehicles = ParseOutputFile.readVehiclefromFile(OUTPUT_FILE);
        outputVehicleMap = new HashMap<>();
        for (Vehicle vehicle : outputFileVehicles) {

            outputVehicleMap.put(vehicle.getRegistrationNumber(), vehicle);
        }
        List<Vehicle> webScrapedVehicles = testCarValuations();


        for (Vehicle vehicle : webScrapedVehicles) {

            String reg= vehicle.getRegistrationNumber();
            Assert.assertTrue(outputVehicleMap.containsKey(reg), "Vehicle with registration" + reg + " not found in the file.");

            Vehicle filevehicle = outputVehicleMap.get(reg);

            Assert.assertEquals(vehicle.getMake().toLowerCase(), filevehicle.getMake().toLowerCase(), "Make mismatch for reg " + reg);

            Assert.assertEquals(vehicle.getModel().toLowerCase(), filevehicle.getModel().toLowerCase(), "Model mismatch for reg " + reg);

            Assert.assertEquals(vehicle.getYear(), filevehicle.getYear(), "Year mismatch for reg " + reg);

        }
    }

    public List<Vehicle> testCarValuations() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
        List<String> regNumbers = extractRegistrationNumbers(lines);
        List<Vehicle> listOfVehicles = new ArrayList<>();

        for (String reg : regNumbers) {
            driver.get("https://car-checking.com");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.id("subForm1")));
            input.clear();
            input.sendKeys(reg);

            WebElement submit = driver.findElement(By.className("check-now-button"));
            submit.click();

            try {
                WebDriverWait waitAgain = new WebDriverWait(driver, Duration.ofSeconds(10));
                waitAgain.until(ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert.alert-danger")),
                        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.table-responsive tbody tr"))
                ));

                List<WebElement> errorMessages = driver.findElements(By.cssSelector(".alert.alert-danger"));
                if (!errorMessages.isEmpty()) {
                    Assert.fail("Details not found for registration: " + reg);
                }

                List<WebElement> rows =driver.findElements(By.cssSelector("table.table-responsive tbody tr"));

                Map<String, String> tableData = new HashMap<>();

                String make=null;
                String model=null;
                String year=null;

                for (WebElement row : rows) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (cells.size() == 2) {

                            String value =cells.get(1).getText();
                            String key = cells.get(0).getText();
                            tableData.put(key, value);

                            switch (key) {
                                case "Make":
                                    make = value;
                                    break;
                                case "Model":
                                    model = value;
                                    break;
                                case "Year of manufacture":
                                    year = value;
                                    break;
                            }
                    }

                }
                if (make != null && model != null && year != null) {
                    Vehicle vehicle = new Vehicle(reg,make, model, year);
                    listOfVehicles.add(vehicle);
                }
            } catch (Exception e) {
                Assert.fail("Details not found for registration: " + reg);
            }
        }
        return listOfVehicles;
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private List<String> extractRegistrationNumbers(List<String> lines) {
        Pattern pattern = Pattern.compile("[A-Z]{2}[0-9]{2}\s?[A-Z]{3}");
        List<String> regs = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line.toUpperCase());
            while (matcher.find()) {
                regs.add(matcher.group().replace(" ", ""));
            }
        }
        return regs;
    }

    private Map<String, String> loadExpectedOutput(String path) {
        Map<String, String> map = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (String line : lines) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    map.put(parts[0].trim().replace(" ", ""), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read output file: " + path);
        }
        return map;
    }
}
