package models;

import lwjglutils.OGLBuffers;

public class Grid {

    /**
     * GL_TRIANGLES
     *
     * @param m vertex count in row
     * @param n vertex count in column
     */
    private OGLBuffers buffers;

    public Grid(final int m, final int n) {
        float[] vertices = new float[2 * m * n];
        int[] indices = new int[3 * 2 * (m - 1) * (n - 1)];

        // vertices <0;1>
        int index = 0;
        for (int i = 0; i < m; i += 1) {
            for (int j = 0; j < n; j += 1) {
                vertices[index++] = j / (float) (n - 1);
                vertices[index++] = i / (float) (m - 1);
            }
        }
        // Indices
        int indicesIndex = 0;
        for (int i = 0; i < m - 1; i++) {
            int offset = (i * m);
            for (int j = 0; j < n - 1; j++) {
                int a = offset + j;
                int b = offset + j + n;
                int c = offset + j + 1;
                int d = offset + j + n + 1;

                // ABC
                indices[indicesIndex++] = a;
                indices[indicesIndex++] = b;
                indices[indicesIndex++] = c;

                // BCD
                indices[indicesIndex++] = b;
                indices[indicesIndex++] = c;
                indices[indicesIndex++] = d;
            }
        }


        OGLBuffers.Attrib[] attribs = new OGLBuffers.Attrib[] {
                new OGLBuffers.Attrib("inPosition", 2),
        };

        buffers = new OGLBuffers(vertices, attribs, indices);
    }

    public Grid(final int m, final int n, String type) {
        float[] vertices = new float[2 * m * n];
        int[] indices = new int[3 * 2 * (m - 1) * (n - 1)];

        // vertices <0;1>
        int index = 0;
        for (int i = 0; i < m; i += 1) {
            for (int j = 0; j < n; j += 1) {
                vertices[index++] = j / (float) (n - 1);
                vertices[index++] = i / (float) (m - 1);
            }
        }
        // Indices
        int indicesIndex = 0;
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                int offset = i + j + ((n - 1) * i);

                indices[indicesIndex++] = offset;
                indices[indicesIndex++] = offset + n;
            }
            indices[indicesIndex++] = Integer.MAX_VALUE;

        }

        OGLBuffers.Attrib[] attribs = new OGLBuffers.Attrib[] {
                new OGLBuffers.Attrib("inPosition", 2),
        };

        buffers = new OGLBuffers(vertices, attribs, indices);
    }


    public OGLBuffers getBuffers() {
        return buffers;
    }
}
