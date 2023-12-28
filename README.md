<div align="center">
  <img alt="am logo" src="https://antchainbridge.oss-cn-shanghai.aliyuncs.com/antchainbridge/document/picture/antchain.png" width="250" >
  <h1 align="center">AntChain Bridge Plugin SDK</h1>
  <p align="center">
    <a href="http://makeapullrequest.com">
      <img alt="pull requests welcome badge" src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat">
    </a>
    <a href="https://www.java.com">
      <img alt="Language" src="https://img.shields.io/badge/Language-Java-blue.svg?style=flat">
    </a>
    <a href="https://github.com/AntChainOpenLab/AntChainBridgePluginSDK/graphs/contributors">
      <img alt="GitHub contributors" src="https://img.shields.io/github/contributors/AntChainOpenLab/AntChainBridgePluginSDK">
    </a>
    <a href="https://www.apache.org/licenses/LICENSE-2.0">
      <img alt="License" src="https://img.shields.io/github/license/AntChainOpenLab/AntChainBridgePluginSDK?style=flat">
    </a>
  </p>
</div>


# 介绍

AntChain Bridge将跨链互操作解释为两个层次：通信和可信，即跨链的目标在于实现区块链实体之间的可信通信。

在AntChain Bridge的架构中，中继需要与区块链进行交互，而异构链的通信协议各式各样，无法统一适配，因此AntChain Bridge抽象出了区块链桥接组件（Blockchain Bridge Component, BBC），来解决区块链和跨链网络的通信问题。

每种异构链要接入AntChain Bridge跨链网络，都需要实现一套标准的区块链桥接组件，可以分为链上和链下两部分，包括**链下插件**和**系统合约**。链下插件需要基于SDK完成开发，链上部分则通常是智能合约，要求实现特定的[接口](antchain-bridge-spi/README.md)和逻辑，为降低开发难度，我们提供了Solidity版本的[实现](./pluginset/ethereum/onchain-plugin/solidity)。

