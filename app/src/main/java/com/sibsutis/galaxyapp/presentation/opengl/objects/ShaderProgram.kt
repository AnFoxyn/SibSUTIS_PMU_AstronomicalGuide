package com.sibsutis.galaxyapp.presentation.opengl.objects

import android.opengl.GLES20
import android.util.Log

class ShaderProgram(vertexShaderCode: String, fragmentShaderCode: String) {
    var programId: Int

    init {
        val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        programId = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == GLES20.GL_FALSE) {
            val errorMessage = GLES20.glGetProgramInfoLog(programId)
            Log.e("com.sibsutis.galaxyapp.opengl.ShaderProgram", "Error linking program: $errorMessage")
        }
    }

    fun use() {
        GLES20.glUseProgram(programId)
    }

    private fun compileShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] == GLES20.GL_FALSE) {
            val errorMessage = GLES20.glGetShaderInfoLog(shader)
            Log.e("com.sibsutis.galaxyapp.opengl.ShaderProgram", "Error compiling shader: $errorMessage")
        }

        return shader
    }

    init {
        val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        programId = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)

            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == GLES20.GL_FALSE) {
                val errorMessage = GLES20.glGetProgramInfoLog(programId)
                Log.e("com.sibsutis.galaxyapp.opengl.ShaderProgram", "Error linking program: $errorMessage")
            }
        }
    }

}
