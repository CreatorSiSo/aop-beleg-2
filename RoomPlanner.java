import java.util.Random;
import java.util.stream.Stream;

public class RoomPlanner {
    public static void main(String[] args) {
        Random rng = new Random();

        GewerbeRaum[] rooms = new GewerbeRaum[9];
        for (int i = 0; i < rooms.length; i += 1) {
            rooms[i] = GewerbeRaum.random(rng);
        }
        rooms[4] = new Treppenhaus();

        for (GewerbeRaum room : rooms) {
            System.out.println(room.getClass().getName());
            room.getInventory()
                    .map(string -> "  " + string)
                    .forEach(System.out::println);
            System.out.println("");
        }
    }
}

interface GewerbeRaum {
    Stream<String> getInventory();

    static GewerbeRaum random(Random rng) {
        int i = rng.nextInt(0, 3);
        return switch (i) {
            default -> throw new IllegalStateException("Out of Range 0..3: " + i);
            case 0 -> GeschäftsRaum.random(rng);
            case 1 -> WirtschaftsRaum.random(rng);
            case 2 -> Werkstatt.random(rng);
        };
    }
}

enum GeschäftsRaumItem {
    Chair,
    Table,
}

interface GeschäftsRaum extends GewerbeRaum {
    static GewerbeRaum random(Random rng) {
        int i = rng.nextInt(0, 2);
        return switch (i) {
            default -> throw new IllegalStateException("Out of Range 0..2: " + i);
            case 0 -> Büro.random(rng);
            case 1 -> AufenthaltsRaum.random(rng);
        };
    }
}

enum WirtschaftsRaumItem {
    Sink,
    TowelDispenser,
}

interface WirtschaftsRaum extends GewerbeRaum {
    static GewerbeRaum random(Random rng) {
        int i = rng.nextInt(0, 2);
        return switch (i) {
            default -> throw new IllegalStateException("Out of Range 0..2: " + i);
            case 0 -> Sanitäreinrichtung.random(rng);
            case 1 -> Teeküche.random(rng);
        };
    }
}

interface Werkstatt extends GewerbeRaum {
    static GewerbeRaum random(Random rng) {
        int i = rng.nextInt(0, 2);
        return switch (i) {
            default -> throw new IllegalStateException("Out of Range 0..2: " + i);
            case 0 -> MetallWerkstatt.random(rng);
            case 1 -> HolzWerkstatt.random(rng);
        };
    }
}

abstract class Raum<I> implements GewerbeRaum {
    public Stream<String> getInventory() {
        return Stream.of(this.inventory).map(Object::toString);
    }

    protected static <I> Raum<I> createRandom(Random rng, Raum<I> room, I[] values) {
        room.inventory = rng
                .ints(0, values.length)
                .limit(rng.nextInt(10))
                .mapToObj(i -> new InventoryItem<I>(values[i]))
                .toArray(i -> (InventoryItem<I>[]) new InventoryItem[i]);
        return room;
    }

    protected InventoryItem<I>[] inventory;
}

class Büro extends Raum<GeschäftsRaumItem> implements GeschäftsRaum {
    static Raum<GeschäftsRaumItem> random(Random rng) {
        return createRandom(rng, new Büro(), GeschäftsRaumItem.values());
    }
}

class AufenthaltsRaum extends Raum<GeschäftsRaumItem> implements GeschäftsRaum {
    static Raum<GeschäftsRaumItem> random(Random rng) {
        return createRandom(rng, new AufenthaltsRaum(), GeschäftsRaumItem.values());
    }
}

class Sanitäreinrichtung extends Raum<WirtschaftsRaumItem> implements WirtschaftsRaum {
    static Raum<WirtschaftsRaumItem> random(Random rng) {
        return createRandom(rng, new Sanitäreinrichtung(), WirtschaftsRaumItem.values());
    }
}

class Teeküche extends Raum<WirtschaftsRaumItem> implements WirtschaftsRaum {
    static Raum<WirtschaftsRaumItem> random(Random rng) {
        return createRandom(rng, new Teeküche(), WirtschaftsRaumItem.values());
    }
}

enum MetallWerkstattItem {
    WeldingMachine,
    PolishingMachine,
}

class MetallWerkstatt extends Raum<MetallWerkstattItem> implements Werkstatt {
    static Raum<MetallWerkstattItem> random(Random rng) {
        return createRandom(rng, new MetallWerkstatt(), MetallWerkstattItem.values());
    }
}

enum HolzWerkstattItem {
    CircularSaw,
    Router,
}

class HolzWerkstatt extends Raum<HolzWerkstattItem> implements Werkstatt {
    static Raum<HolzWerkstattItem> random(Random rng) {
        return createRandom(rng, new HolzWerkstatt(), HolzWerkstattItem.values());
    }
}

class Treppenhaus implements GewerbeRaum {
    public Stream<String> getInventory() {
        return Stream.empty();
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
