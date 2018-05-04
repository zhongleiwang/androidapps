package(default_visibility = ["//visibility:public"])

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
    name = "firebase_lib",
    srcs = [
        "java/com/cwave/firebase/Auth.java",
        "java/com/cwave/firebase/AuthImpl.java",
        "java/com/cwave/firebase/Database.java",
        "java/com/cwave/firebase/DatabaseImpl.java",
        "java/com/cwave/firebase/Store.java",
        "java/com/cwave/firebase/StoreImpl.java",
    ],
    manifest = "AndroidManifest.xml",
    deps = [
        "//proto/src/main/proto:user",
        "//third_party:auto_value",
        "//third_party:com_android_support_support_annotations",
        "//third_party:guava_android",
        "//third_party:joda_time",
        "//third_party:jsr305_annotations",
        "//third_party:jsr330_inject",
        "//third_party:okhttp",
        "//third_party:otto",
        "//third_party:protobuf_lite",
        "//third_party:volley",
        "@com_android_support_constraint_constraint_layout//aar",
        "@com_android_support_design//aar",
        "@dagger//:android",
        "@dagger//:android-support",
        "@dagger//:dagger_with_compiler",
        "@firebase-analytics-impl//aar",
        "@firebase-analytics//aar",
        "@firebase-auth//aar",
        "@firebase-common//aar",
        "@firebase-core//aar",
        "@firebase-database-connection//aar",
        "@firebase-database//aar",
        "@firebase-firestore//aar",
        "@firebase-iid//aar",
        "@firebase-ui-auth//aar",
        "@firebase-ui-common//aar",
        "@firebase-ui-database//aar",
        "@firebase-ui-storage//aar",
        "@play-services-base//aar",
        "@play-services-tasks//aar",
    ],
)

android_binary(
    name = "firebase",
    custom_package = "com.cwave.exchange",
    manifest = "AndroidManifest.xml",
    manifest_values = {
        "applicationId": "com.cwave.firebase",
    },
    multidex = "native",
    resource_files = glob(["res/**"]) + [
        ":google_services_json",
    ],
    deps = [
        ":firebase_lib",
    ],
)
