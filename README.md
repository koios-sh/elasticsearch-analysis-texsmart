# elasticsearch-analysis-texsmart
TexSmart Analyzer for ElasticSearch

此分词器基于腾讯AI实验室[TexSmart中文分词库](https://ai.tencent.com/ailab/nlp/texsmart)，提供了ES中文分词插件.

🚩 更新日志：
1. 适配Elasticsearch 7.x版本，TexSmart-0.1.3版本

----------

版本对应
----------

| Plugin version | Elastic version |
| :------------- | :-------------- |
| master         | 7.x             |
| 7.6.2          | 7.6.2           |


安装步骤
----------

### 1. 下载安装ES对应Plugin Release版本

安装方式：

方式一

   a. 下载对应的release安装包，最新release包可从baidu盘下载（链接:https://pan.baidu.com/s/1mFPNJXgiTPzZeqEjH_zifw  密码:i0o7）
   
   b. 执行如下命令安装，其中PATH为插件包绝对路径：
   
   `./bin/elasticsearch-plugin install file://${PATH}`
   
方式二

   a. 使用elasticsearch插件脚本安装command如下：
   
   `./bin/elasticsearch-plugin install https://github.com/github.com/koios-sh/elasticsearch-analysis-texsmart/releases/download/v7.6.2/elasticsearch-analysis-hanlp-7.6.2.zip`

方式三

   a. 编译：sh build.sh
   b. 执行如下命令安装，其中PATH为插件包绝对路径：
      
      `./bin/elasticsearch-plugin install file://${PATH}`

### 2. 安装数据包

release包中不包含TexSmart数据包，若要下载完整版数据包，请查看[TexSmart Release](https://ai.tencent.com/ailab/nlp/texsmart/zh/download.html)。

数据包目录：/etc/elasticsearch/texsmart/data
可以修改config/texsmart.properties文件中的path值，调整数据路径

### 3. 安装libtencent_ai_texsmart.so

cp libtencent_ai_texsmart.so /usr/lib64 && chmod 777 /usr/lib64/libtencent_ai_texsmart.so

**注：每个节点都需要做上述更改**

提供的分词方式说明
----------

texsmart: texsmart默认分词

texsmart_standard: 标准分词

texsmart_index: 索引分词

样例
----------

```text
POST http://localhost:9200/test/_analyze
{
    "text": "2020年，空调市场“冷风吹过”",
    "tokenizer": "texsmart_standard"
}
```

```json
{
    "tokens": [
        {
            "token": "2020",
            "start_offset": 0,
            "end_offset": 4,
            "type": "CD",
            "position": 0
        },
        {
            "token": "年",
            "start_offset": 4,
            "end_offset": 5,
            "type": "M",
            "position": 1
        },
        {
            "token": "，",
            "start_offset": 5,
            "end_offset": 6,
            "type": "PU",
            "position": 2
        },
        {
            "token": "空调",
            "start_offset": 6,
            "end_offset": 8,
            "type": "NN",
            "position": 3
        },
        {
            "token": "市场",
            "start_offset": 8,
            "end_offset": 10,
            "type": "NN",
            "position": 4
        },
        {
            "token": "“",
            "start_offset": 10,
            "end_offset": 11,
            "type": "PU",
            "position": 5
        },
        {
            "token": "冷风",
            "start_offset": 11,
            "end_offset": 13,
            "type": "NN",
            "position": 6
        },
        {
            "token": "吹过",
            "start_offset": 13,
            "end_offset": 15,
            "type": "VV",
            "position": 7
        },
        {
            "token": "”",
            "start_offset": 15,
            "end_offset": 16,
            "type": "PU",
            "position": 8
        }
    ]
}
```

- 保证词典编码UTF-8

自定义分词配置
----------

HanLP在提供了各类分词方式的基础上，也提供了一系列的分词配置，分词插件也提供了相关的分词配置，我们可以在通过如下配置来自定义自己的分词器：

| Config                               | Elastic version     |
| :----------------------------------- | :------------------ |
| enable_index_mode                    | 是否是索引分词        |
| enable_stop_dictionary               | 是否启用停用词        |
| enable_offset                        | 是否计算偏移量        |

注意： 如果要采用如上配置配置自定义分词，需要设置enable_custom_config为true

例如：
```text
PUT test
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_texsmart_analyzer": {
          "tokenizer": "my_texsmart"
        }
      },
      "tokenizer": {
        "my_texsmart": {
          "type": "texsmart",
          "enable_stop_dictionary": true
        }
      }
    }
  }
}
```

```text
POST test/_analyze
{
    "text": "2020年，空调市场“冷风吹过”",
    "analyzer": "my_texsmart_analyzer"
}
```

结果：
```json
{
    "tokens": [
        {
            "token": "2020",
            "start_offset": 0,
            "end_offset": 4,
            "type": "CD",
            "position": 0
        },
        {
            "token": "年",
            "start_offset": 4,
            "end_offset": 5,
            "type": "M",
            "position": 1
        },
        {
            "token": "空调",
            "start_offset": 6,
            "end_offset": 8,
            "type": "NN",
            "position": 2
        },
        {
            "token": "市场",
            "start_offset": 8,
            "end_offset": 10,
            "type": "NN",
            "position": 3
        },
        {
            "token": "冷风",
            "start_offset": 11,
            "end_offset": 13,
            "type": "NN",
            "position": 4
        },
        {
            "token": "吹过",
            "start_offset": 13,
            "end_offset": 15,
            "type": "VV",
            "position": 5
        }
    ]
}

```

# 特别说明
1, texsmart目前官方不支持热词加载更新，听说下一个版本会支持。
   代码中参考analysis-hanlp插件集成了远程词库和动态更新分词的功能
   后续等腾讯官方版本更新后，上线该功能

🚩 参考资料：
[TexSmart](https://ai.tencent.com/ailab/nlp/texsmart)
[analysis-hanlp](https://github.com/KennFalcon/elasticsearch-analysis-hanlp)