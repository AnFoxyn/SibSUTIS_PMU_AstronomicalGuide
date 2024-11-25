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
import java.nio.ShortBuffer
import kotlin.math.cos
import kotlin.math.sin

class Fire(
    private val context: Context,
    private val latitudeBands: Int = 40,
    private val longitudeBands: Int = 40,
    private val radius: Float = 1.0f
) {
    private lateinit var shaderProgram: ShaderProgram ///!!!!!!
    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var indexBuffer: ShortBuffer
    private lateinit var textureBuffer: FloatBuffer
    private val heightOffset: Float = 1.5f
    private val vertices: FloatArray
    private val indices: ShortArray
    private val textureCoords: FloatArray

    private val fireTextures = listOf(
        loadTexture(R.drawable.course_popov_fire)
    )


    fun loadTexture(resId: Int): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] == 0) {
            throw RuntimeException("Текстура не загружена")
        }
        val options = BitmapFactory.Options().apply { inScaled = false }
        val bitmap = BitmapFactory.decodeResource(context.resources, resId, options)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()

        return textureHandle[0]
    }

    private var currentFrame = 0
    private val frameInterval = 1000
    private var lastFrameTime = System.currentTimeMillis()

    init {
        val vertexList = mutableListOf<Float>()
        val indexList = mutableListOf<Short>()
        val textureList = mutableListOf<Float>()

        for (lat in 0..latitudeBands) {
            val theta = lat * Math.PI / latitudeBands
            val sinTheta = sin(theta).toFloat()
            val cosTheta = cos(theta).toFloat()
            for (long in 0..longitudeBands) {
                val phi = long * 2 * Math.PI / longitudeBands
                val sinPhi = sin(phi).toFloat()
                val cosPhi = cos(phi).toFloat()

                val x = cosPhi * sinTheta
                val y = cosTheta * 5 - heightOffset
                val z = sinPhi * sinTheta

                vertexList.add(x * radius)
                vertexList.add(y * radius)
                vertexList.add(z * radius)

                val u = 1f - (long / longitudeBands.toFloat())
                val v = 1f - (lat / latitudeBands.toFloat())
                textureList.add(u)
                textureList.add(v)
            }
        }

        for (lat in 0 until latitudeBands) {
            for (long in 0 until longitudeBands) {
                val first = (lat * (longitudeBands + 1) + long).toShort()
                val second = (first + longitudeBands + 1).toShort()

                indexList.add(first)
                indexList.add(second)
                indexList.add((first + 1).toShort())

                indexList.add(second)
                indexList.add((second + 1).toShort())
                indexList.add((first + 1).toShort())
            }
        }

        vertices = vertexList.toFloatArray()
        indices = indexList.toShortArray()
        textureCoords = textureList.toFloatArray()
    }

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

        textureBuffer = ByteBuffer.allocateDirect(textureCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(textureCoords)
                position(0)
            }
        }
    }

    fun draw(mvpMatrix: FloatArray, time: Float) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastFrameTime > frameInterval) {
            currentFrame = (currentFrame + 1) % fireTextures.size
            lastFrameTime = currentTime
        }
        val textureId = fireTextures[currentFrame]

        shaderProgram.use()

        val positionHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_Position")
        val texCoordHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_TexCoord")
        val mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgram.programId, "u_MVPMatrix")
        val timeHandle = GLES20.glGetUniformLocation(shaderProgram.programId, "u_Time")

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glUniform1f(timeHandle, time)

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(texCoordHandle)

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.size, GLES20.GL_UNSIGNED_SHORT, indexBuffer)

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

    companion object {
        @Language("GLSL")
        private const val VERTEX_SHADER_CODE = """
            attribute vec4 a_Position;
            attribute vec2 a_TexCoord;
            uniform mat4 u_MVPMatrix;
            uniform float u_Time;
            varying vec2 v_TexCoord;
            varying float v_Offset;
        
            void main() {
                float waveHeightX = cos(a_Position.z * 1.0 + u_Time * 2.0) * 0.01;
                float waveHeightZ = sin(a_Position.x * 1.0 + u_Time * 2.0) * 0.0; // змейка
                float waveHeightY = cos(u_Time * 2.0) * 0.0; // эффект волны
        
                vec4 modPosition = a_Position;
                modPosition.x += waveHeightX;
                modPosition.z += waveHeightZ;
                modPosition.y += waveHeightY;
        
                gl_Position = u_MVPMatrix * modPosition;
        
                if (a_Position.x > 0.0) {
                    // считаем вверху
                    v_TexCoord = vec2(a_TexCoord.x * 0.9, a_TexCoord.x);
                } else {
                    // считаем снизу
                    v_TexCoord = vec2(a_TexCoord.x * 0.9 + 0.5, a_TexCoord.x);
                }
                v_Offset = waveHeightY;
    }
"""


        @Language("GLSL")
        private const val FRAGMENT_SHADER_CODE = """
            precision mediump float;
            varying vec2 v_TexCoord;
            varying float v_Offset;
            uniform sampler2D u_Texture;

            void main() {
                vec4 texColor = texture2D(u_Texture, v_TexCoord);
                float flicker = sin(v_Offset * 1.0) * 0.1;
                texColor.r += flicker * 0.1;
                texColor.g -= flicker * 0.1;
                texColor.a = 1.0 - flicker * 0.1;

                gl_FragColor = texColor;
            }
        """
    }
}