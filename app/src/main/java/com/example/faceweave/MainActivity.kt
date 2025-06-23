package com.example.faceweave

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.facemesh.FaceMesh
import com.google.mlkit.vision.facemesh.FaceMeshDetection
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModels()

    private lateinit var imageView: ImageView
    private lateinit var overlay: FaceMeshOverlay
    private lateinit var resultTextView: TextView
    private lateinit var leftEyeProbabilityTextView: TextView
    private lateinit var rightEyeProbabilityTextView: TextView
    private lateinit var smileProbabilityTextView: TextView
    private lateinit var labelTextView: TextView
    private lateinit var loadingSpinner: ProgressBar

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100

    private var capturedBitmap: Bitmap? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables the SplashScreen
        val splashScreen = installSplashScreen()

        // Keep splash visible until loading is complete
        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

        // Add animation (e.g., fade)
        splashScreen.setOnExitAnimationListener { splashView ->
            splashView.iconView.animate()
                .translationY(-200f)
                .scaleX(2f)
                .scaleY(2f)
                .alpha(0f)
                .setDuration(200)
                .withEndAction { splashView.remove() }
                .start()
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.imageView)
        overlay = findViewById(R.id.overlay)
        resultTextView = findViewById(R.id.resultTextView)
        leftEyeProbabilityTextView = findViewById(R.id.leftEyeProbabilityTextView)
        rightEyeProbabilityTextView = findViewById(R.id.rightEyeProbabilityTextView)
        smileProbabilityTextView = findViewById(R.id.smileProbabilityTextView)
        labelTextView = findViewById(R.id.labelTextView)
        loadingSpinner = findViewById(R.id.loadingSpinner)

        findViewById<Button>(R.id.btnCamera).setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "Camera not supported", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                capturedBitmap = imageBitmap

                imageView.setImageBitmap(imageBitmap)
                overlay.setFaceMeshes(emptyList(), 1, 1) // Reset

                // Sync overlay size
                imageView.post {
                    overlay.layoutParams.width = imageView.width
                    overlay.layoutParams.height = imageView.height
                    overlay.requestLayout()
                }

                runAllMLKitDetections(imageBitmap)
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun runAllMLKitDetections(bitmap: Bitmap) {
        loadingSpinner.visibility = View.VISIBLE
        val image = try {
            InputImage.fromBitmap(bitmap, 0)
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading image: ${e.message}", Toast.LENGTH_LONG).show()
            loadingSpinner.visibility = View.GONE
            return
        }

        runFaceDetection(image)
        runFaceMeshDetection(image, bitmap)
        runImageLabelingSafe(image)
    }

    private fun runFaceDetection(image: InputImage) {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { faces -> processFaces(faces) }
            .addOnFailureListener {
                resultTextView.text = "Face Detection Error: ${it.message}"
            }
    }

    private fun processFaces(faces: List<Face>) {
        if (faces.isEmpty()) {
            resultTextView.text = "No face detected."
            leftEyeProbabilityTextView.text = ""
            rightEyeProbabilityTextView.text = ""
            smileProbabilityTextView.text = ""
            return
        }

        val face = faces[0]
        resultTextView.text = "Face Detected"

        leftEyeProbabilityTextView.text =
            "Left Eye Open: %.2f".format(face.leftEyeOpenProbability ?: 0f)
        rightEyeProbabilityTextView.text =
            "Right Eye Open: %.2f".format(face.rightEyeOpenProbability ?: 0f)
        smileProbabilityTextView.text =
            "Smiling: %.2f".format(face.smilingProbability ?: 0f)
    }

    private fun runFaceMeshDetection(image: InputImage, bitmap: Bitmap) {
        val options = FaceMeshDetectorOptions.Builder()
            .setUseCase(FaceMeshDetectorOptions.FACE_MESH)
            .build()

        val detector = FaceMeshDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { meshes ->
                overlay.setFaceMeshes(meshes, bitmap.width, bitmap.height)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Face mesh detection failed: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnCompleteListener {
                loadingSpinner.visibility = View.GONE
            }
    }

    private fun runImageLabelingSafe(image: InputImage) {
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val labelText = labels.joinToString("\n") {
                    "- ${it.text} (%.2f)".format(it.confidence)
                }
                labelTextView.text = labelText
            }
            .addOnFailureListener {
                labelTextView.text = "Image labeling failed: ${it.message}"
            }
    }
}
