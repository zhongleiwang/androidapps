package(default_visibility = ["//visibility:public"])

java_library(
    name = "user",
    exports = ["user_java_lite_proto"],
)

java_lite_proto_library(
    name = "user_java_lite_proto",
    deps = [":user_proto"],
)

proto_library(
    name = "user_proto",
    srcs = ["user.proto"],
)

java_library(
    name = "record",
    exports = ["record_java_lite_proto"],
)

java_lite_proto_library(
    name = "record_java_lite_proto",
    deps = [":record_proto"],
)

proto_library(
    name = "record_proto",
    srcs = ["record.proto"],
)
