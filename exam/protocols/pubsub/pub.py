import paho.mqtt.publish as publish

msgs = \
[
    {
        'topic':"/jeju/sotolab",
        'payload':"multiple 1"
    },

    (
        "/jeju/sotolab",
        "multiple 2", 0, False
    )
]
publish.multiple(msgs, hostname="192.168.200.129")
#Topic /seoul/yuokok 에 문자값 multiple 1, multiple 2를 발행한다.