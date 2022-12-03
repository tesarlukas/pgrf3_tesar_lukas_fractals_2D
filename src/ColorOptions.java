public enum ColorOptions {
    DEFAULT(0),
    UV(1),
    POSITION(2),
    DEPTH(3),
    TEXTURE(4),
    NORMAL(5),
    NORMAL_TEXTURE(6),
    LIGHT_DISTANCE(7),
    ;

    private int index;

    ColorOptions(int i) {
        index = i;
    }

    public int getValue() {
        return index;
    }
}
