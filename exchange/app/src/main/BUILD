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
        "//proto/src/main/proto:user",
        "//firebase/src/main:firbase_lib",
        "//third_party:auto_value",
        "//third_party:guava_android",
        "//third_party:joda_time",
        "//third_party:jsr305_annotations",
        "//third_party:jsr330_inject",
        "//third_party:okhttp",
        "//third_party:otto",
        "//third_party:protobuf_lite",
        "//third_party:volley",
        "@androidsdk//com.android.support:appcompat-v7-25.0.0",
        "@androidsdk//com.android.support:cardview-v7-25.0.0",
        "@androidsdk//com.android.support.constraint:constraint-layout-1.0.0-beta4",
        "@androidsdk//com.android.support:design-25.0.0",
        "@androidsdk//com.android.support:recyclerview-v7-25.0.0",
        "@androidsdk//com.android.support:support-annotations-25.3.1",
        "@dagger//:android",
        "@dagger//:android-support",
        "@dagger//:dagger_with_compiler",
        "@firebase-analytics-impl//aar",
        "@firebase-analytics//aar",
        "@firebase-common//aar",
        "@firebase-database-connection//aar",
        "@firebase-database//aar",
        "@firebase-firestore//aar",
        "@firebase-iid//aar",
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
