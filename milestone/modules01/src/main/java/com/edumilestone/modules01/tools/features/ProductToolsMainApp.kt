package com.eMilestone.modules01.tools.features

import android.util.Log
import com.eMilestone.modules01.tools.features.ocr.OCRProcessor
import com.eMilestone.modules01.tools.features.pdf.PDFProcessor
import com.eMilestone.modules01.tools.features.word.WordProcessor

/**
 * ✅ ProductToolsMainApp manages the initialization & cleanup of Module 01.
 * ✅ It ensures that OCR, PDF, and Word tools are properly handled.
 */
object ProductToolsMainApp {

    /**
     * ✅ Initializes **Module 01** (OCR, PDF, Word Tools).
     * Called when **Module 01** is loaded.
     */
    fun initializeModule01(): Boolean {
        // Initialization logic for Module01
        return try {
            Log.d("ProductToolsMainApp", "🚀 Initializing Module 01 (OCR, PDF, Word Tools)")
            // Initialize all tools in Module 01
            initializeOcrTool()
            initializePdfTool()
            initializeWordTool()
            true
        } catch (e: Exception) {
            // Handle any exceptions
            false
        }
    }
    /**
     * ✅ Cleans up resources when **Module 01** is unloaded.
     * Called when **Module 01** is unloaded.
     */
    fun cleanupModule01(): Boolean {
        return try {
            Log.d("ProductToolsMainApp", "❌ Cleaning up Module 01 (OCR, PDF, Word Tools)")

            // Cleanup all tools in Module 01
            cleanupOcrTool()
            cleanupPdfTool()
            cleanupWordTool()
            true
        }
        catch (e: Exception) {
            // Handle any exceptions
            false
        }
    }

    // ======================== OCR Tool ========================
    private fun initializeOcrTool() {
        Log.d("ProductToolsMainApp", "✅ Initializing OCR Tool")
        OCRProcessor.initialize()
    }

    private fun cleanupOcrTool() {
        Log.d("ProductToolsMainApp", "❌ Cleaning up OCR Tool")
        OCRProcessor.releaseResources()
    }

    // ======================== PDF Tool ========================
    private fun initializePdfTool() {
        Log.d("ProductToolsMainApp", "✅ Initializing PDF Tool")
        PDFProcessor.initialize()
    }

    private fun cleanupPdfTool() {
        Log.d("ProductToolsMainApp", "❌ Cleaning up PDF Tool")
        PDFProcessor.releaseResources()
    }

    // ======================== Word Tool ========================
    private fun initializeWordTool() {
        Log.d("ProductToolsMainApp", "✅ Initializing Word Tool")
        WordProcessor.initialize()
    }

    private fun cleanupWordTool() {
        Log.d("ProductToolsMainApp", "❌ Cleaning up Word Tool")
        WordProcessor.releaseResources()
    }
}
