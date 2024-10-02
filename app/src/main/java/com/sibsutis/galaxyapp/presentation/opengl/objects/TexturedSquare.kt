package com.sibsutis.galaxyapp.presentation.opengl.objects

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import com.sibsutis.galaxyapp.R
import org.intellij.lang.annotations.Language
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TexturedSquare(private val context: Context) {
    private lateinit var shaderProgram: ShaderProgram
    private var textureId: Int = 0
    private lateinit var vertexBuffer: FloatBuffer


    private val vertices = floatArrayOf(
        -1f, 1f, 0f, 0f, 0f,
        -1f, -1f, 0f, 0f, 1f,
        1f, 1f, 0f, 1f, 0f,
        1f, -1f, 0f, 1f, 1f
    )

    fun initialize() {
        shaderProgram = ShaderProgram(
            TexturedSquare.Companion.VERTEX_SHADER_CODE,
            TexturedSquare.Companion.FRAGMENT_SHADER_CODE
        )
        textureId = loadTexture(context, R.drawable.galaxy_texture)
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        shaderProgram.use()

        val positionHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_Position")
        val texCoordHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_TexCoordinate")
        val mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgram.programId, "u_MVPMatrix")

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(texCoordHandle)

        val stride = 5 * 4 // 5 elements per vertex, 4 bytes per float
        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(
            positionHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            stride,
            vertexBuffer
        )
        vertexBuffer.position(3)
        GLES20.glVertexAttribPointer(
            texCoordHandle,
            2,
            GLES20.GL_FLOAT,
            false,
            stride,
            vertexBuffer
        )

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(GLES20.glGetUniformLocation(shaderProgram.programId, "u_Texture"), 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

    private fun loadTexture(context: Context, resourceId: Int): Int {
        val textureIds = IntArray(1)
        GLES20.glGenTextures(1, textureIds, 0)

        if (textureIds[0] == 0) {
            throw RuntimeException("Error generating texture ID.")
        }

        val options = BitmapFactory.Options()
        options.inScaled = false // No pre-scaling

        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)
            ?: throw RuntimeException("Error loading texture.")

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0])

        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR_MIPMAP_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        bitmap.recycle()

        return textureIds[0]
    }

    companion object {
        @Language("GLSL")
        private const val VERTEX_SHADER_CODE = """
            attribute vec4 a_Position;
            attribute vec2 a_TexCoordinate;
            uniform mat4 u_MVPMatrix;
            varying vec2 v_TexCoordinate;

            void main() {
                gl_Position = u_MVPMatrix * a_Position;
                v_TexCoordinate = a_TexCoordinate;
            }
        """

        @Language("GLSL")
        private const val FRAGMENT_SHADER_CODE = """
            precision mediump float;
            uniform sampler2D u_Texture;
            varying vec2 v_TexCoordinate;

            void main() {
                gl_FragColor = texture2D(u_Texture, v_TexCoordinate);
            }
        """
    }
}
