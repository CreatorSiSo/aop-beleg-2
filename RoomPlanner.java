public class RoomPlanner {
    public static void main(String[] args) {
        GewerbeRaum[] rooms = generateRooms();
        for (GewerbeRaum room : rooms) {
            room.listInventory();
        }
    }

    static GewerbeRaum[] generateRooms() {
        GewerbeRaum[] rooms = new GewerbeRaum[9];
        for (int i = 0; i < rooms.length; i += 1) {
            rooms[i] = GewerbeRaum.random();
        }
        rooms[4] = new Treppenhaus();
        return rooms;
    }
}

interface GewerbeRaum {
    void listInventory();

    static GewerbeRaum random() {
        // TODO
        return new Treppenhaus();
    }
}

interface GeschäftsRaum extends GewerbeRaum {
    // Stühle und Tische für Geschäftsräume.

    static GeschäftsRaum random() {
        // TODO
        return null;
    }
}

interface WirtschaftsRaum extends GewerbeRaum {
    // Waschbecken, Handtuchspender für Wirtschaftsräume

    static WirtschaftsRaum random() {
        // TODO
        return null;
    }
}

interface Werkstatt extends GewerbeRaum {
    static Werkstatt random() {
        // TODO
        return null;
    }
}

class Büro implements GeschäftsRaum {
    @Override
    public void listInventory() {
        // TODO
    }
}

class AufenthaltsRaum implements GeschäftsRaum {
    @Override
    public void listInventory() {
        // TODO
    }
}

class Sanitäreinrichtungen implements WirtschaftsRaum {
    @Override
    public void listInventory() {
        // TODO
    }
}

class Teeküche implements WirtschaftsRaum {
    @Override
    public void listInventory() {
        // TODO
    }
}

class Metallwerkstatt implements Werkstatt {
    // Schweißgeräte und Poliermaschinen für Metallwerkstätten
    @Override
    public void listInventory() {
        // TODO
    }
}

class HolzWerkstatt implements Werkstatt {
    // Kreissägen und Oberfräsen für Holzwerkstätten
    @Override
    public void listInventory() {
        // TODO
    }
}

class Treppenhaus implements GewerbeRaum {
    @Override
    public void listInventory() {
        System.out.println("Treppenhaus:");
        System.out.println("  kein Inventar");
    }
}

interface InventoryItem {

}
