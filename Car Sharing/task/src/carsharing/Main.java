package carsharing;

public class Main {

    public static void main(String[] args) {
        H2jdbcUtils.createDB();
        Console.printGeneralOptions();
    }
}