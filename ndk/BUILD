cc_library(
    name = "log",
    srcs = [
        "log.cc",
    ],
    copts = [
        "-fPIE",
        "-pie",
    ],
)

cc_binary(
    name = "main",
    srcs = [
        "main.cc", 
    ], 
    copts = [
        "-fPIE",
        "-pie", 
    ], 
    linkopts = [ 
        "-fPIE", 
        "-pie", 
        "-llog", 
        "-landroid", 
        "-Wl,-z,defs", 
        "-Wl,--no-undefined", 
    ],
    deps = [
        ":log",
    ]
)
