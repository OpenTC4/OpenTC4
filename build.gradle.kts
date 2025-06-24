
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

tasks.register("fullBuild") {
    group = "build"
    description = "The build that actually works. Does not clean the build directory, file locks on Windows."

    dependsOn("spotlessApply", "build")
}

