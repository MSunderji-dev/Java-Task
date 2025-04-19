# Java-Task


Test Description:
Write a test automation suite which does following.
1. Reads the given input file: car_input.txt
2. Extracts vehicle registration numbers based on pattern(s).
3. Each number extracted from input file is fed to any car valuation website for e.g.
https://car-checking.com
(Perform vehicle details search from car valuation page)
4. Compare the output returned by car valuation website with given car_output.txt
5. Highlight/fail the test if car reg details not found from the comparison site or mismatches from
point #4. Showcase your skills so it’s easier to add more input files in future. Utilise any JVM based
language with browser automation tools. Use
design patterns where appropriate.


SOLUTION:

Selenium based automation test suite written in Java.

The test suite performs the following steps:

1. Reads vehicle registration numbers from `car_input - V6.txt`.
2. Extracts the details such as make, model, and year using browser automation.
3. Compares the web scraped results with expected output from `car_output - V6.txt`.
4. Highlights mismatches or missing vehicle data through assertions.
5. It fails the test if registration details are not found for e.g java.lang.AssertionError: Details not found for registration: BW57BOW


Run the test from CarValuationTest.Java

Code Structure
- CarValuationTest.java is the main test runner and it validates the logic. 
- ParseOutputFile.java reads teh expected vehicle data from the output file
- Vehicle.java is the Java object representing the vehicle
- car_input - V6.txt  is the input file with text containing vehicle registration numbers
- car_output - V6.txt is hte output file containing the expected results for comparison



Note: Please make sure testng and selenium JAR files have been installed
