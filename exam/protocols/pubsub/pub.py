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
#Topic /seoul/yuokok �� ���ڰ� multiple 1, multiple 2�� �����Ѵ�.