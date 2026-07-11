package com.example.tfimageclassifier.data.local

import android.content.Context
import android.graphics.Bitmap
import com.example.tfimageclassifier.domain.model.ClassificationResult
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Low-level TensorFlow Lite wrapper.
 * Responsible ONLY for loading the model, pre-processing input, and running inference.
 *
 * Model expected in assets/mobilenet_v1.tflite
 * Labels expected in assets/labels.txt
 */
@Singleton
class TFLiteImageClassifier @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val MODEL_FILE = "mobilenet_v1.tflite"
        private const val LABELS_FILE = "labels.txt"
        private const val INPUT_SIZE = 224   // MobileNet v1 input dimension
    }

    // Lazily initialized interpreter — loads model only when first needed
    private val interpreter: Interpreter by lazy {
        val model = FileUtil.loadMappedFile(context, MODEL_FILE)
        val options = Interpreter.Options().apply {
            numThreads = 4
        }
        Interpreter(model, options)
    }

    private val labels: List<String> by lazy {
        FileUtil.loadLabels(context, LABELS_FILE)
    }

    /** Pre-processing pipeline: resize → convert to float */
    private val imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(INPUT_SIZE, INPUT_SIZE, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(0f, 255f))
        .build()

    /**
     * Run inference on a [Bitmap] and return raw classification results.
     */
    fun classify(bitmap: Bitmap): List<ClassificationResult> {
        // 1. Prepare input tensor
        val tensorImage = TensorImage.fromBitmap(bitmap)
        val processedImage = imageProcessor.process(tensorImage)

        // 2. Prepare output buffer
        val outputShape = interpreter.getOutputTensor(0).shape()
        val outputDataType = interpreter.getOutputTensor(0).dataType()
        val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

        // 3. Run inference
        interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())

        // 4. Map probabilities → label strings
        val labeledProbabilities = TensorLabel(labels, outputBuffer).mapWithFloatValue

        return labeledProbabilities.map { (label, score) ->
            ClassificationResult(label = label, score = score)
        }
    }
}
