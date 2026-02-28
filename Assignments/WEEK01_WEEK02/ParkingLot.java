import java.util.*;

public class ParkingLot {

    private static final int SIZE = 500;

    private static class ParkingSpot {

        String licensePlate;
        long entryTime;
        Status status;

        public ParkingSpot() {
            this.status = Status.EMPTY;
        }
    }

    private enum Status {
        EMPTY,
        OCCUPIED,
        DELETED
    }

    private final ParkingSpot[] table;
    private int totalProbes = 0;
    private int totalParked = 0;
    private int peakHour = 0;
    private int[] hourlyCount = new int[24];

    public ParkingLot() {
        table = new ParkingSpot[SIZE];
        for (int i = 0; i < SIZE; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // -------------------------------
    // Hash Function
    // -------------------------------
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % SIZE;
    }

    // -------------------------------
    // Park Vehicle
    // -------------------------------
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % SIZE;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = Status.OCCUPIED;

        totalProbes += probes;
        totalParked++;

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        hourlyCount[hour]++;
        peakHour = findPeakHour();

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }

    // -------------------------------
    // Exit Vehicle
    // -------------------------------
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].status != Status.EMPTY) {

            if (table[index].status == Status.OCCUPIED &&
                    table[index].licensePlate.equals(plate)) {

                long exitTime = System.currentTimeMillis();
                long durationMillis =
                        exitTime - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);
                double fee = Math.ceil(hours) * 5.0;

                table[index].status = Status.DELETED;

                System.out.println("exitVehicle(\"" + plate + "\") → Spot #" +
                        index + " freed, Duration: " +
                        String.format("%.2f", hours) +
                        "h, Fee: $" + fee);

                return;
            }

            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found.");
    }

    // -------------------------------
    // Statistics
    // -------------------------------
    public void getStatistics() {

        int occupied = 0;

        for (ParkingSpot spot : table) {
            if (spot.status == Status.OCCUPIED) {
                occupied++;
            }
        }

        double occupancy =
                (double) occupied / SIZE * 100;

        double avgProbes =
                totalParked == 0 ? 0 :
                        (double) totalProbes / totalParked;

        System.out.println("getStatistics() → Occupancy: " +
                String.format("%.1f", occupancy) +
                "%, Avg Probes: " +
                String.format("%.2f", avgProbes) +
                ", Peak Hour: " +
                peakHour + ":00-" + (peakHour + 1) + ":00");
    }

    private int findPeakHour() {

        int max = 0;
        int hour = 0;

        for (int i = 0; i < 24; i++) {
            if (hourlyCount[i] > max) {
                max = hourlyCount[i];
                hour = i;
            }
        }

        return hour;
    }

    // -------------------------------
    // Main
    // -------------------------------
    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}