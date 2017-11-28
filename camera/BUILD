cc_library(
    name = "camera_lib",
    srcs = ["main.cc"],
    copts = [
        "-fPIE",
        "-pie",
    ],
)

cc_binary(
    name = "camera",
    srcs = [
        "main.cc",
    ],
    copts = [
        "-fPIE",
        "-pie",
        "-std=c++11",
    ],
    linkopts = [
        "-fPIE",
        "-pie",
        "-landroid",
        "-ldl",
        "-ljnigraphics",
        "-llog",
        "-lm",
        "-z defs",
        "-s",
    ],
)
