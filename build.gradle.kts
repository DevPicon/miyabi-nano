// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false // Use alias from version catalog
    alias(libs.plugins.ksp) apply false // Add KSP plugin
    // Top-level build file where you can add configuration options common to all sub-projects/modules.
}