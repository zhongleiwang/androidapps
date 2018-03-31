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
)
