import java.util.Random;
import java.util.stream.Stream;

public class RoomPlanner {
    public static void main(String[] args) {
        Random rng = new Random();

        Raum[] rooms = new Raum[9];
        for (int i = 0; i < rooms.length; i += 1) {
            rooms[i] = GewerbeRaum.random(rng);
        }
        rooms[4] = new Treppenhaus();

        for (Raum room : rooms) {
            System.out.println(room.getClass().getName());
            room.getInventory()
                    .map(string -> "  " + string)
                    .forEach(System.out::println);
            System.out.println("");
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

    static Raum random(Random rng) {
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
    static GewerbeRaum<GeschäftsRaumItem> random(Random rng) {
        return createRandom(rng, new Büro(), GeschäftsRaumItem.values());
    }
}

class AufenthaltsRaum extends GewerbeRaum<GeschäftsRaumItem> {
    static GewerbeRaum<GeschäftsRaumItem> random(Random rng) {
        return createRandom(rng, new AufenthaltsRaum(), GeschäftsRaumItem.values());
    }
}

class Sanitäreinrichtung extends GewerbeRaum<WirtschaftsRaumItem> {
    static GewerbeRaum<WirtschaftsRaumItem> random(Random rng) {
        return createRandom(rng, new Sanitäreinrichtung(), WirtschaftsRaumItem.values());
    }
}

class Teeküche extends GewerbeRaum<WirtschaftsRaumItem> {
    static GewerbeRaum<WirtschaftsRaumItem> random(Random rng) {
        return createRandom(rng, new Teeküche(), WirtschaftsRaumItem.values());
    }
}

enum MetallWerkstattItem {
    WeldingMachine,
    PolishingMachine,
}

class MetallWerkstatt extends GewerbeRaum<MetallWerkstattItem> {
    static GewerbeRaum<MetallWerkstattItem> random(Random rng) {
        return createRandom(rng, new MetallWerkstatt(), MetallWerkstattItem.values());
    }
}

enum HolzWerkstattItem {
    CircularSaw,
    Router,
}

class HolzWerkstatt extends GewerbeRaum<HolzWerkstattItem> {
    static GewerbeRaum<HolzWerkstattItem> random(Random rng) {
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
