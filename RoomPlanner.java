import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RoomPlanner {
    public static void main(String[] args) {
        Random rng = new Random();
        System.out.println("Grundriss:\n\n"
                + "  |---|---|---|\n"
                + "  | 0 | 1 | 2 |\n"
                + "  |---|---|---|\n"
                + "  | 3 | 4 | 5 |\n"
                + "  |---|---|---|\n"
                + "  | 6 | 7 | 8 |\n"
                + "  |---|---|---|\n");

        Raum[] rooms = IntStream.range(0, 9)
                .mapToObj(i -> i == 4 ? new Treppenhaus() : GewerbeRaum.random(rng))
                .toArray(Raum[]::new);

        Scanner input = new Scanner(System.in);
        String currentLine = "";
        while (true) {
            System.out.println("Id of room to enter: ");
            currentLine = input.nextLine();
            int i = Integer.parseInt(currentLine.trim());
            if (i < 0 || i >= rooms.length) {
                System.out.println("\nRoom " + i + " does not exist.\n");
                continue;
            }
            System.out.println("\n" + rooms[i]);
        }
    }
}

interface Raum {
    Stream<String> getInventory();
}

class Treppenhaus implements Raum {
    public Stream<String> getInventory() {
        return Stream.empty();
    }

    @Override
    public String toString() {
        return getClass().getName() + "\n";
    }
}

enum GeschäftsRaumItem {
    Chair,
    Table,
}

enum WirtschaftsRaumItem {
    Sink,
    TowelDispenser,
}

abstract class GewerbeRaum<I> implements Raum {
    public Stream<String> getInventory() {
        return Stream.of(this.inventory).map(Object::toString);
    }

    public static Raum random(Random rng) {
        int i = rng.nextInt(0, 6);
        return switch (i) {
            default -> throw new IllegalStateException("Out of Range 0..6: " + i);
            case 0 -> Büro.random(rng);
            case 1 -> AufenthaltsRaum.random(rng);
            case 2 -> Sanitäreinrichtung.random(rng);
            case 3 -> Teeküche.random(rng);
            case 4 -> MetallWerkstatt.random(rng);
            case 5 -> HolzWerkstatt.random(rng);
        };
    }

    @Override
    public String toString() {
        String result = getClass().getName() + "\n";
        result += getInventory()
                .map(string -> "  " + string)
                .collect(Collectors.joining("\n"));
        result += "\n";
        return result;
    }

    protected static <I> GewerbeRaum<I> createRandom(Random rng, GewerbeRaum<I> room, I[] values) {
        room.inventory = rng
                .ints(0, values.length)
                .limit(rng.nextInt(10))
                .mapToObj(i -> new InventoryItem<I>(values[i]))
                .toArray(i -> (InventoryItem<I>[]) new InventoryItem[i]);
        return room;
    }

    protected InventoryItem<I>[] inventory;
}

class Büro extends GewerbeRaum<GeschäftsRaumItem> {
    public static GewerbeRaum<GeschäftsRaumItem> random(Random rng) {
        return createRandom(rng, new Büro(), GeschäftsRaumItem.values());
    }
}

class AufenthaltsRaum extends GewerbeRaum<GeschäftsRaumItem> {
    public static GewerbeRaum<GeschäftsRaumItem> random(Random rng) {
        return createRandom(rng, new AufenthaltsRaum(), GeschäftsRaumItem.values());
    }
}

class Sanitäreinrichtung extends GewerbeRaum<WirtschaftsRaumItem> {
    public static GewerbeRaum<WirtschaftsRaumItem> random(Random rng) {
        return createRandom(rng, new Sanitäreinrichtung(), WirtschaftsRaumItem.values());
    }
}

class Teeküche extends GewerbeRaum<WirtschaftsRaumItem> {
    public static GewerbeRaum<WirtschaftsRaumItem> random(Random rng) {
        return createRandom(rng, new Teeküche(), WirtschaftsRaumItem.values());
    }
}

enum MetallWerkstattItem {
    WeldingMachine,
    PolishingMachine,
}

class MetallWerkstatt extends GewerbeRaum<MetallWerkstattItem> {
    public static GewerbeRaum<MetallWerkstattItem> random(Random rng) {
        return createRandom(rng, new MetallWerkstatt(), MetallWerkstattItem.values());
    }
}

enum HolzWerkstattItem {
    CircularSaw,
    Router,
}

class HolzWerkstatt extends GewerbeRaum<HolzWerkstattItem> {
    public static GewerbeRaum<HolzWerkstattItem> random(Random rng) {
        return createRandom(rng, new HolzWerkstatt(), HolzWerkstattItem.values());
    }
}

class InventoryItem<Kind> {
    public int serialNumber;
    public Kind kind;

    InventoryItem(Kind kind) {
        this.kind = kind;
        this.serialNumber = nextSerialNumber;
        nextSerialNumber += 1;
    }

    @Override
    public String toString() {
        return "00-" + serialNumber + "-S04: " + kind;
    }

    // Not a great way of doing this, but this is a small piece of software and
    // we are not doing any multithreading/async stuff
    private static int nextSerialNumber = 12000;
}
