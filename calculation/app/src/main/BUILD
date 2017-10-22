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
        "//firebase/src/main:firbase_lib",
        "//proto/src/main/proto:record",
        "//third_party:auto_value",
        "//third_party:guava_android",
        "//third_party:joda_time",
        "//third_party:jsr305_annotations",
        "//third_party:jsr330_inject",
        "//third_party:otto",
        "//third_party:protobuf_lite",
        "@androidsdk//com.android.support:appcompat-v7-25.3.1",
        "@androidsdk//com.android.support:cardview-v7-25.3.1",
        "@androidsdk//com.android.support:design-25.3.1",
        "@androidsdk//com.android.support:recyclerview-v7-25.3.1",
        "@dagger//:android",
        "@dagger//:android-support",
        "@dagger//:dagger_with_compiler",
    ],
)

android_binary(
    name = "calculation",
    custom_package = "com.cwave.calculation",
    manifest = "AndroidManifest.xml",
    manifest_merger = "android",
    manifest_values = {
        "applicationId": "com.cwave..calculation",
    },
    multidex = "native",
    resource_files = glob(["res/**"]) + [
        ":google_services_json",
    ],
    deps = [
        ":calculation_lib",
    ],
)
