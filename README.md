# Losovátko Jíti

Krátká hotová ukázka: FXML popisuje rozhraní, CSS jeho vzhled a Java controller reaguje na tlačítko. Projekt používá jen jednoduché pole jmen, `Random`, dva popisky a jedno tlačítko.

Po kliknutí se pod textem **„nejoblíbenější student(ka) Jíti je:“** objeví jedno ze jmen.


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



