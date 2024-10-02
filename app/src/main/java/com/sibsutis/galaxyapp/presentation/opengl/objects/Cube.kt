package com.sibsutis.galaxyapp.presentation.opengl.objects
import android.opengl.GLES20
import android.util.Log
import org.intellij.lang.annotations.Language
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube {
    private lateinit var shaderProgram: ShaderProgram
    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var indexBuffer: ShortBuffer

    private val vertices = floatArrayOf(

        -1f, 1f, 1f,       0f, 0f, 0f, 1f,
        -1f, -1f, 1f,      0f, 0f, 0f, 1f,
        1f, -1f, 1f,       0f, 0f, 1f, 1f,
        1f, 1f, 1f,        0f, 0f, 0f, 1f,
        -1f, 1f, -1f,      0f, 0f, 0f, 1f,
        -1f, -1f, -1f,     0f, 0f, 0f, 1f,
        1f, -1f, -1f,      0f, 0f, 1f, 1f,
        1f, 1f, -1f,       0f, 0f, 0f, 1f
    )


    private val indices = shortArrayOf(
        0, 1, 2, 0, 2, 3,
        4, 5, 6, 4, 6, 7,
        0, 1, 5, 0, 5, 4,
        3, 2, 6, 3, 6, 7,
        0, 3, 7, 0, 7, 4,
        1, 2, 6, 1, 6, 5
    )

    fun initialize() {
        shaderProgram = ShaderProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE)

        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }

        indexBuffer = ByteBuffer.allocateDirect(indices.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(indices)
                position(0)
            }
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        shaderProgram.use()

        val positionHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_Position")
        val colorHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_Color")
        val mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgram.programId, "u_MVPMatrix")

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(colorHandle)

        val stride = 7 * 4

        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, stride, vertexBuffer)

        vertexBuffer.position(3)
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, stride, vertexBuffer)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.size, GLES20.GL_UNSIGNED_SHORT, indexBuffer)

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(colorHandle)
    }




    companion object {
        @Language("GLSL")
        private const val VERTEX_SHADER_CODE = """
            attribute vec4 a_Position;
            attribute vec4 a_Color;
            uniform mat4 u_MVPMatrix;
            varying vec4 v_Color;

            void main() {
                gl_Position = u_MVPMatrix * a_Position;
                v_Color = a_Color;
            }
        """

        @Language("GLSL")
        private const val FRAGMENT_SHADER_CODE = """
            precision mediump float;
            varying vec4 v_Color;

            void main() {
                gl_FragColor = v_Color;
            }
        """
    }
}