AntChain Bridge为开发者提供了SDK、手册和系统合约模板，来帮助开发者完成插件和合约的开发。同时，AntChain Bridge提供了插件服务（[PluginServer](https://github.com/AntChainOpenLab/AntChainBridgePluginServer)）来运行插件，插件服务是一个独立的服务，具备插件管理和响应中继请求的功能。

在当前的工程实现中，BBC链下部分是以插件的形式实现的。AntChain Bridge实现了一套SDK，通过实现SDK中规定的接口（SPI），经过简单的编译，即可生成插件包。插件服务（PluginServer, PS）可以加载BBC链下插件，详情可以参考插件服务的介绍[文档](https://github.com/AntChainOpenLab/AntChainBridgePluginServer/blob/main/README.md)。

在v0.2.0版本之后，加入了区块链域名服务（BlockChain Domain Name Service, BCDNS）模块以及其他数据结构，比如区块链域名证书等类型和工具，并在`antchain-bridge-bcdns`中增加了基于[星火链网](https://bitfactory.cn/)的BCDNS服务的客户端实现，该BCDNS服务由[中国信息通信研究院](http://www.caict.ac.cn/)开发支持，详情请[见](https://git.xinghuo.space/xinghuo-open-source/DLT/bcdns)。

在SDK中抽象了BCDNS服务的接口[IBlockChainDomainNameService](antchain-bridge-bcdns/src/main/java/com/alipay/antchain/bridge/bcdns/service/IBlockChainDomainNameService.java)，描述了BCDNS应该提供的功能，目前仅支持官方实现的BCDNS，支持的类型可[见](antchain-bridge-bcdns/src/main/java/com/alipay/antchain/bridge/bcdns/service/BCDNSTypeEnum.java)。

以下介绍了基于SDK的一个集成架构：

![](https://antchainbridge.oss-cn-shanghai.aliyuncs.com/antchainbridge/document/picture/deploy_arch_231228.png)

SDK共有五个部分，包括：

- **antchain-bridge-commons**：包含很多工具方法和数据结构，帮助BBC实现快速开发；

- **antchain-bridge-plugin-lib**：BBC插件化的依赖库，给出一个注解`@BBCService`，帮助插件开发者可以快速完成插件构建；

- **antchain-bridge-plugin-manager**：插件的管理库，提供插件的加载、生命周期管理等能力，插件服务依赖于这个库；

- **antchain-bridge-spi**：主要包含了接口`IBBCService`，描述了一个BBC实现类应该有的功能，开发者只要依次实现接口即可，详细接口介绍请[见](./antchain-bridge-spi/README.md)；

- **antchain-bridge-bcdns**：主要包含了接口`IBlockChainDomainNameService`，描述了一个BCDNS客户端应该有的功能，目前仅支持星火链网（BIF）的BCDNS客户端实现，详细使用可以参考[wiki]()中“如何实现跨链”的内容；

  

# 构建

**在开始之前，请您确保安装了maven和JDK，这里推荐使用[openjdk-1.8](https://adoptium.net/zh-CN/temurin/releases/?version=8)版本*

## 本地安装

在项目根目录下，直接使用maven编译即可：

```
mvn install -Dmaven.test.skip=true
```

或者在release[页面](https://github.com/AntChainOpenLab/AntChainBridgePluginSDK/releases)找到适合的版本并下载到本地，解压之后，在根目录下，运行脚本完成SDK的安装：

```
./install_sdk.sh
```

提示信息如下，代表安装完成：

```
    ___            __   ______ __            _           ____         _      __
   /   |   ____   / /_ / ____// /_   ____ _ (_)____     / __ ) _____ (_)____/ /____ _ ___
  / /| |  / __ \ / __// /    / __ \ / __ `// // __ \   / __  |/ ___// // __  // __ `// _ \
 / ___ | / / / // /_ / /___ / / / // /_/ // // / / /  / /_/ // /   / // /_/ // /_/ //  __/
/_/  |_|/_/ /_/ \__/ \____//_/ /_/ \__,_//_//_/ /_/  /_____//_/   /_/ \__,_/ \__, / \___/
                                                                            /____/        

[ INFO ]_[ 2023-12-27 17:40:42.170 ] : successful to install antchain-bridge-commons-0.2.0.jar
[ INFO ]_[ 2023-12-27 17:40:44.170 ] : successful to install antchain-bridge-spi-0.2.0.jar
[ INFO ]_[ 2023-12-27 17:40:45.170 ] : successful to install antchain-bridge-plugin-lib-0.2.0.jar
[ INFO ]_[ 2023-12-27 17:40:47.170 ] : successful to install antchain-bridge-plugin-manager-0.2.0.jar
[ INFO ]_[ 2023-12-27 17:40:48.170 ] : successful to install antchain-bridge-bcdns-0.2.0.jar
[ INFO ]_[ 2023-12-27 17:40:48.170 ] : success
```

这样，SDK的Jar包就被安装在本地了。

然后，可以通过在maven的pom.xml配置依赖就可以了，比如下面一段配置，`${antchain-bridge.sdk.version}`为当前仓库的版本号，可以在项目目录的[po m.xml](pom.xml)看到。

```xml
<dependency>
    <groupId>com.alipay.antchain.bridge</groupId>
    <artifactId>antchain-bridge-plugin-lib</artifactId>
    <version>${antchain-bridge.sdk.version}</version>
</dependency>
<dependency>
    <groupId>com.alipay.antchain.bridge</groupId>
    <artifactId>antchain-bridge-plugin-manager</artifactId>
    <version>${antchain-bridge.sdk.version}</version>
</dependency>
<dependency>
    <groupId>com.alipay.antchain.bridge</groupId>
    <artifactId>antchain-bridge-spi</artifactId>
    <version>${antchain-bridge.sdk.version}</version>
</dependency>
<dependency>
    <groupId>com.alipay.antchain.bridge</groupId>
    <artifactId>antchain-bridge-commons</artifactId>
    <version>${antchain-bridge.sdk.version}</version>
</dependency>
<dependency>
    <groupId>com.alipay.antchain.bridge</groupId>
    <artifactId>antchain-bridge-bcdns</artifactId>
    <version>${antchain-bridge.sdk.version}</version>
</dependency>
```

## 通过GitHub Package安装

参考[这里](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-to-github-packages)配置您的maven，在`setting.xml`中配置上您的GitHub账号和Token。

在您的项目`pom.xml`中，配置上我们的repository：

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/AntChainOpenLab/*</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

这样您就可以导入上面的dependency来使用SDK。

# 快速开始

## Testchain

[Testchain](pluginset/demo-testchain)是一个用于讲解如何开发BBC插件的demo工程，结合AntChain Bridge的文档，可以更好地理解BBC的开发过程。

详细的开发教程请参考本仓库的[Wiki](https://github.com/AntChainOpenLab/AntChainBridgePluginSDK/wiki)。

## 以太坊

基于SDK，我们开发了一个打通以太坊的BBC[插件](./pluginset/ethereum)。

进入以太坊插件的路径下，可以看到以下文件：

```
# tree -L 4 .        
.
├── offchain-plugin
│   ├── README.md
│   ├── pom.xml
│   └── src
└── onchain-plugin
    ├── README.md
    └── solidity
        ├── scenarios
        │   └── nft_crosschain
        └── sys
            ├── AppContract.sol
            ├── AuthMsg.sol
            ├── SDPMsg.sol
            ├── interfaces
            └── lib
```

- **offchain-plugin**工程下面，我们基于`Web3j`，实现了以太坊的BBC插件的链下部分；
- **onchain-plugin**工程下面，主要分为两部分：
  - **sys**：包含以太坊的BBC链上部分，实现了AM、SDP等逻辑。
  - **scenarios**：本路径下的`nft_crosschain`中，我们实现了一套跨链桥方案，用于ERC1155资产的跨链。

详细操作请[见](pluginset/ethereum/offchain-plugin/README.md)。

## EOS

基于SDK，我们提供了一个打通EOS链的BBC[插件](pluginset/eos)。

- **offchain-plugin**工程下面实现了EOS的BBC插件的链下部分；
- **onchain-plugin**工程下面，主要分为两部分：
  - **合约代码**：合约代码放在[路径](pluginset/eos/onchain-plugin/cpp/sys/src)下面，包含AM合约、SDP合约、Demo合约，详情请[见](pluginset/eos/onchain-plugin/README.md)。

详细操作请[见](pluginset/ethereum/offchain-plugin/README.md)。

## Mychain

基于SDK我们给出了打通蚂蚁链（Mychain）的BBC[插件](pluginset/mychain0.10)，目前内部依赖发布中，发布之后即可编译使用。

# 社区治理

AntChain Bridge 欢迎您以任何形式参与社区建设。

您可以通过以下方式参与社区讨论

- 钉钉

![scan dingding](https://antchainbridge.oss-cn-shanghai.aliyuncs.com/antchainbridge/document/picture/dingding.png)

- 邮件

发送邮件到`antchainbridge@service.alipay.com`

# License

详情参考[LICENSE](./LICENSE)。