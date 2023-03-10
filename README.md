## Chat2QQ+


- **ð§ è¯´æ**
  - [ApliNi/Chat2QQ](https://github.com/ApliNi/Chat2QQ) æ¯åå« [DreamVoid/Chat2QQ](https://github.com/DreamVoid/Chat2QQ) å ä¹ææåè½çæä»¶, ç»è¿éåå¹¶æ·»å äºè®¸å¤åè½.
  - å¦æä½ æä»»ä½æ³æ³è¯·ç¹å» [`Issues`](https://github.com/ApliNi/Chat2QQ/issues), æå¼ä¸ä¸ªåè½è¯·æ±. å¦ææè½åå°å°±ä¼å»å®ç°åºæ¥. 
<p></p>

- **ð¥ ä¸è½½**
  - å¨ [`Releases`](https://github.com/ApliNi/Chat2QQ/releases) ä¸­ä¸è½½ææ°çjaræä»¶.
  - å¦æéè¦å¼åçæ¬, å¯ä»¥ç¹å» [`Actions`](https://github.com/ApliNi/Chat2QQ/actions), æå¼ç¬¬ä¸ä¸ª, æ¾å°æä¸é¢çjaræä»¶. 
<p></p>

- **â° å¶ä»**
  - è®¢éæ´æ°: ç¹å»å³ä¸è§ `Watch` æé®, éæ© `Custom` ä¸­ç `Releases` !
  - ä½¿ç¨éç»è®¡: [bStats](https://bstats.org/plugin/bukkit/ApliNi-Chat2QQ/17587)
<p></p>


<br />

> **â¨ æ°å¢åè½åè¡¨ (ç¹å»å±å¼)**

[[æ¥çå®æ´éç½®]](https://github.com/ApliNi/Chat2QQ/blob/main/src/main/resources/config.yml)

<details><summary>æ§è¡æä»¤</summary>

```yaml
  # å¨QQç¾¤ä¸­è¿è¡æä»¤ [éè¦åç¬æ·»å  QQç¾¤]
  # æ­¤æ¨¡åä¸å¤çé»åå blacklist
  run-command:
    enabled: false
    # å¯ç¨ç QQç¾¤
    qq-group:
      - 1000001

    # æä»¤åç¼, å¯ä»¥æ¯å¤ä¸ªå­ç¬¦, æ¯å¦ "~$"
    command-prefix: '/'
    # æä»¤æå¤§é¿åº¦ (ä¸åæ¬æä»¤åç¼)
    command-max-length: 255
    # è·åæä»¤çæ­£åè¡¨è¾¾å¼, å½ç¬¬ä¸ä¸ªæè·ç»çåå®¹ä¸æä»¤ç½ååä¸­çå¹éæ¶ååè®¸è¿è¡ (ä¸å¸¦ææ æåç¼)
    regex-command-main: '^([^ ]+)'
    # æ¯å¦å°ä¸»å½ä»¤è½¬æ¢ä¸ºå°ååæ§è¡
    always-lowercase: false

    # æ¯å¦åéæä»¤çè¾åº, å³é­å¯æé«æ§è½æè§£å³ä¸äºå¼å®¹æ§é®é¢
    return: true
    # ç­å¾æä»¤è¿è¡å¤é¿æ¶é´åå°ç»æåéå°QQç¾¤ (æ¯«ç§), éè¦å¼å¯ run-command.return
    return-sleep: 300
    # æ¯å¦å°æä»¤çè¾åºæå°å°æ§å¶å°åæ¥å¿
    return-log: true

    # æ§è¡ä¸å¨ç½ååä¸­çæä»¤æ¶åéè¿åæ¶æ¯
    message-miss: 'æªå½ä¸­çæä»¤'
    # è¿è¡æ è¿åæä»¤çæ¶æ¯
    message-no-out: 'è¿è¡æ è¿åæä»¤'

    # è®¾ç½®åç»å¯æ§è¡çä¸»å½ä»¤ç½åå (ä¸å¸¦ææ æåç¼)
    # æéæ´é«çç¨æ·å°å¯ä»¥ä½¿ç¨æ´ä½çç¨æ·çæä»¤
    # å¦ææ·»å ä¸æ¡ ___ALL_COMMAND___ ä½ä¸ºæä»¤, åè¡¨ç¤ºæ­¤ç»å¯ä»¥ä½¿ç¨æææä»¤, æ­¤åè½è¯·å¿éæä½¿ç¨ !
    group:
      # permission_<int> æ¯ MiraiMC è·åå°çæéæ°å­, ä»¥åæ´æ°äºå¶ä»æéåªéè¦ä»¥æ­¤æ ¼å¼æ·»å å³å¯ä½¿ç¨
      permission_2: # ç¾¤ä¸»
      #- chat2qq
      permission_1: # ç®¡çå
      #- spark
      permission_0: # æå
      #- list
      #- tps
```

</details>


<details><summary>å¹éæ¸¸æå</summary>

```yaml
  # ä» ç¾¤åç(%nick%) ä¸­å¹é MC å¯ç¨çæ¸¸æåç§°
  # æ·»å åé: %regex_nick% - ä½¿ç¨æ­£åå¹éå°çåç§°, éè¦å¼å¯ cleanup-name åè½
  cleanup-name:
    enabled: false
    # ç¨åºåç¬¬ä¸ä¸ªæè·ç»çç»æ
    regex: '([a-zA-Z0-9_]{3,16})'
    # å¦æå¹éä¸å°, åä½¿ç¨ä»¥ä¸å­ç¬¦ä¸²
    # %nick% - ç¾¤åç
    # %qq% - qqå·
    not-captured: '%nick%'
```

</details>

<details><summary>æ¶æ¯é¢å¤ç</summary>

```yaml
  # é¢å¤ç %message% ä¸­çæ¶æ¯
  pretreatment:
    enabled: true
    # **ä½¿ç¨æ¹æ³**
    # list:
    #   - å¹éæ¹å¼: prefix (åç¼å¹é), å¯ç¨å¤çæ¹å¼: to_all
    #                                           to_replace
    #             contain (åå«), å¯ç¨å¤çæ¹å¼: to_all
    #                                        to_replace
    #             equal (å®å¨ç¸ç­), å¯ç¨å¤çæ¹å¼: to_all
    #             regular (æ­£åå¹é), å¯ç¨å¤çæ¹å¼: to_all
    #                                           to_regular
    #     å¤çæ¹å¼: to_all (æ¿æ¢æ´æ¡æ¶æ¯)
    #             to_replace (æ¿æ¢å¹éå°çé¨å)
    #             to_regular (ä½¿ç¨æ­£åæ¿æ¢, å¯ä½¿ç¨æ­£ååé)
    #
    #     æ¯å¦åé: send (å¡«å send éç½®å°åæ¶è½¬åéå¹éå°çæ¶æ¯, ä¸éè¦æ¶è¯·å¿½ç¥)
    #
    # > æ­£åçæ§è½è¾å·®, è¯·å°½éé¿åä½¿ç¨å¾å¤æ­£å
    # ç¤ºä¾éç½®, é»è®¤éç½®äºä¸äºå¯è½æç¨çåè½:
    list:

      # å¡çæ¶æ¯, JSON
      - prefix: '{"app":"com.tencent.'
        to_all: '[å¡çæ¶æ¯]'

      # ç¾¤å¬å, JSON
      - prefix: '{"app":"com.tencent.mannounce"'
        to_all: '[ç¾¤å¬å]'

      # è§é¢, å­ç¬¦ä¸²
      - prefix: 'ä½ çQQæä¸æ¯ææ¥çè§é¢ç­ç'
        to_all: '[è§é¢]'

      # æä»¶, å­ç¬¦ä¸², è°æ´æ ¼å¼
      - prefix: '[æä»¶]'
        to_replace: '[æä»¶] '

      # è§£å³ Emoji æ¾ç¤ºä¸ºæªç¥å­ç¬¦
      - regular: '[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]'
        to_regular: '[Emoji]'
```

</details>


<details><summary>é¢è®¾çæ ¼å¼è°æ´éç½®</summary>

```yaml
  # é¢è®¾çæ ¼å¼è°æ´åè½
  other-format-presets:
    # æ¯å¦å é¤ %message% æ¶æ¯ ä¸­çæ ¼å¼åå­ç¬¦
    render-message_format-code: false
    # å é¤ %message% æ¶æ¯ ååçç©ºæ ¼åç©ºè¡
    message-trim: true
    # æ¯å¦å é¤ %nick% ç¾¤åç ä¸­çæ ¼å¼åå­ç¬¦
    render-nick_format-code: true

    # èå¤©æ¶æ¯è¿é¿æ¶è½¬æ¢ä¸ºæ¬æµ®ææ¬
    long-message:
      enabled: true
      # ä»¥ä¸ä»»æä¸ä¸ªæ¡ä»¶æç«æ¶è¢«å¤å®ä¸ºé¿æ¶æ¯, è¥éåæ¶ä¸ä¸ª, è¯·æ¹ä¸ºå¾å¤§çæ°
      # æ¡ä»¶1: æ¶æ¯é¿åº¦è¾¾å°æ­¤å¼
      condition-length: 210
      # æ¡ä»¶2: æ¢è¡æ°éè¾¾å°æ­¤å¼, å¨ message-trim ä¹åè¿è¡
      condition-line_num: 6
      # æ¾ç¤ºä¸º
      message: 'Â§f[Â§7é¿æ¶æ¯Â§f]'

    # æ¯å¦å¯ç¨ "æ´å¥½çå¤è¡æ¶æ¯"
    multiline-message:
      enabled: true
      line-0: '' # [å¤è¡æ¶æ¯]
      line-prefix: '  '

    # æ¯å¦å°èå¤©æ¶æ¯è½¬åå°æ§å¶å°/æ¥å¿
    message-to-log: true
```

</details>

<details><summary>å¼ç¨åå¤</summary>

```yaml
  # å¼ç¨åå¤
  # æ·»å åé: %_reply_%
  # å¦ææ¯åå¤æ¶æ¯, åä¸ºåéèµå¼å¹¶ä¸ºæ¶æ¯æ·»å æ¬æµ®ææ¬æ¡ç¨äºæ¾ç¤ºåå®¹. å¯ä»¥å°é¼ æ æ¬åå¨æ¶æ¯ä¸æ¥çåå¤çåå®¹
  reply-message:
    # å¯ç¨åé:
    # %qq% - è¢«åå¤çæ¶æ¯çåéèQQå·
    # %c_name% - ç¾¤åç - éè¦å¼å¯ aplini.format-qq-id
    var: 'Â§f[Â§7åå¤ @%c_name%Â§f] '

    # å¯ç¨åé:
    # %_/n_% - æ¢è¡
    # %qq% - è¢«åå¤çæ¶æ¯çåéèQQå·
    # %c_name% - ç¾¤åç - éè¦å¼å¯ aplini.format-qq-id
    # %message% - åå¤åå®¹
    # %main_message% - å½åæ¶æ¯çå®æ´åå®¹
    message: 'Â§f[Â§7å¼ç¨ @%c_name%Â§f] Â§7%message%Â§r%_/n_%%_/n_%Â§f%main_message%'

    # å é¤éå¤@ :: å¦æå¼ç¨åå¤å¯¹è±¡ç­äºæ¶æ¯å¼å¤´ç@å¯¹è±¡, åå é¤æ¶æ¯å¼å¤´ç @
    del-duplicates-at: true
```

</details>

<details><summary><code>/qchat</code></summary>

```yaml
  # åéæ¶æ¯çæä»¤
  # /qchat <æ¶æ¯>  - ç©å®¶ä½¿ç¨æ­¤æä»¤
  # /qchat [èªå®ä¹åç§°] <æ¶æ¯>  - éç©å®¶å®ä½æå¶ä»ç¨åºä½¿ç¨æ­¤æä»¤
  qchat:
    # ä½¿ç¨ä¸æ¹ general.group-ids ä¸­éç½®çç¾¤
    use-general-group-ids: true
    # æ¶æ¯è½¬åå°åªäºç¾¤, éè¦ use-general-group-ids: false
    group-ids:
      - 1000000
    # éç©å®¶ æ§è¡æä»¤æ¶å¿½ç¥ [èªå®ä¹åç§°] åæ°
    use-fill-name: false
    # å¦æè·åä¸å°åç§° (å¼å¯ auto-other-name æåæ°ä¸è¶³) åä½¿ç¨ä»¥ä¸è®¾å®å¼
    fill-name: 'æ§å¶å°'
    # è½¬åå°QQç¾¤çæ ¼å¼
    # %name% - ç©å®¶åç§°æèªå®ä¹åç§°æ fill-other-name
    # %message% - æ¶æ¯
    qq-format: '[%name%] %message%'
    # æ¯å¦åæ¶å°æ¶æ¯å¹¿æ­å°MCæå¡å¨
    mc-broadcast: true
    # å¹¿æ­å°MCæå¡å¨ç
    mc-format: 'Â§f[Â§7%name%Â§f] Â§r%message%'
```

</details>

<details><summary>[åç½®] ç¾¤æåä¿¡æ¯ç¼å­</summary>

```yaml
  # [åç½®] ç¾¤æåä¿¡æ¯ç¼å­, æµè¯åè½
  player-cache:
    # å¨æå®æºå¨äººç»å½æ¶è¿è¡æ­¤ç¨åº
    enabled: true
    # èªå¨æ´æ°ç¼å­
    auto-update: true
    # æµè¯.èªå¨æ´æ°æ¶éå»ºææç¼å­æ°æ®
    #test-auto-update-all: false

    # ä½¿ç¨ä¸æ¹ general.group-ids ä¸­éç½®çç¾¤
    use-general-group-ids: true
    # ç¼å­åªäºç¾¤, éè¦ use-general-group-ids: false
    group-ids:
      - 1000000
```

</details>


<details><summary>ä½¿ç¨ @ç¾¤åç</summary>

```yaml
  # å° %message% ä¸­ç @qqID æ¿æ¢ä¸º @åç§°
  # éè¦å¼å¯ aplini.player-cache
  format-qq-id:
    enabled: true
    # ç¨äºå¹é @qqID çæ­£å
    regular: '(@[0-9]{5,11})'
    # æ ¼å¼
    # %qq% - qqå·
    # %name% - åç§°
    format: '[@%name%]'
    # æå¤å¹éå æ¬¡, é²æ­¢å·å±æµªè´¹æ§è½
    max-cycles-num: 7
```

</details>


<details><summary>äºä»¶ä»»å¡</summary>

```yaml
  # äºä»¶ä»»å¡
  event-func:
    # enable ä¿®æ¹åéè¦éå¯æå¡å¨
    enable: false
    # ä½¿ç¨ä¸æ¹ general.group-ids ä¸­éç½®çç¾¤
    use-general-group-ids: true
    # å¯ç¨å¨åªäºç¾¤, éè¦ use-general-group-ids: false
    group-ids:
      - 1000000

      # æ¯ä¸ªäºä»¶å¯ç¨çä»»å¡ä¸å, è¿éååºäºææä»»å¡çä½¿ç¨æ¹æ³:
      # - command: 'command' - åéæä»¤

      # - message-text: 'æ¶æ¯' - åäºä»¶æ¥æºåéæ¶æ¯, ç¾¤ æ å¥½å/ç§è

      # - message-group: 1000000 - åæå®ç¾¤åéæ¶æ¯
      #   message-text: 'æ¶æ¯'

      # - message-friend: 2000003 - åæå®å¥½ååéæ¶æ¯
      #   message-text: 'æ¶æ¯'

    MiraiMemberJoinEvent: # ç¾¤æåå å¥
      # å¯ä½¿ç¨: command, message-text, message-group
      # ä¸ä¸ªäºä»¶ä¸­å¯æ·»å å¤ä¸ªç¸åæä¸ç¸åçä»»å¡
      - message-text: 'æ¬¢è¿'
      - command: 'tps'
      - command: 'mspt'
      #- message-group: 1000000
      #  message-text: 'æ¶æ¯'

    MiraiMemberLeaveEvent: # æåéåº
    # å¯ä½¿ç¨: command, message-text, message-group
```

</details>


<details><summary>æçº¿éè¿</summary>

```yaml
  # æµè¯åè½ :: æçº¿éè¿
  bot-offline:
    # enable ä¿®æ¹åéè¦éå¯æå¡å¨
    enable: false
    # å¯ç¨åªäºæºå¨äºº, å¯æ·»å å¤ä¸ª, åªè½å¨è¿éæ·»å 
    bot-ids:
      - 2000000
      - 2000001

    # å»¶è¿éæ°è¿æ¥, ç§
    delay: 14
    # éæ°è¿æ¥å¤±è´¥éè¯æ¬¡æ°, è¶è¿åä¸åç»§ç»­éè¿
    max-reconnect-num: 7
```

</details>


<details><summary>èªå¨åå¤</summary>

```yaml
  # èªå¨åå¤
  # å½QQç¾¤ä¸­çæ¶æ¯å¹éæ¶åéèªå®ä¹æ¶æ¯
  auto-response:
    enable: true
    # ä½¿ç¨ä¸æ¹ general.group-ids ä¸­éç½®çç¾¤
    use-general-group-ids: true
    # åå¤åªäºç¾¤çæ¶æ¯, éè¦ use-general-group-ids: false
    group-ids:
      - 1000000
    # ä¸ºæ­¤åè½å¯ç¨PAPI, éè¦å®è£PAPIæä»¶
    enable-papi: false
    #

    # **ä½¿ç¨æ¹æ³**
    # list:
    #   - å¹éæ¹å¼: prefix (åç¼å¹é)
    #             contain (åå«)
    #             equal (å®å¨ç¸ç­)
    #             regular (æ­£åå¹é, send ä¸­å¯ä½¿ç¨æ­£ååé)
    #     send (åéçæ¶æ¯åå®¹)
    #
    # > æ­£åçæ§è½è¾å·®, è¯·å°½éé¿åä½¿ç¨å¾å¤æ­£å
    # !! è¯·å°å¿ä½¿ç¨æ­£åæ¼æ¥PAPIåé, å¦ææ­£åè®¾è®¡æé®é¢åå¯è½åºç°æ³¨å¥æ¼æ´ !!
    #    - æç¤º: åºæå®å¹éçå­ç¬¦èå´åæå°æå¤§æ¬¡æ°, è¦ç»å¯¹çé²æ­¢è¾å¥PAPIåéçä¿çç¬¦å·: %
    #      - æ¯å¦: - regular: '^\#ping ([a-zA-Z0-9_]{3,16})$'
    #               send: '$1 çå»¶è¿ä¸º: %player_ping_$1%ms'
    # ç¤ºä¾éç½®, é»è®¤éç½®äºä¸äºå¯è½æç¨çåè½:
    list:

      # ä½¿ç¨PAPIè·åå¨çº¿ç©å®¶æ°é, éè¦å¯ç¨ aplini.auto-response.enable-papi
      # PlayerList: /papi ecloud download playerlist
      - equal: '#list'
        send: 'å¨çº¿ç©å®¶: [%playerlist_online,normal,yes,amount%] \n%playerlist_online,normal,yes,list%'

      # ä½¿ç¨PAPIè·åæå¡å¨TPS, éè¦å¯ç¨ aplini.auto-response.enable-papi
      # Server: /papi ecloud download Server
      - equal: '#tps'
        send: 'TPS [1m, 5m, 15m]: %server_tps_1% / %server_tps_5% / %server_tps_15%'

      # æä»¤åè¡¨
      - equal: '#help'
        send: 'æä»¤åè¡¨: 
        \n    - #list - æ¾ç¤ºå¨çº¿ç©å®¶åè¡¨
        \n    - #tps - æ¾ç¤ºæå¡å¨TPS'

      # @ä¸ä¸ªQQå·æ¶åéæ¶æ¯
      - contain: '@2000000'
        send: 'OwO'
```

</details>


<details><summary>å¶ä»ä¿¡æ¯åå¼å®¹æ§</summary>

- [x] å¼å®¹å½ä»¤æ¹å
- [ ] å¼å®¹æ¨¡ç»æ?

</details>


<details><summary>æä»¤åæé</summary>

- `qchat [åç§°] <æ¶æ¯>` - ä½¿ç¨èªå®ä¹åç§°åéæ¶æ¯å°ç¾¤
- `qchat <æ¶æ¯>` - åéæ¶æ¯å°ç¾¤
- `chat2qq` - æä»¶ä¸»å½ä»¤ & å¸®å©ä¿¡æ¯
    - `chat2qq setgroupcacheall` - éæ°åå»ºç¾¤æåç¼å­

plugin.yml
```yaml
commands:
  qchat:
    description: åéèå¤©æ¶æ¯å°QQç¾¤
    permission: chat2qq.command.qchat
  chat2qq:
    description: Chat2QQ æä»¶ä¸»å½ä»¤
permissions:
  chat2qq.join.silent:
    description: åè®¸ææå å¥æå¡å¨
    default: false
  chat2qq.quit.silent:
    description: åè®¸ææç¦»å¼æå¡å¨
    default: false
  chat2qq.command.qchat:
    description: åè®¸ä½¿ç¨ /qchat
    default: op
  chat2qq.command.chat2qq:
    description: åè®¸ä½¿ç¨ /chat2qq
    default: op
  chat2qq:.command.setgroupcacheall:
    description: åè®¸ä½¿ç¨ /chat2qq setgroupcacheall
    default: op
```

</details>

