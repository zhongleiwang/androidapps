genrule(
    name = "google_services_json",
    srcs = [
        "//calculation/app/src/debug:google-services.json",
    ],
    outs = [
        "res/values/googleservices.xml",
    ],
    cmd = (
        "$(location //third_party/firebase:generate_xml_from_google_services_json) " +
        " -i $(location //calculation/app/src/debug:google-services.json) " +
        " -o $(location res/values/googleservices.xml)"
    ),
    tools = [
        "//third_party/firebase:generate_xml_from_google_services_json",
    ],
)

android_library(
    name = "calculation_lib",
    srcs = glob(["java/**/*.java"]),
    custom_package = "com.cwave.calculation",
    manifest = "AndroidManifest.xml",
    resource_files = glob(["res/**"]),
    deps = [
        # "//firebase/src/main:firebase_lib",
        "//proto/src/main/proto:record",
        "//third_party:android_arch_lifecycle_common",
        "//third_party:auto_value",
        "//third_party:guava_android",
        "//third_party:joda_time",
        "//third_party:jsr305_annotations",
        "//third_party:jsr330_inject",
        "//third_party:otto",
        "//third_party:protobuf_lite",
        "@android_arch_lifecycle_runtime//aar",
        "@com_android_support_appcompat//aar",
        "@com_android_support_design//aar",
        "@com_android_support_cardview//aar",
        "@com_android_support_multidex//aar",
        "@com_android_support_recyclerview//aar",
        "@com_android_support_support-compat//aar",
        "@com_android_support_support_core_ui//aar",
        "@com_android_support_support_fragment//aar",
        "@com_android_support_support_annotations//jar",
        "@com_firebase_firebase_jobdispatcher//aar",
        "@dagger//:android",
        "@dagger//:android-support",
        "@dagger//:dagger_with_compiler",
        "@firebase-analytics-impl//aar",
        "@firebase-analytics//aar",
        "@firebase-auth//aar",
        "@firebase-common//aar",
        "@firebase-config//aar",
        "@firebase-core//aar",
        "@firebase-crash//aar",
        "@firebase-database//aar",
        "@firebase-database-connection//aar",
        "@firebase-firestore//aar",
        "@firebase-iid//aar",
        "@firebase-messaging//aar",
        "@firebase-perf//aar",
        "@firebase-storage//aar",
        "@firebase-ui-auth//aar",
        "@firebase-ui-common//aar",
        "@firebase-ui-database//aar",
        "@firebase-ui-firestore//aar",
        "@firebase-ui-storage//aar",
        "@play-services-auth//aar",
        "@play-services-base//aar",
        "@play-services-basement//aar",
        "@play-services-location//aar",
        "@play-services-tasks//aar",
        "@play-services//aar",
        "@facebook-login//aar",
        "@twitter_core//aar",
        "@com_squareup_retrofit2_retrofit//jar",
        "@com_google_code_gson_gson//jar",
        "@com_squareup_okhttp3_okhttp//jar",
        "@com_squareup_okio_okio//jar",
        "@com_squareup_okhttp_okhttp//jar",
    ],
)

android_binary(
    name = "calculation",
    custom_package = "com.cwave.calculation",
    manifest = "AndroidManifest.xml",
    manifest_merger = "android",
    manifest_values = {
        "applicationId": "com.cwave.calculation",
    },
    multidex = "native",
    proguard_specs = [
        ":proguard.pro",
    ],
    resource_files = glob(["res/**"]) + [
        ":google_services_json",
    ],
    deps = [
        ":calculation_lib",
    ],
)

exports_files(
    [
        "proguard-android.txt",
        "proguard.pro",
    ],
)
