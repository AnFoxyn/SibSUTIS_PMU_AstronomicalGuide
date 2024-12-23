package com.sibsutis.galaxyapp.presentation.opengl.courseKornilov

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import com.sibsutis.galaxyapp.R
import com.sibsutis.galaxyapp.presentation.opengl.coursePopov.Candle
import com.sibsutis.galaxyapp.presentation.opengl.objects.Fire
import com.sibsutis.galaxyapp.presentation.opengl.objects.Fruit
import com.sibsutis.galaxyapp.presentation.opengl.objects.Table
import javax.microedition.khronos.opengles.GL10

class TableRender(private val context: Context) : GLSurfaceView.Renderer {

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val mVPMatrix = FloatArray(16)
    private val normalMatrix = FloatArray(16)
    private val lightPos = floatArrayOf(0f, -0.03f, 2.8f)

    private lateinit var table: Table
    private lateinit var glass: GlassСup
    private lateinit var candle: Candle
    private lateinit var candleFire: Fire
    private lateinit var melon: Fruit
    private lateinit var grape: Fruit
    private lateinit var qiwi: Fruit
    private lateinit var lemon: Fruit

    private var melonTexture: Int = 0
    private var grapeTexture: Int = 0
    private var qiwiTexture: Int = 0
    private var lemonTexture: Int = 0

    override fun onDrawFrame(arg0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        val time = System.currentTimeMillis() % 10000L / 1000.0f // в секундах

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.setLookAtM(viewMatrix,
            0, 0.5f, 0f, 5f,
            0.3f, 1f, 0f,
            0f, 0.8f, 0f)

        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.invertM(normalMatrix, 0, viewMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, -1.3f, -1f)
        Matrix.rotateM(modelMatrix, 0, 15f, 0.1f, 0.1f, 0f)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0)
        Matrix.setIdentityM(modelMatrix, 0)
        table.draw(mVPMatrix)

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0.4f, -0.3f, 2.6f)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0)
        melon.draw(mVPMatrix, normalMatrix, lightPos, viewMatrix, melonTexture)

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.translateM(modelMatrix, 0, -0.3f, -0.5f, 2f)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0)
        grape.draw(mVPMatrix, normalMatrix, lightPos, viewMatrix, grapeTexture)

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0.5f, -0.6f, 2.9f)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0)
        lemon.draw(mVPMatrix, normalMatrix, lightPos, viewMatrix, lemonTexture)

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 1f, -0.55f, 2.8f)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0)
        qiwi.draw(mVPMatrix, normalMatrix, lightPos, viewMatrix, qiwiTexture)

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0.73f, -0.5f, 2.9f)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0)
        glass.draw(mVPMatrix, lightPos, viewMatrix)


        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        //Matrix.translateM(modelMatrix, 0, 0f, -0.5f, 2.8f)
        Matrix.translateM(modelMatrix, 0, 0f, -0.5f, 2.8f)
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0)
        candle.draw(mVPMatrix, lightPos, viewMatrix)


        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, lightPos[0], lightPos[1], lightPos[2])
        Matrix.multiplyMM(mVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0)
        candleFire.draw(mVPMatrix, time)
    }

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        GLES20.glClearColor(0f,0f,0f, 0.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LESS)

        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        table = Table(context, R.drawable.course_kornilov_table)
        melon = Fruit(radius = 0.6f/3)
        melonTexture = loadTexture(R.drawable.course_kornilov_melon)

        glass = GlassСup(context)

        candle = Candle(context)
        candleFire = Fire(context, radius = 0.035f)
        candleFire.initialize()

        grape = Fruit(radius = 1f/3)
        grapeTexture = loadTexture(R.drawable.course_kornilov_grape)

        qiwi = Fruit(radius = 0.25f/3)
        qiwiTexture = loadTexture(R.drawable.course_kornilov_qiwi)

        lemon = Fruit(radius = 0.4f/3)
        lemonTexture = loadTexture(R.drawable.course_kornilov_lemon)
    }

    override fun onSurfaceChanged(arg0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 5f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    private fun loadTexture(resourceId: Int): Int {
        val textureIds = IntArray(1)
        GLES20.glGenTextures(1, textureIds, 0)
        val textureId = textureIds[0]

        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        bitmap.recycle()
        return textureId
    }
}