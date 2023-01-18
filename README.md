## Chat2QQ+
Chat2QQ+ 是 [Chat2QQ](https://github.com/DreamVoid/Chat2QQ) 的分支, 用于添加开发者不接受合并的功能. 

您可以点击右侧 `Releases` 按钮下载jar文件. 对于新添加的功能请在这里提问(而不是Chat2QQ).

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
    sleep: 500
    # 运行无返回指令的消息
    message-no-out: "运行无返回指令"
    # 设置各组可执行的指令
    # 消息以设定的指令开头则作为指令运行, 因此可以精确到指令参数
    # 权限更高的用户将可以使用更低的用户的指令
    group:
      OWNER: # 群主
        - 指令
      ADMINISTRATOR: # 管理员
        - 指令
      MEMBER: # 成员 (默认组)
        - 指令
```
