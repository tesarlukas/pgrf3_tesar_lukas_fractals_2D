package models;

import lwjglutils.OGLBuffers;

public class Axes {
    private OGLBuffers buffers;

    public Axes() {
        float[] vertices = {
                0.f, 0.f, 0.f,  1.f, 0.f, 0.f,  // origin red
                1.f, 0.f, 0.f,  1.f, 0.f, 0.f, // 1

                0.f, 0.f, 0.f,  0.f, 1.f, 0.f,  // origin green
                0.f, 1.f, 0.f,  0.f, 1.f, 0.f, // 2

                0.f, 0.f, 0.f,  0.f, 0.f, 1.f,  // origin blue
                0.f, 0.f, 1.f,  0.f, 0.f, 1.f, // 3
        };
        int[] indices = {
                0, 1, 2, 3, 4, 5, 6
        };

        OGLBuffers.Attrib[] attribs = new OGLBuffers.Attrib[] {
                new OGLBuffers.Attrib("inPosition", 3),
                new OGLBuffers.Attrib("inColor", 3),
        };

        buffers = new OGLBuffers(vertices, attribs, indices);
    }

    public OGLBuffers getBuffers() {
        return buffers;
    }
}
