package org.mql.java.models;

public class Relation {
    private String source;       // Classe source
    private String destination;  // Classe destination
    private String type;         // Type de relation : inheritance, aggregation, etc.

    public Relation(String source, String destination, String type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s : %s", source, destination, type);
    }
}
