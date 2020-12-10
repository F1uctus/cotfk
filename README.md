<h1 align="center">CotFK</h1>

<p align="center">
    <a href="https://github.com/actions/setup-java">
        <img src="https://github.com/F1uctus/cotfk/workflows/Maven/badge.svg"
             alt="GitHub Actions status for 'Maven'">
    </a>
    <a href="https://gitmoji.carloscuesta.me">
        <img src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg?style=flat-square"
             alt="Gitmoji">
    </a>
</p>

<h3 align="center">Crown of the Fallen King - An example RPG built with <a href="https://github.com/F1uctus/crown">Crown.</a></h3>

Powered by JDK 11.
Only arrow keys and console commands are now supported.

Notable Ant/Ivy build targets:

|                 |                                                              |
| --------------- | ------------------------------------------------------------ |
| `install`       | Download dependencies from `pom.xml` into `target/deps`.     |
| `build`         | Create a `.jar` from sources & deps.                         |
| `build-tests`   | Create a `.jar` from test sources & deps.                    |
| `run`           | Launch the game from generated `.jar` file.                  |
| `test`          | Execute JUnit tests & save reports to `target/test-reports`. |

![](screenshots/1.png)
