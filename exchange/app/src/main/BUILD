# Simulate google-services gradle plugin
genrule(
    name = "google_services_json",
    srcs = [
        "//exchange/app/src/debug:google-services.json",
    ],
    outs = [
        "res/values/googleservices.xml",
    ],
    cmd = (
        "$(location //third_party/firebase:generate_xml_from_google_services_json) " +
        " -i $(location //exchange/app/src/debug:google-services.json) " +
        " -o $(location res/values/googleservices.xml)"
    ),
    tools = [
        "//third_party/firebase:generate_xml_from_google_services_json",
    ],
)

android_library(
    name = "exchange_lib",
    srcs = glob([
        "java/**/*.java",
    ]),
    custom_package = "com.cwave.exchange",
    manifest = "AndroidManifest.xml",
    resource_files = glob(["res/**"]) + [
        ":google_services_json",
    ],
    deps = [
        "//firebase/src/main:firebase_lib",
        "//proto/src/main/proto:user",
        "//third_party:auto_value",
        "//third_party:guava_android",
        "//third_party:joda_time",
        "//third_party:jsr305_annotations",
        "//third_party:jsr330_inject",
        "//third_party:okhttp",
        "//third_party:otto",
        "//third_party:protobuf_lite",
        "//third_party:volley",
        "//third_party:android_arch_lifecycle_common",
        "//third_party:com_android_support_support_annotations",
        "//third_party:com_android_support_constraint_constraint_layout_solver",
        "@com_android_support_design//aar",
        "@com_android_support_constraint_constraint_layout//aar",
        "@com_android_support_support_compat//aar",
        "@com_android_support_appcompat//aar",
        "@com_android_support_support_v4//aar",
        "@com_android_support_support_media_compat//aar",
        "@com_android_support_support_core_utils//aar",
        "@com_android_support_support_core_ui//aar",
        "@android_arch_lifecycle_runtime//aar",
        "@com_android_support_support_vector_drawable//aar",
        "@com_android_support_animated_vector_drawable//aar",
        "@dagger//:android",
        "@dagger//:android-support",
        "@dagger//:dagger_with_compiler",
        "@firebase-auth//aar",
        "@firebase-firestore//aar",
        "@firebase-ui-auth//aar",
        "@firebase-ui-firestore//aar",
        "@play-services-basement//aar",
        "@play-services-tasks//aar",
    ],
)

android_binary(
    name = "exchange",
    custom_package = "com.cwave.exchange",
    manifest = "AndroidManifest.xml",
    manifest_merger = "android",
    manifest_values = {
        "applicationId": "com.cwave.exchange",
    },
    multidex = "native",
    resource_files = glob(["res/**"]) + [
        ":google_services_json",
    ],
    deps = [
        ":exchange_lib",
    ],
)
