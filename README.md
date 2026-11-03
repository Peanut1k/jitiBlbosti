# Losovátko Jíti

Krátká hotová ukázka: FXML popisuje rozhraní, CSS jeho vzhled a Java controller reaguje na tlačítko. Projekt používá jen jednoduché pole jmen, `Random`, dva popisky a jedno tlačítko.

Po kliknutí se pod textem **„nejoblíbenější student(ka) Jíti je:“** objeví jedno ze jmen:

Mates, Péťa, Fanda, Šáry, Oli, Ali, Míra, Dan, Marťas, David, Kája, Pepa, Martin, Kornel.

Losuje se s opakováním. Každé jméno má při každém kliknutí stejnou šanci 1/14. Počítadlo ukazuje počet kliknutí na Losuj!, po novém spuštění začíná od nuly.

## Spuštění v IntelliJ IDEA

Připraveno pro **JDK 17 nebo 21**, JavaFX **21.0.13**, Maven. Pro výuku doporučujeme JDK 21. JavaFX stáhne Maven podle systému počítače.

1. Rozbal celý ZIP do složky.
2. V IntelliJ zvol **File → Open**, vyber `pom.xml` a otevři jej jako projekt.
3. Nastav **Project SDK** na JDK 17 nebo 21. Stejný JDK použij v **Settings → Build Tools → Maven → Runner → JRE**.
4. Nech Maven načíst závislosti. Při prvním načtení je potřeba internet.
5. V panelu **Maven → Plugins → javafx → javafx:run** spusť aplikaci. Panel otevřeš přes **View → Tool Windows → Maven**.

Pokud máš Maven i v terminálu, můžeš ze složky obsahující `pom.xml` spustit:

```sh
mvn clean javafx:run
```

Pro kontrolu překladu bez otevření okna:

```sh
mvn clean compile
```

Ukázka nepotřebuje ruční stahování JavaFX SDK ani další knihovny. Soubor `pom.xml` a `module-info.java` už jsou připravené. FXML můžeš upravovat jako text nebo otevřít v Scene Builderu.

## Který soubor otevřít

| Soubor | Co si na něm ukážeme |
| --- | --- |
| `src/main/java/cz/skola/losovatko/LosovatkoApplication.java` | `FXMLLoader`, `Scene`, připojení CSS a `Stage` |
| `src/main/resources/cz/skola/losovatko/losovatko-view.fxml` | `VBox`, `Label`, `Button`, `fx:controller`, `fx:id`, `onAction` |
| `src/main/java/cz/skola/losovatko/LosovatkoController.java` | `@FXML`, losování z pole a změna textů |
| `src/main/resources/cz/skola/losovatko/losovatko.css` | `.label`, `.button`, vlastní třídy a `#jmeno` |
| `src/main/java/module-info.java` | Přístup FXMLLoaderu k controlleru přes `opens` |

Java a resources mají shodnou cestu `cz/skola/losovatko`. Proto `getResource("losovatko-view.fxml")` najde soubor vedle třídy v témže balíčku po sestavení. Názvy a velikost písmen musí souhlasit.

## Projdi spojení od kliknutí k výsledku

1. `LosovatkoApplication.start()` načte FXML přes `loader.load()` a připojí CSS k vytvořené scéně.
2. `fx:controller="cz.skola.losovatko.LosovatkoController"` určí třídu, kterou FXMLLoader vytvoří.
3. `fx:id="jmenoLabel"` propojí Label s polem `@FXML private Label jmenoLabel;`.
4. `onAction="#losuj"` zavolá po aktivaci tlačítka metodu `@FXML private void losuj()`.
5. Controller vybere index pole a zavolá `jmenoLabel.setText(...)`.
6. Prvek s `id="jmeno"` získá vzhled ze selektoru `#jmeno` v CSS.

`fx:id` slouží k propojení s Javou. `id` slouží k výběru přes `#...` v CSS. JavaFX může při chybějícím `id` převzít hodnotu `fx:id`; zde používáme obě hodnoty výslovně a odlišně, aby bylo vidět, co s čím souvisí. `styleClass="nadpis"` ve FXML se v CSS zapisuje jako `.nadpis`. Znak `#` v `onAction="#losuj"` odkazuje na metodu, zatímco `#jmeno` v CSS vybírá prvek podle `id`.

## Rychlé ověření ukázky

| Akce | Očekávaný výsledek |
| --- | --- |
| Spusť aplikaci | Výsledek `......`, počet losování 0, fialové tlačítko a bílé pole pro jméno |
| Jednou klikni na Losuj! | Jedno ze 14 zadaných jmen, počet 1 |
| Klikni ještě devětkrát | Počet 10, vždy platné jméno, aplikace běží dál |
| Aktivuj tlačítko klávesou Enter | Stejné chování jako kliknutí, počet se zvýší o 1 |
| Spusť aplikaci znovu | Znovu `......` a počet 0 |

Opakované jméno je platný výsledek. Ani po 14 kliknutích nemusí padnout všech 14 jmen.

## Práce ve dvojici

Pokračujte souborem **ZADANI-45-MIN.md**. Když se zaseknete, otevřete postupně nápovědy v **NAPOVEDY.md**.

## Když něco neběží

| Projev | Co zkontrolovat |
| --- | --- |
| `Location is not set` nebo nenalezený resource | FXML a CSS patří do `src/main/resources/cz/skola/losovatko/`, názvy musí přesně souhlasit |
| `Error resolving onAction='#losuj'` | Metoda `losuj`, její `@FXML` a správný `fx:controller` |
| `NullPointerException` při `jmenoLabel.setText(...)` | Shodu `fx:id` s názvem pole a anotaci `@FXML` |
| Chyba o nepřístupném controlleru | Řádek `opens cz.skola.losovatko to javafx.fxml;` v `module-info.java` |
| Okno funguje, ale je bez barev | Připojení CSS v `start()` a přesný název souboru |
| `JavaFX runtime components are missing` | Spusť projekt přes připravený Maven cíl `javafx:run` |
| Maven hlásí neplatnou verzi Javy | Project SDK i Maven Runner musí používat JDK 17 nebo 21 |

Dokumentace: [FXML](https://openjfx.io/javadoc/21/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html), [CSS](https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/doc-files/cssref.html), [spouštění přes Maven](https://openjfx.io/openjfx-docs/maven).
