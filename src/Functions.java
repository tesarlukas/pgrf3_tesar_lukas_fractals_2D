public enum Functions {
    DEFAULT(0),
    PRESENTATION(1),
    INTERES(2),
    INTERES_TIME(3),
    SPHERICAL(4),
    SPHERICAL2(5),
    CYLINDR(6),
    CYLINDR2(7);

    private int index;

    Functions(int i) {
        index = i;
    }

    public int getValue() {
        return index;
    }
}
