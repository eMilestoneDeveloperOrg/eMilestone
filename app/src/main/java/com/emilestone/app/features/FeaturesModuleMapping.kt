package com.eMilestone.app.features

// Mapping of features to corresponding modules
/**
 * This object maps feature names (such as OCR, PDF, and Word) to their corresponding module names.
 * It encapsulates the mapping logic and provides a method to retrieve the module name based on a feature.
 */
object FeaturesModuleMapping {

    // Private map that holds the relationship between feature names and corresponding module names
    // This map ensures that only valid feature names can be mapped to modules, and prevents direct external access to the map
    private val featureToModuleMap = mapOf(
        "OCR" to "Module01",   // The "OCR" feature is mapped to "Module01" (for Optical Character Recognition)
        "PDF" to "Module01",   // The "PDF" feature is mapped to "Module01" (for PDF Viewer functionality)
        "WORD" to "Module01",  // The "WORD" feature is mapped to "Module01" (for Word Processor functionality)
        // You can add more feature-to-module mappings here as your app expands
        // For example:
        // "ImageProcessing" to "Module02"
        // "VideoEditing" to "Module03"
    )

    /**
     * This function retrieves the corresponding module name for a given feature.
     * The mapping of features to modules is encapsulated within the private map featureToModuleMap.
     *
     * @param feature The name of the feature (e.g., "OCR", "PDF", "WORD")
     * @return The module name as a string, or null if the feature is not found in the map
     */
    fun getModuleForFeature(feature: String): String? {
        // The function checks the map for the provided feature name and returns the corresponding module name
        // If the feature is not found in the map, it will return null
        return featureToModuleMap[feature]
    }
}
