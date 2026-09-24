# Boolean Logic Simulator

A Java Swing desktop app that takes a boolean expression (like `(A+B)^~C`), evaluates it for
whatever values you assign to each variable, and draws the corresponding logic-gate circuit
diagram on screen.

- **Input:** an infix boolean expression using variable letters, `+` for OR, `^` for AND, and
  `~` or `'` for NOT (e.g. `A^B`, `(A+B)^~C`, `A'^B`).
- **Output:** the evaluated `TRUE`/`FALSE` result, a live count of AND/OR/NOT gates used, and an
  auto-laid-out circuit diagram of the expression.

## How it works

1. The user types an infix expression and clicks **Add Expression**.
2. The expression is converted to postfix notation (`InfixToPostfix`).
3. A copy of that postfix string — still containing the variable letters — is turned into a
   binary tree of gates (`CircuitBuilder` + `CircuitNode`) and handed to `CircuitPanel`, which
   lays it out and paints it.
4. Separately, the user is prompted for a `0`/`1` value for each variable, those values are
   substituted into the postfix string (`MyFrame.convertInput`), and the resulting `0`/`1`
   postfix expression is evaluated (`PostfixEvaluater`) to get the final `TRUE`/`FALSE`.

## Classes

### Core logic

| Class | Purpose |
|---|---|
| `InfixToPostfix` | Converts the infix expression the user types (e.g. `(A+B)^~C`) into postfix notation (`AB+C~^`) using the standard operator-precedence/shunting-yard algorithm. Handles `+` (OR), `^` (AND), `~`/`'` (NOT), and parentheses. |
| `PostfixEvaluater` | Evaluates a postfix expression made up of `0`s and `1`s (after variables have been substituted with actual values) down to a single `true`/`false` result, using a stack. |
| `CircuitNode` | A single node in the circuit tree. Either a **leaf** (an input variable) or a **gate** (`~` NOT with one child, `^` AND or `+` OR with two children). Also stores the node's computed `height` (distance from the nearest leaf) and its `x`/`y` screen position once laid out. |
| `CircuitBuilder` | Static helper that builds a tree of `CircuitNode`s from a postfix expression (`buildTree`), computes each node's height bottom-up (`computeHeights`), and counts how many leaf/variable nodes are in the tree (`countLeaves`). |

### Drawing

| Class | Purpose |
|---|---|
| `CircuitPanel` | The custom `JPanel` that actually draws the full gate diagram. It lays out every leaf in its own horizontal lane and every gate one level to the right of its tallest child, then draws AND/OR/NOT gate shapes and the elbow wires connecting them. This is the diagram shown in the "Circuit Diagram" card in the UI. |
| `AndGraph`, `OrGraph`, `NotGraph` | Early standalone sketches of individual AND/OR/NOT gate shapes, each drawn at a fixed hardcoded position. They predate `CircuitPanel`'s own built-in gate-drawing methods and are not currently used by the running app — the gate shapes actually drawn on screen come from `CircuitPanel`'s private `drawAndGate`/`drawOrGate`/`drawNotGate` methods instead. |

### User interface

| Class | Purpose |
|---|---|
| `MyFrame` | The main application window. Builds the whole UI (header, expression sidebar, variable list, circuit diagram card, and the result/gate-count card), wires up the "Add Expression" button, and coordinates the full pipeline described above: parsing the expression, prompting for variable values, evaluating the result, updating the gate-count display, and telling `CircuitPanel` what to draw. |
| `PlaceholderTextField` | A `JTextField` subclass that shows light-gray hint text (e.g. `e.g. (A+B)^~C`) when it's empty and unfocused, so first-time users know what format to type the expression in. |
| `RoundedButton` | A flat, rounded-corner `JButton` with hover/press color states, used for the "Add Expression" button instead of the default chunky Swing button. |
| `RoundedPanel` | A `JPanel` that paints itself as a white rounded "card" with a thin border, used throughout the UI (sidebar, circuit diagram card, result card) to give the app its card-based look. Works as a drop-in `JPanel` replacement — `add()`/`remove()`/`setLayout()` all still work normally. |

## Running

Compile all files under the `Boolean_Expression` package and run `MyFrame`'s `main` method
(this is the entry point that creates and shows the window).

## Example

Input: `(A+B)^~C`

- Postfix: `AB+C~^`
- Circuit: an OR gate combining `A` and `B`, feeding into an AND gate along with a NOT gate on `C`
- Result: depends on the `0`/`1` values you supply for `A`, `B`, and `C` when prompted
