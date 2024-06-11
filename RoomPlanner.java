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
    static GeschäftsRaum random(Random rng) {
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

    static WirtschaftsRaum random(Random rng) {
        int i = rng.nextInt(0, 2);
        return switch (i) {
            default -> throw new IllegalStateException("Out of Range 0..2: " + i);
            case 0 -> Sanitäreinrichtungen.random(rng);
            case 1 -> Teeküche.random(rng);
        };
    }
}

interface Werkstatt extends GewerbeRaum {
    static Werkstatt random(Random rng) {
        int i = rng.nextInt(0, 2);
        return switch (i) {
            default -> throw new IllegalStateException("Out of Range 0..2: " + i);
            case 0 -> MetallWerkstatt.random(rng);
            case 1 -> HolzWerkstatt.random(rng);
        };
    }
}

class Büro implements GeschäftsRaum {
    static Büro random(Random rng) {
        Büro room = new Büro();
        room.inventory = InventoryItem.randomArray(rng, GeschäftsRaumItem.values());
        return room;
    }

    public Stream<String> getInventory() {
        return Stream.of(this.inventory).map(Object::toString);
    }

    private InventoryItem<GeschäftsRaumItem>[] inventory;
}

class AufenthaltsRaum implements GeschäftsRaum {
    static AufenthaltsRaum random(Random rng) {
        AufenthaltsRaum room = new AufenthaltsRaum();
        room.inventory = InventoryItem.randomArray(rng, GeschäftsRaumItem.values());
        return room;
    }

    public Stream<String> getInventory() {
        return Stream.of(this.inventory).map(Object::toString);
    }

    private InventoryItem<GeschäftsRaumItem>[] inventory;
}

class Sanitäreinrichtungen implements WirtschaftsRaum {
    static Sanitäreinrichtungen random(Random rng) {
        Sanitäreinrichtungen room = new Sanitäreinrichtungen();
        room.inventory = InventoryItem.randomArray(rng, WirtschaftsRaumItem.values());
        return room;
    }

    public Stream<String> getInventory() {
        return Stream.of(this.inventory).map(Object::toString);
    }

    private InventoryItem<WirtschaftsRaumItem>[] inventory;
}

class Teeküche implements WirtschaftsRaum {
    static Teeküche random(Random rng) {
        Teeküche room = new Teeküche();
        room.inventory = InventoryItem.randomArray(rng, WirtschaftsRaumItem.values());
        return room;
    }

    public Stream<String> getInventory() {
        return Stream.of(this.inventory).map(Object::toString);
    }

    private InventoryItem<WirtschaftsRaumItem>[] inventory;
}

enum MetallWerkstattItem {
    WeldingMachine,
    PolishingMachine,
}

class MetallWerkstatt implements Werkstatt {
    static MetallWerkstatt random(Random rng) {
        MetallWerkstatt room = new MetallWerkstatt();
        room.inventory = InventoryItem.randomArray(rng, MetallWerkstattItem.values());
        return room;
    }

    public Stream<String> getInventory() {
        return Stream.of(this.inventory).map(Object::toString);
    }

    private InventoryItem<MetallWerkstattItem>[] inventory;
}

enum HolzWerkstattItem {
    CircularSaw,
    Router,
}

class HolzWerkstatt implements Werkstatt {
    static HolzWerkstatt random(Random rng) {
        HolzWerkstatt room = new HolzWerkstatt();
        room.inventory = InventoryItem.randomArray(rng, HolzWerkstattItem.values());
        return room;
    }

    public Stream<String> getInventory() {
        return Stream.of(this.inventory).map(Object::toString);
    }

    private InventoryItem<HolzWerkstattItem>[] inventory;
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

    static <Kind> InventoryItem<Kind>[] randomArray(Random rng, Kind[] itemKinds) {
        return rng
                .ints(0, itemKinds.length)
                .limit(rng.nextInt(10))
                .mapToObj(i -> new InventoryItem<Kind>(itemKinds[i]))
                .toArray(i -> (InventoryItem<Kind>[]) new InventoryItem[i]);
    }

    @Override
    public String toString() {
        return "00-" + serialNumber + "-S04: " + kind;
    }

    // Not a great way of doing this, but this is a small piece of software and
    // we are not doing any multithreading/async stuff
    private static int nextSerialNumber = 12000;
}
