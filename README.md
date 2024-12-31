# Distributed and Parallel Computing Project (2024, Nanjing University)

This is the course project for the 2024 Distributed and Parallel Computing, Nanjing University. 

Focusing on string matching problem, the project explores two practical scenarios to demonstrate the power of parallel computing:

1. **Document Retrieval**: Efficiently search for keywords across multiple documents using parallel algorithms.
2. **Antivirus Software Simulation**: Simulate a virus scanning process with parallelized file analysis.

## Technology Stack
- **Programming Language**: Java
- **Build Tool**: Gradle
- **IDE**: IntelliJ IDEA

## Prerequisites
- **JDK**: Version 17 or higher
- **Gradle**: Version 7.0 or higher
- **IDE**: IntelliJ IDEA (optional, recommended)

## Build & Run
本项目可通过下面两个方式构建&运行。

#### Gradle

该项目使用 Gradle 构建工具，相关配置在项目根目录下的 `build.gradle` 文件中。

构建项目：
```
./gradlew build
```

运行项目：

```
./gradlew run --args="problem_to_solve parameter1 parameter2 ..."
```

e.g.
```
./gradlew run --args="doc path/to/document.txt path/to/target.txt -a kmp -b 16K"
./gradlew run --args="virus path/to/scandir path/to/virusdir -a kmp"
```

#### IntelliJ IDEA

在 IntelliJ IDEA 中打开本项目。

找到 `Main.java` 文件，右键选择 `Modify Run Configuration`，
在其中添加运行参数，而后即可使用 IDEA 运行本项目。

#### 参数示例

使用 `-h` 或 `--help` 可以查看所有参数。

对于场景1文档检索，必要的参数如下：
```
    doc path/to/document.txt path/to/target.txt
```

对于场景2软件杀毒，必要的参数如下：
```
    virus path/to/opencv-4.10.0 path/to/virus
```


可选的选项为：
* `-o, --output=<output>`：指定输出文件路径；
* `-a, --algo=<algo>`：指定算法，默认为暴力算法 `simple`，可选 `simple, kmp`；
* `--no-para`：串行执行；
* `-t, --thread-num=<nrThread>`：指定线程数，默认为运行环境的可用处理器数，仅对并行执行有效；
* `-b, --block-size=<blockSize>`：指定场景1中文件分块大小，可选择单位 `K` 或 `M`，默认为 `16K`，仅对场景1并行执行有效； 

#### 

## Project Structure
```
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── document    # Code for document retrieval
│   │   │   ├── virus       # Code for antivirus simulation
│   │   │   ├── util        # Some utils
│   │   │   ├── option      # Code for option configuration
│   │   │   └── Main.java   # Entry point of the project
│   ├── test                # Unit tests (no tests now)
├── build.gradle            # Gradle build script
└── README.md               # Project documentation
```
