import java.io.FileInputStream
import java.util.Properties

val secretPropertiesFile = rootProject.file("secrets.properties")
val secretProperties = Properties()
secretProperties.load(FileInputStream(secretPropertiesFile))

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

vkidManifestPlaceholders {
    val clientId = secretProperties["VKIDClientId"].toString()
    val clientSecret = secretProperties["VKIDClientSecret"].toString()

    init(
        clientId = clientId,
        clientSecret = clientSecret,
    )

    vkidRedirectHost = "vk.com"
    vkidRedirectScheme = "vk$clientId"
    vkidClientId = clientId
    vkidClientSecret = clientSecret
}