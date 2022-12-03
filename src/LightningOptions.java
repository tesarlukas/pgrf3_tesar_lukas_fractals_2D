public enum LightningOptions {
    WITHOUT(0),
    AMBIENT(1),
    DIFFUSE(2),
    SPECULAR(3),
    FULL(4),
    ATTENUATION(5),
    ;

    private int index;

    LightningOptions(int i) {
        index = i;
    }

    public int getValue() {
        return index;
    }
}
