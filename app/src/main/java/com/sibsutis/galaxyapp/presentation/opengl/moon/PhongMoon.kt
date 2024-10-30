package com.sibsutis.galaxyapp.presentation.opengl.moon

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import com.sibsutis.galaxyapp.R
import javax.microedition.khronos.opengles.GL10


class PhongMoon(
    private val context: Context
) : GLSurfaceView.Renderer {

    private lateinit var sphere: ShadedSphere
    private var textureId: Int = 0

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val modelViewMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private val cameraPosition = floatArrayOf(0f, 0f, 5f) // Камера перед сферой
    private val lightPosition = floatArrayOf(2f, 2f, 3f)  // Свет сбоку

    init {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.setLookAtM(
            viewMatrix, 0,
            cameraPosition[0], cameraPosition[1], cameraPosition[2], // Позиция камеры
            0f, 0f, 0f, // Точка, на которую смотрит камера
            0f, 1f, 0f // Вектор "вверх"
        )
    }

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        sphere = ShadedSphere()
        sphere.initialize()
        textureId = loadTexture(context, R.drawable.moon)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        val left = -ratio
        val right = ratio
        val bottom = -1f
        val top = 1f
        val near = 1f
        val far = 10f

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)

        sphere.draw(
            mvpMatrix = modelViewProjectionMatrix,
            modelViewMatrix = modelViewMatrix,
            textureId = textureId,
            lightPosition = lightPosition,
            cameraPosition = cameraPosition
        )
    }

    private fun loadTexture(context: Context, resourceId: Int): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] != 0) {
            val options = BitmapFactory.Options().apply { inScaled = false }
            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        }

        if (textureHandle[0] == 0) {
            throw RuntimeException("Ошибка загрузки текстуры.")
        }

        return textureHandle[0]
    }
}