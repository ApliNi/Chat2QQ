## Chat2QQ+


- **🚧 说明**
  - [ApliNi/Chat2QQ](https://github.com/ApliNi/Chat2QQ) 是包含 [DreamVoid/Chat2QQ](https://github.com/DreamVoid/Chat2QQ) 几乎所有功能的插件, 经过重写并添加了许多功能.
  - 如果你有任何想法请点击 [`Issues`](https://github.com/ApliNi/Chat2QQ/issues), 打开一个功能请求. 如果我能做到就会去实现出来. 
<p></p>

- **🥡 下载**
  - 在 [`Releases`](https://github.com/ApliNi/Chat2QQ/releases) 中下载最新的jar文件.
  - 如果需要开发版本, 可以点击 [`Actions`](https://github.com/ApliNi/Chat2QQ/actions), 打开第一个, 找到最下面的jar文件. 
<p></p>

- **✨ 其他**
  - 订阅更新: 点击右上角 `Watch` 按钮, 选择 `Custom` 中的 `Releases` !
  - 使用量统计: [bStats](https://bstats.org/plugin/bukkit/ApliNi-Chat2QQ/17587)
<p></p>


<a href="https://bstats.org/plugin/bukkit/ApliNi-Chat2QQ/17587">![](https://bstats.org/signatures/bukkit/ApliNi-Chat2QQ.svg)</a>


<br />


### ✨ 图片

![image](https://user-images.githubusercontent.com/59365724/227127511-a149cc33-9683-4bd9-9640-fd938546859d.png)

![image](https://user-images.githubusercontent.com/59365724/227157788-d9259da8-46e6-438e-b32b-3926a2d3a9a3.png)
![image](https://user-images.githubusercontent.com/59365724/227157812-a0245843-66e9-44bc-8bc2-e0935ebc2d2b.png)

![image](https://user-images.githubusercontent.com/59365724/227128189-8f217293-04e9-472d-a09e-daad7eef79f0.png)
![image](https://user-images.githubusercontent.com/59365724/227128209-f6be73c2-efd1-4ca2-b85b-b2c2b84dad7d.png)

![image](https://user-images.githubusercontent.com/59365724/227128013-013e0514-771e-4075-8d4d-c28557cfa126.png)
![image](https://user-images.githubusercontent.com/59365724/227127980-628d662d-cc47-4468-9437-2e5298a6d6c3.png)

![image](https://user-images.githubusercontent.com/59365724/227158659-b35ac9b3-113e-4d3d-80d7-f214c8064b02.png)
![image](https://user-images.githubusercontent.com/59365724/227158697-45a16e51-f4b1-4f97-8296-03d4074d3efb.png)

![image](https://user-images.githubusercontent.com/59365724/227159155-8a731828-c929-44f8-b0c1-5f380da403cc.png)
![image](https://user-images.githubusercontent.com/59365724/227159055-02e5cdcd-3a5e-4c20-8fda-85b0df90062a.png)

![image](https://user-images.githubusercontent.com/59365724/227159023-7333b385-c687-45a4-965b-e9b066af0e6f.png)
![image](https://user-images.githubusercontent.com/59365724/227159083-e8d6b7e7-9ba6-41e8-9562-e7a174f87be5.png)

![image](https://user-images.githubusercontent.com/59365724/227128959-ff5fcdab-f09e-4ed8-bee9-08ade7cf7ef3.png)
![image](https://user-images.githubusercontent.com/59365724/227128919-092545b6-6d38-4324-b8b8-1903cb522a05.png)

![image](https://user-images.githubusercontent.com/59365724/227157070-c8fa51ca-b623-4167-a7ef-f02239d84aef.png)
![image](https://user-images.githubusercontent.com/59365724/227157097-d6b226e4-2158-4b3b-a36b-db21acfeff2e.png)

![image](https://user-images.githubusercontent.com/59365724/227129074-9a8316e1-8b8f-4abe-9e74-ad73f212f9ec.png)


### 完整配置和功能
```yaml

# 游戏内配置
# QQ -> MC 的消息
general:
  # 处理哪些QQ群的消息
  group-ids:
    - 1000000

  # 群聊天前缀 (聊天需要带有指定前缀才能发送到服务器)
  requite-special-word-prefix:
    enabled: false
    prefix:
      - '#'

  # 当群名片不存在时是否尝试获取昵称
  use-nick-if-namecard-null: true

  # QQ群消息广播到游戏内聊天的格式 格式化代码: §
  # %groupname% - 群名称
  # %groupid% - 群号
  # %nick% - 发送者群名片
  # %regex_nick% - 使用正则匹配到的名称, 需要开启 aplini.cleanup-name 模块
  # %qq% - 发送者QQ号
  # %message% - 消息内容, 支持预处理模块 aplini.pretreatment
  # %_reply_% - 如果是回复消息..., 配置在 aplini.reply-message 模块
  in-game-chat-format: '§f[§7%nick%§r§f] %_reply_%§7%message%'

  # 启用 MiraiMC 内置的QQ绑定
  use-miraimc-bind: false
  # 已绑定玩家的广播消息格式
  bind-chat-format: '§f[§7%nick%§r§f] %_reply_%§7%message%'



# 机器人配置
# MC -> QQ 的消息
bot:
  # 使用哪些QQ号处理消息
  # 只能添加一个
  bot-accounts:
    - 2000000

  # 玩家在以下世界中聊天才会被转发
  available-worlds:
    #- world
  # 将以上配置作为黑名单, 玩家不在以上世界中聊天才会被转发
  available-worlds-use-as-blacklist: true

  # 游戏聊天前缀 (聊天需要带有指定前缀才能发送到QQ群) 
  requite-special-word-prefix:
    enabled: true
    prefix:
      - '#'

  # 服务器消息发送到QQ群的格式
  # %player% - 玩家名称
  # %message% - 消息内容
  group-chat-format: '[%player%] %message%'


  # 是否发送玩家进出服务器的消息
  # %player% - 玩家显示昵称
  send-player-join-quit-message: false
  # 加入
  player-join-message: '%player% 进入服务器'
  # 退出
  player-quit-message: '%player% 离开服务器'
  # 防刷屏, 在此时间内多次进出服务器不会发送消息
  player-join-quit-message-interval: 0



# 黑名单, 可用于添加其他QQ机器人
# 优先级大于上方配置
blacklist:
  # 不转发以下QQ号的聊天消息
  qq:
  #- 2000001

  # 不转发以下玩家名的聊天消息
  player:
  #- playerName


# ############### #
# 以下为功能模块配置 #
# ############### #

aplini:

  ## 1
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
    # 判断指令返回为空的正则, 匹配多行文本
    return-isNull: '^\s*$'
    # 是否将主命令转换为小写再执行
    always-lowercase: false

    # 是否发送指令的输出, 关闭可提高性能或解决一些兼容性问题
    return: true
    # 等待指令运行多长时间再将结果发送到QQ群 (毫秒), 需要开启 run-command.return
    # 如果你遇到了一些提前输出类似 "正在运行...请稍等" 消息的插件, 可以在 pretreatment-command-message 中配置完全删除这条消息. 然后 return-sleep-min 保持不变 :)
    return-sleep-min: 14 # 最小等待时间
    return-sleep-max: 5346 # 最大等待时间, 如果一些长耗时指令没有输出请增大此值
    return-sleep-sampling-interval: 172 # 输出内容检查间隔, 如果经常执行长耗时指令可以增大此值
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


  ## 2
  # 从 群名片(%nick%) 中匹配 MC 可用的游戏名称
  # 添加变量: %regex_nick% - 使用正则匹配到的名称, 需要开启 cleanup-name 功能
  cleanup-name:
    enabled: false
    # 程序取第一个捕获组的结果
    regex: '([a-zA-Z0-9_]{3,16})'
    # 如果匹配不到, 则使用以下字符串
    # %nick% - 群名片
    # %qq% - qq号
    not-captured: '%nick%'


  ## 3
  # 预处理 %message% 中的消息
  pretreatment:
    enabled: true
    # **使用方法**
    # list:
    #   - 匹配方式: prefix (前缀匹配), 处理方式: to_all, to_replace
    #              contain (包含), 处理方式: to_all, to_replace
    #              equal (完全相等), 处理方式: to_all
    #              regular (正则匹配), 处理方式: to_all, to_regular
    #
    #     处理方式: to_all (替换整条消息)
    #              to_replace (替换匹配到的部分)
    #              to_regular (使用正则替换, 可使用正则变量)
    #
    #     是否发送: send (填写 send 配置将取消转发送匹配到的消息, 不需要时请忽略)

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

      # 示例: 取消发送包含此内容的消息
      #- contain: '此内容'
      #  send: false

      # 示例: 更好的匹配卡片消息, 使用正则解析内容. 这里的正则有点问题, 仅示范, 不采用
      #- regular: '\{"app":"com.tencent.miniapp(?:.*)"prompt":"(?:.*?)](.*?)","(?:.*)"desc":"(.*?)","(?:.*)' # miniApp
      #  to_regular: '[小程序: $1] $2' # [小程序: 哔哩哔哩] xxxxxxx


  ## 3.1
  # 按行预处理指令返回消息, 用于处理返回到QQ群的消息
  pretreatment-command-message:
    enabled: true
    # 使用方法: 如上
    list:
      # 删除格式化字符
      - regular: '§[a-z0-9]'
        to_regular: ''

      # 示例: co插件翻页消息处理
      #- regular: '◀? ?第 (.*) 页 ▶? ?\((.*)\)'
      #  to_regular: '第 $1 页,  使用 /co page <页码> 翻页'


  ## 3.2
  # 按多行文本预处理指令返回消息
  # 可使用占位符:
  #   - %command% :: 用户运行的指令原文(不带斜杠/前缀)
  #   - %time% :: 指令运行耗时
  #   - %qq% :: 执行指令的qq号
  #   - %group% :: 执行指令的群号
  pretreatment-command-message-all:
    enabled: false
    enabled-placeholder: false # 关闭占位符可提高性能
    # 使用方法: 如上
    list:
      # 示例: 显示指令运行时间, 需要开启占位符
      #- regular: '([\s\S]+)'
      #  to_regular: '$1\n  - 运行耗时: %time%ms'


  ## 4
  # 预设的格式调整功能
  other-format-presets:
    # 是否删除 %message% 消息 中的格式化字符
    render-message_format-code: false
    # 删除 %message% 消息 前后的空格和空行
    message-trim: true
    # 是否删除 %nick% 群名片 中的格式化字符
    render-nick_format-code: true

    # 聊天消息过长时转换为悬浮文本
    long-message:
      enabled: true
      # 以下任意一个条件成立时被判定为长消息, 若需取消一个, 请改为很大的数
      # 条件1: 消息长度达到此值
      condition-length: 210
      # 条件2: 换行数量达到此值, 在 message-trim 之后运行
      condition-line_num: 6
      # 显示为
      message: '§f[§7长消息§f]'

    # 是否启用 "更好的多行消息"
    multiline-message:
      enabled: true
      line-0: '' # [多行消息]
      line-prefix: '  '

    # 是否将聊天消息转发到控制台/日志
    message-to-log: true


  ## 5
  # 引用回复
  # 添加变量: %_reply_%
  # 如果是回复消息, 则为变量赋值并为消息添加悬浮文本框用于显示内容. 可以将鼠标悬停在消息上查看回复的内容
  reply-message:
    # 可用变量:
    # %qq% - 被回复的消息的发送者QQ号
    # %c_name% - 群名片 - 需要开启 aplini.format-qq-id
    var: '§f[§7回复 @%c_name%§f] '

    # 可用变量:
    # %_/n_% - 换行
    # %qq% - 被回复的消息的发送者QQ号
    # %c_name% - 群名片 - 需要开启 aplini.format-qq-id
    # %message% - 回复内容
    # %main_message% - 当前消息内容
    message: '§f[§7引用 @%c_name%§f]%_/n_%§7%message%§r%_/n_%%_/n_%§f——%main_message%'

    # 删除重复@ :: 如果引用回复对象等于消息开头的@对象, 则删除消息开头的 @
    del-duplicates-at: true


  ## 6
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
    mc-format: '§f[§7%name%§f] §7%message%'


  ## 7
  # [前置] 群成员信息缓存, 测试功能
  # ! 需要开启 MiraiMC 配置中的 bot.contact-cache.enable-group-member-list-cache
  player-cache:
    # 在指定机器人登录时运行此程序
    enabled: true
    # 在玩家群名片修改时更新内存中的数据
    auto-update: true
    # 使用上方 general.group-ids 中配置的群
    use-general-group-ids: true
    # 缓存哪些群, 需要 use-general-group-ids: false
    group-ids:
      - 1000000
    # MiraiMC 群缓存文件路径, 如果你修改了插件目录相关的配置, 才需要修改它
    # %qq% - 机器人账号
    # %group% - 群号
    mirai-cache-path: "plugins/MiraiMC/MiraiBot/bots/%qq%/cache/contacts/groups/%group%.json"


  ## 8
  # 将 %message% 中的 @qqID 替换为 @名称
  # 需要开启前置: aplini.player-cache
  format-qq-id:
    enabled: true
    # 用于匹配 @qqID 的正则
    regular: '(@[0-9]{5,11})'
    # 格式
    # %qq% - qq号
    # %name% - 名称
    format: '§f[§7@%name%§f]§7'
    # 一条消息最多匹配几次, 防止刷屏浪费性能
    max-cycles-num: 10


  ## 9
  # 事件任务
  event-func:
    # enable 修改后需要重启服务器
    enable: false
    # 使用上方 general.group-ids 中配置的群
    use-general-group-ids: true
    # 启用在哪些群, 需要 use-general-group-ids: false
    group-ids:
      - 1000000

    # 每个事件可用的任务不同, 这里列出了所有任务的使用方法:
      # - command: 'command' - 发送指令

      # - message-text: '消息' - 向事件来源发送消息, 群 或 好友/私聊

      # - message-group: 1000000 - 向指定群发送消息
      #   message-text: '消息'

      # - message-friend: 2000003 - 向指定好友发送消息
      #   message-text: '消息'

    MiraiMemberJoinEvent: # 群成员加入
      # 可使用: command, message-text, message-group
      # 一个事件中可添加多个相同或不相同的任务
      - message-text: '欢迎'
      - command: 'tps'
      - command: 'mspt'
      #- message-group: 1000000
      #  message-text: '消息'

    MiraiMemberLeaveEvent: # 成员退出
      # 可使用: command, message-text, message-group


  ## 10
  # 测试功能 :: 掉线重连
  bot-offline:
    # enable 修改后需要重启服务器
    enable: false
    # 启用哪些机器人, 可添加多个, 只能在这里添加
    bot-ids:
      - 2000000
      - 2000001

    # 延迟重新连接, 秒
    delay: 14
    # 重新连接失败重试次数, 超过后不再继续重连
    max-reconnect-num: 7


  ## 11
  # 自动回复
  # 当QQ群中的消息匹配时发送自定义消息
  auto-response:
    enable: true
    # 使用上方 general.group-ids 中配置的群
    use-general-group-ids: true
    # 回复哪些群的消息, 需要 use-general-group-ids: false
    group-ids:
      - 1000000
    # 为此功能启用PAPI, 需要安装PAPI插件
    enable-papi: false

    # **使用方法**
    # list:
    #   - 匹配方式: prefix (前缀匹配)
    #             contain (包含)
    #             equal (完全相等)
    #             regular (正则匹配, send 中可使用正则变量)
    #     send (发送的消息内容)
    #
    # > 正则的性能较差, 请尽量避免使用很多正则
    # !! 请小心使用正则拼接PAPI变量, 如果正则设计有问题则可能出现注入漏洞 !!
    #    - 提示: 应指定匹配的字符范围和最小最大次数, 要绝对的防止输入PAPI变量的保留符号: %
    #      - 比如: - regular: '^\#ping ([a-zA-Z0-9_]{3,16})$'
    #               send: '$1 的延迟为: %player_ping_$1%ms'
    # 示例配置, 默认配置了一些可能有用的功能:
    list:

      # 使用PAPI获取在线玩家数量, 需要启用 aplini.auto-response.enable-papi
      # PlayerList: /papi ecloud download playerlist
      - equal: '#list'
        send: '在线玩家: [%playerlist_online,normal,yes,amount%] \n%playerlist_online,normal,yes,list%'

      # 使用PAPI获取服务器TPS, 需要启用 aplini.auto-response.enable-papi
      # Server: /papi ecloud download Server
      - equal: '#tps'
        send: 'TPS [1m, 5m, 15m]: %server_tps_1% / %server_tps_5% / %server_tps_15%'

      # 指令列表
      - equal: '#help'
        send: '指令列表: 
        \n    - #list - 显示在线玩家列表
        \n    - #tps - 显示服务器TPS'

      # @一个QQ号时发送消息
      - contain: '@2000000'
        send: 'OwO'


# <- 至此, 您已经完成了所有配置, 部分功能使用 /chat2qq reload 重载插件即可应用 uwu

# 使用量统计, 我不建议你关闭它
allow-bStats: true

```

### 其他信息和兼容性

- [x] 兼容命令方块
- [ ] 兼容模组服?




### 指令和权限

- `qchat [名称] <消息>` - 使用自定义名称发送消息到群
- `qchat <消息>` - 发送消息到群
- `chat2qq` - 插件主命令 & 帮助信息
    - `chat2qq setgroupcacheall` - 重新创建群成员缓存

plugin.yml
```yaml
commands:
  qchat:
    description: 发送聊天消息到QQ群
    permission: chat2qq.command.qchat
  chat2qq:
    description: Chat2QQ 插件主命令
permissions:
  chat2qq.join.silent:
    description: 允许悄悄加入服务器
    default: false
  chat2qq.quit.silent:
    description: 允许悄悄离开服务器
    default: false
  chat2qq.command.qchat:
    description: 允许使用 /qchat
    default: op
  chat2qq.command.chat2qq:
    description: 允许使用 /chat2qq
    default: op
  chat2qq:.command.setgroupcacheall:
    description: 允许使用 /chat2qq setgroupcacheall
    default: op
```
