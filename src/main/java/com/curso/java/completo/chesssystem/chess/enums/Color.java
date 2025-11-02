package com.curso.java.completo.chesssystem.chess.enums;

public enum Color {
    BLACK(0),
    WHITE(1);

    private final int value;

    Color(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Color opposite() {
        return this == BLACK ? WHITE : BLACK;
    }

    public static Color fromInt(int v) {
        for (Color c : values()) {
            if (c.value == v) return c;
        }
        throw new IllegalArgumentException("Invalid Color value: " + v);
    }
}
