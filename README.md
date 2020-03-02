# kotlium

Seleniumをベースとした、E2Eテストのサポートライブラリです。Kotlin DSLにより、変更容易性、拡張容易性、理解容易性の高いテストコードの記述をサポートします。

Kotliumの使用サンプル

``` kotlin
@Test
fun scenarioExecuteMultipleActionTest() {
    Scenario {
        browserStage("http://example.com") {
            click {
                By.id("login")
            }
            input {
                target = By.xpath("//input[@class='user-name']")
                value = "USER-NAME"
            }
            input {
                target = By.xpath("//input[@class='password']")
                value = "PASSWORD"
                lastEnter = true
            }
            waitFor {
                urlToBe("http://example.com/user")
            }
            assertPage {
                assertThat(findElement(By.id("user-name")).isDisplayed).isTrue()
            }
        }
    }.execute(webDriver).throwIfFailed()
}
```

## Feature

* ブラウザ操作を `Action` と呼ばれるクラスで、管理しやすい単位ごとに実装できます。
* ブラウザの一連の操作を `BrowserStage` クラスとして実装することで、再利用性を向上できます。
* 複雑なブラウザ操作を隠蔽し、理解容易性を向上します。

## install

Kotlium本体と、追加の依存関係をビルドツールに設定します。Kotlium本体は[リリースページ](https://github.com/BooookStore/kotlium/releases)から入手できます。

```
implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
implementation "org.jetbrains.kotlin:kotlin-reflect:1.3.61"
implementation "org.seleniumhq.selenium:selenium-java:3.141.59"
implementation "kotlium-0.0.1.jar"
```

## Usage

Kotliumの使い方を簡単に示します。

### Architecture

Kotliumは以下の３つの要素でテストを構成します。

* Action ... プリミティブな操作。ブラウザであれば、クリックやログインなど管理しやすい単位。
* Stage ... 複数のActionで構成されたテストのフェーズ。
* Scenario ... トップの要素。複数のStageで構成され、テストの実行の起点になる。
