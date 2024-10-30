package com.sibsutis.galaxyapp.presentation.opengl.moon

import android.opengl.GLES20
import com.sibsutis.galaxyapp.presentation.opengl.objects.ShaderProgram
import org.intellij.lang.annotations.Language
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import kotlin.math.cos
import kotlin.math.sin

class ShadedSphere(
    private val latitudeBands: Int = 30,
    private val longitudeBands: Int = 30,
    private val radius: Float = 1.0f
) {
    private lateinit var shaderProgram: ShaderProgram
    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var normalBuffer: FloatBuffer
    private lateinit var indexBuffer: ShortBuffer
    private lateinit var textureBuffer: FloatBuffer

    private val vertices: FloatArray
    private val normals: FloatArray
    private val indices: ShortArray
    private val textureCoords: FloatArray

    init {
        val vertexList = mutableListOf<Float>()
        val normalList = mutableListOf<Float>()
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
                val y = cosTheta
                val z = sinPhi * sinTheta

                // Вершины
                vertexList.add(x * radius)
                vertexList.add(y * radius)
                vertexList.add(z * radius)

                // Нормали (нормализованное направление)
                normalList.add(x)
                normalList.add(y)
                normalList.add(z)

                // Текстурные координаты
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
        normals = normalList.toFloatArray()
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

        normalBuffer = ByteBuffer.allocateDirect(normals.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(normals)
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

    fun draw(
        mvpMatrix: FloatArray,
        modelViewMatrix: FloatArray,
        textureId: Int,
        lightPosition: FloatArray,
        cameraPosition: FloatArray
    ) {
        shaderProgram.use()

        // Передача матриц
        val mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgram.programId, "u_MVPMatrix")
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        val modelViewMatrixHandle =
            GLES20.glGetUniformLocation(shaderProgram.programId, "u_ModelViewMatrix")
        GLES20.glUniformMatrix4fv(modelViewMatrixHandle, 1, false, modelViewMatrix, 0)

        // Передача позиции света и камеры
        val lightPositionHandle =
            GLES20.glGetUniformLocation(shaderProgram.programId, "u_LightPosition")
        GLES20.glUniform3fv(lightPositionHandle, 1, lightPosition, 0)

        val cameraPositionHandle =
            GLES20.glGetUniformLocation(shaderProgram.programId, "u_CameraPosition")
        GLES20.glUniform3fv(cameraPositionHandle, 1, cameraPosition, 0)

        // Передача текстуры
        val textureHandle = GLES20.glGetUniformLocation(shaderProgram.programId, "u_Texture")
        GLES20.glUniform1i(textureHandle, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)

        // Передача атрибутов вершин, нормалей и текстурных координат
        val positionHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_Position")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        val normalHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_Normal")
        GLES20.glEnableVertexAttribArray(normalHandle)
        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 0, normalBuffer)

        val texCoordHandle = GLES20.glGetAttribLocation(shaderProgram.programId, "a_TexCoord")
        GLES20.glEnableVertexAttribArray(texCoordHandle)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)

        // Отрисовка сферической сетки
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            indices.size,
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer
        )

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(normalHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

     /** Ambient (Фоновое освещение):
     * - Симулирует рассеянный свет в сцене, который равномерно освещает объект.
     * - Это постоянный цвет, который не зависит от угла освещения.
      *
     **  Diffuse (Диффузное освещение):
      * - Симулирует направленный свет, который зависит от ориентации поверхности относительно источника света.
      * - Чем больше угол между направлением на источник света и нормалью поверхности, тем меньше освещение.
      * Это моделируется с помощью косинуса угла между нормалью и направлением на свет.
      *
     ** Specular (Зеркальное освещение):
      * - Симулирует отражение света от блестящих поверхностей.
      * - Спекулярное освещение зависит от угла между направлением взгляда и направлением отраженного света.
      * Чем ближе угол к направлению взгляда, тем ярче отражение.
      * - Обычно имеет показатель блеска (shineness), чтобы контролировать "резкость" блика.
     *
     * */

    companion object {
        @Language("GLSL")
        private const val VERTEX_SHADER_CODE = """
            attribute vec4 a_Position;
            attribute vec3 a_Normal;
            attribute vec2 a_TexCoord;

            uniform mat4 u_MVPMatrix;
            uniform mat4 u_ModelViewMatrix;

            varying vec3 v_Normal;
            varying vec3 v_Position;
            varying vec2 v_TexCoord;

            void main() {
                // Вычисляем направление на источник света
                v_Position = vec3(u_ModelViewMatrix * a_Position);
                // Преобразуем нормаль в пространство камеры и передаем ее во фрагментный шейдер
                v_Normal = normalize(mat3(u_ModelViewMatrix) * a_Normal);
                // Преобразуем позицию вершины в пространство камеры и передаем ее во фрагментный шейдер
                v_TexCoord = a_TexCoord;
                // Передаем текстурные координаты во фрагментный шейдер
                gl_Position = u_MVPMatrix * a_Position;
            }
        """

        @Language("GLSL")
        private const val FRAGMENT_SHADER_CODE = """
            precision mediump float;
    
            uniform sampler2D u_Texture;
            uniform vec3 u_LightPosition;
            uniform vec3 u_CameraPosition;
            
            varying vec3 v_Normal;
            varying vec3 v_Position;
            varying vec2 v_TexCoord;
            
            void main() {
                //вычисление направлений
                vec3 lightDir = normalize(u_LightPosition - v_Position);
                vec3 viewDir = normalize(u_CameraPosition - v_Position);
                vec3 reflectDir = reflect(-lightDir, normalize(v_Normal));
                
                //фоновое освещение
                float ambient = 0.4;
                //дифузное
                float diffuse = max(dot(normalize(v_Normal), lightDir), 0.4);
                //зеркальное
                float specular = pow(max(dot(viewDir, reflectDir), 0.0), 16.0);
            
                vec4 textureColor = texture2D(u_Texture, v_TexCoord);
                vec3 color = (ambient + 0.8 * diffuse + 0.6 * specular) * textureColor.rgb;
            
                gl_FragColor = vec4(color, 1.0);
            }
"""
    }
}