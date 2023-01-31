## Chat2QQ+

> **说明**

Chat2QQ+ 是 [Chat2QQ](https://github.com/DreamVoid/Chat2QQ) 的分支, 用于添加我需要的功能.

如果你有好的想法请点击上方 `Issues` 按钮, 发送一个功能请求. 如果我能做到就会去实现出来. 

! 注意, 此版本不兼容 Bukkit 服务器, 最低需要使用 1.18.1 版本的 Spigot (Chat2QQ-1.7.4 及以下版本支持 MC1.13.2). 

! 我正在考虑重写它, 完成后将添加连接 mirai-api-http 的功能(而非MC服务器中的MiraiMC), 并使用 GPLv3.0 开源协议(如果可以). 



> **下载**

可以点击右侧 `Releases` 按钮下载jar文件. 对于新添加的功能请在这里提问(而不是 DreamVoid/Chat2QQ).

如果需要开发版(包含一些未发布的功能和BUG), 可以点击上方 `Actions`, 选择第一个, 找到最下面的jar文件. 



> **订阅更新**

如果想要订阅更新, 请点击右上角 `Watch` 按钮, 选择 `Custom` 中的 `Releases` !

---

**功能列表**

- 执行指令
```yaml
  # 在QQ群中运行指令 [需要单独添加 QQ群]
  # 此模块不处理黑名单 blacklist
  run-command:
    enabled: false
    # 启用的 QQ群
    qq-group:
      - 1000001

    # 指令前缀, 可以是多个字符, 比如 "~$"
    command-prefix: '/'
    # 指令最大长度 (不包括指令前缀)
    command-max-length: 255
    # 获取指令的正则表达式, 当第一个捕获组的内容与指令白名单中的匹配时则允许运行 (不带斜杠或前缀)
    regex-command-main: '^([^ ]+)'

    # 是否发送指令的输出, 关闭可提高性能或解决一些兼容性问题
    return: true
    # 等待指令运行多长时间再将结果发送到QQ群 (毫秒), 需要开启 run-command.return
    return-sleep: 300
    # 是否将指令的输出打印到控制台和日志
    return-log: true

    # 执行不在白名单中的指令时发送返回消息
    message-miss: '未命中的指令'
    # 运行无返回指令的消息
    message-no-out: '运行无返回指令'

    # 设置各组可执行的主命令白名单 (不带斜杠或前缀)
    # 权限更高的用户将可以使用更低的用户的指令
    # 如果添加一条 ___ALL_COMMAND___ 作为指令, 则表示此组可以使用所有指令, 此功能请勿随意使用 !
    group:
      # permission_<int> 是 MiraiMC 获取到的权限数字, 以后更新了其他权限只需要以此格式添加即可使用
      permission_2: # 群主
      #- chat2qq
      permission_1: # 管理员
      #- spark
      permission_0: # 成员
      #- list
      #- tps
```

- 匹配游戏名
```yaml
  # 从 群名片(%nick%) 中匹配 MC 可用的游戏名称
  # 添加变量: %regex_nick% - 使用正则匹配到的名称, 需要开启 cleanup-name 功能
  cleanup-name:
    enabled: false
    # 程序取第一个捕获组的结果
    regex: '([a-zA-Z0-9_]{3,16})'
    # 如果匹配不到, 则使用 %nick%, 也可以替换为其他 %regex_nick% 以外的变量或字符串
    not-captured: '%nick%'
```

- 消息预处理
```yaml
  # 预处理 %message% 中的消息
  pretreatment:
    enabled: true
    # **使用方法**
    # list:
    #   - 匹配方式: prefix (前缀匹配), 可用处理方式: to_all
    #                                           to_replace
    #             contain (包含), 可用处理方式: to_all
    #                                        to_replace
    #             equal (完全相等), 可用处理方式: to_all
    #             regular (正则匹配), 可用处理方式: to_all
    #                                           to_regular
    #     处理方式: to_all (替换整条消息)
    #             to_replace (替换匹配到的部分)
    #             to_regular (使用正则替换, 可使用正则变量)
    #
    #     是否发送: send (填写 send 配置将取消转发送匹配到的消息, 不需要时请忽略)
    #
    # > 正则的性能较差, 请尽量避免使用很多正则
    # 示例配置, 默认配置了一些可能有用的功能:
    list:

      # 卡片消息, JSON
      - prefix: '{"app":"com.tencent.'
        to_all: '[卡片消息]'

      # 群公告, JSON
      - prefix: '{"app":"com.tencent.mannounce"'
        to_all: '[群公告]'

      # 视频, 字符串
      - prefix: '你的QQ暂不支持查看视频短片'
        to_all: '[视频]'

      # 文件, 字符串, 调整格式
      - prefix: '[文件]'
        to_replace: '[文件] '

      # 解决 Emoji 显示为未知字符
      - regular: '[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]'
        to_regular: '[Emoji]'
```

- 预设格式调整配置
```yaml
  # 预设的格式调整功能
  other-format-presets:
    # 是否删除 %message% 消息 中的格式化字符
    render-message_format-code: false
    # 是否删除 %nick% 群名片 中的格式化字符
    render-nick_format-code: true

    # 是否启用 "更好的多行消息"
    multiline-message:
      enabled: true
      line-0: '' # [多行消息]
      line-prefix: '  '

    # 是否将聊天消息转发到控制台/日志
    message-to-log: true
```

- 引用回复
```yaml
  # 引用回复
  # 添加变量: %_reply_%
  # 如果是回复消息, 则为变量赋值并为消息添加悬浮文本框用于显示内容. 可以将鼠标悬停在消息上查看回复的内容
  reply-message:
    # 可用变量:
    # %qq% - 被回复的消息的发送者QQ号
    var: '§f[§7回复 @%qq%§f] '

    # 可用变量:
    # %_/n_% - 换行
    # %qq% - 被回复的消息的发送者QQ号
    # %message% - 回复内容
    message: '§f[引用回复 @%qq%]%_/n_%§7%message%'
```

- `qchat` 指令
```yaml
  # 发送消息的指令
  # /qchat <消息>  - 玩家使用此指令
  # /qchat [自定义名称] <消息>  - 非玩家实体或其他程序使用此指令
  qchat:
    # 使用上方 general.group-ids 中配置的群
    use-general-group-ids: true
    # 消息转发到哪些群, 需要 use-general-group-ids: false
    group-ids:
      - 1000000
    # 非玩家 执行指令时忽略 [自定义名称] 参数
    use-fill-name: false
    # 如果获取不到名称 (开启 auto-other-name 或参数不足) 则使用以下设定值
    fill-name: '控制台'
    # 转发到QQ群的格式
    # %name% - 玩家名称或自定义名称或 fill-other-name
    # %message% - 消息
    qq-format: '[%name%] %message%'
    # 是否同时将消息广播到MC服务器
    mc-broadcast: true
    # 广播到MC服务器的
    mc-format: '§f[§7%name%§f] §r%message%'
```