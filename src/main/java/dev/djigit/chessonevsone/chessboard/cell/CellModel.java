package dev.djigit.chessonevsone.chessboard.cell;

public class CellModel {
    private Coords coords;
    private State state;

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum Coords {
        A1('a', (short)1),A2('a', (short)2),A3('a', (short)3),A4('a',(short)4),
        A5('a', (short)5),A6('a', (short)6),A7('a', (short)7),A8('a',(short)8),
        B1('b', (short)1),B2('b', (short)2),B3('b', (short)3),B4('b',(short)4),
        B5('b', (short)5),B6('b', (short)6),B7('b', (short)7),B8('b',(short)8),
        C1('c', (short)1),C2('c', (short)2),C3('c', (short)3),C4('c',(short)4),
        C5('c', (short)5),C6('c', (short)6),C7('c', (short)7),C8('c',(short)8),
        D1('d', (short)1),D2('d', (short)2),D3('d', (short)3),D4('d',(short)4),
        D5('d', (short)5),D6('d', (short)6),D7('d', (short)7),D8('d',(short)8),
        E1('e', (short)1),E2('e', (short)2),E3('e', (short)3),E4('e',(short)4),
        E5('e', (short)5),E6('e', (short)6),E7('e', (short)7),E8('e',(short)8),
        F1('f', (short)1),F2('f', (short)2),F3('f', (short)3),F4('f',(short)4),
        F5('f', (short)5),F6('f', (short)6),F7('f', (short)7),F8('f',(short)8),
        G1('g', (short)1),G2('g', (short)2),G3('g', (short)3),G4('g',(short)4),
        G5('g', (short)5),G6('g', (short)6),G7('g', (short)7),G8('g',(short)8),
        H1('h', (short)1),H2('h', (short)2),H3('h', (short)3),H4('h',(short)4),
        H5('h', (short)5),H6('h', (short)6),H7('h', (short)7),H8('h',(short)8);


        char x;
        short y;
        double xPositionRelative;
        double yPositionRelative;

        Coords(char x, short y) {
            this.x = x;
            this.y = y;
        }

        Coords(char x, short y, double xPositionRelative, double yPositionRelative) {
            this.x = x;
            this.y = y;
            this.xPositionRelative = xPositionRelative;
            this.yPositionRelative = yPositionRelative;
        }

        public static Coords getCordsByValue(char x, short y) {
            for (Coords cords : values()) {
                if(cords.x == x && cords.y == y)
                    return cords;
            }
            throw new IllegalStateException("Can't find piece coordinates by coordinates value");
        }

        public Coords getByCoords(short x, short y) {
            return getCordsByValue((char)(this.x + x), (short)(this.y + y));
        }

        public char getX() {
            return x;
        }

        public short getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Coords{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public enum State {
        RELEASED, ACQUIRED
    }
}