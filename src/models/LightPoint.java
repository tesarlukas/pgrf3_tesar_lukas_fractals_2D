package models;

import lwjglutils.OGLBuffers;

public class LightPoint {
    private OGLBuffers buffers;
    private float[] position;

    public LightPoint() {
        float[] vertices = {
            0.00f, 0.00f ,0.f,  //0
            .05f, .00f, 0.f,   //1
            0.00f, .05f, 0.f,  //2
            0.05f, 0.05f,0.f,  //3
        };
        int[] indices = {
                0, 1, 2,
                2, 1, 3
        };

        OGLBuffers.Attrib[] attribs = new OGLBuffers.Attrib[] {
                new OGLBuffers.Attrib("inPosition", 3),
        };

        position = new float[] {
                0.5f, 0.5f, 0.5f
        };

        buffers = new OGLBuffers(vertices, attribs, indices);
    }
    public OGLBuffers getBuffers() {
        return buffers;
    }
    public float[] getPosition() {
       	return position;
    }
    public void moveUp(float step) {
        this.position[2] += step;
    }
    public void moveDown(float step) {
        this.position[2] -= step;
    }
    public void moveLeft(float step) {
        this.position[0] -= step;
    }
    public void moveRight(float step) {
        this.position[0] += step;
    }
    public void moveForward(float step) {
        this.position[1] += step;
    }
    public void moveBackward(float step) {
        this.position[1] -= step;
    }
    public void setPosition(float[] position) {
        this.position = position;
    }
}
