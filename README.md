## Chat2QQ+
Chat2QQ+ 是 [Chat2QQ](https://github.com/DreamVoid/Chat2QQ) 的分支, 用于添加开发者不接受合并的功能. 

可以点击右侧 `Releases` 按钮下载jar文件. 对于新添加的功能请在这里提问(而不是Chat2QQ). 

如果你有好的想法请点击上方 `Issues` 按钮, 发送一个功能请求. 如果我能做到就会去实现出来. 

---

**功能列表**
- 执行指令
```yaml
  # 在QQ群中运行指令
  run-command:
    # 不使用时请关闭
    enabled: false
    # 启用此功能的QQ群, 需要同时设置在 bot.group-ids
    qq-group:
      - 1000001
    # 指令前缀, 可以是多个字符, 比如 "~$"
    prefix: "/"
    # 指令最大长度 (不包括指令前缀)
    command-max-length: 255
    # 是否发送指令的输出, 关闭可提高性能或解决一些兼容性问题
    return: true
    # 等待指令运行多长时间再处理输出 (毫秒), 需要开启 return
    # 如果需要执行较慢的指令, 请尝试增大此值
    sleep: 300
    # 是否同时将指令作为消息转发到服务器中
    also-as-message: true
    # 是否为执行不在白名单中的指令发送返回消息
    message-miss: "未命中的指令"
    # 运行无返回指令的消息
    message-no-out: "运行无返回指令"
    # 获取指令的正则表达式, 当第一个捕获组的内容与指令白名单中的匹配时则允许运行 (不带斜杠或前缀)
    regex-command-main: "^([^ ]+)"
    # 设置各组可执行的主命令白名单 (不带斜杠或前缀)
    # 权限更高的用户将可以使用更低的用户的指令
    # 如果添加一条 `___ALL_COMMAND___` 作为指令, 则表示此组可以使用所有指令, 此功能请勿随意使用 ! (同时我不建议使用它)
    group:
      # permission_<int> 是 MiraiMC 获取到的权限数字, 以后更新了其他权限只需要以此格式添加即可使用
      permission_2: # 群主
        - 指令
      permission_1: # 管理员
        - 指令
      permission_0: # 成员
        - 指令
```
