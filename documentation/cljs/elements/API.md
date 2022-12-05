
# elements.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > elements.api

### Index

- [anchor](#anchor)

- [apply-preset](#apply-preset)

- [blank](#blank)

- [breadcrumbs](#breadcrumbs)

- [button](#button)

- [button-separator](#button-separator)

- [card](#card)

- [checkbox](#checkbox)

- [chip](#chip)

- [chip-group](#chip-group)

- [circle-diagram](#circle-diagram)

- [color-marker](#color-marker)

- [color-stamp](#color-stamp)

- [column](#column)

- [combo-box](#combo-box)

- [counter](#counter)

- [digit-field](#digit-field)

- [expandable](#expandable)

- [file-drop-area](#file-drop-area)

- [ghost](#ghost)

- [horizontal-line](#horizontal-line)

- [horizontal-polarity](#horizontal-polarity)

- [icon](#icon)

- [icon-button](#icon-button)

- [image](#image)

- [label](#label)

- [line-diagram](#line-diagram)

- [menu-bar](#menu-bar)

- [multi-combo-box](#multi-combo-box)

- [multi-field](#multi-field)

- [point-diagram](#point-diagram)

- [radio-button](#radio-button)

- [row](#row)

- [select](#select)

- [slider](#slider)

- [stepper](#stepper)

- [switch](#switch)

- [text](#text)

- [text-field](#text-field)

- [thumbnail](#thumbnail)

- [toggle](#toggle)

- [vertical-line](#vertical-line)

- [vertical-polarity](#vertical-polarity)

### anchor

```
@param (keyword) anchor-id
@param (map) anchor-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [anchor]]))

(elements.api/anchor ...)
(anchor              ...)
```

</details>

---

### apply-preset

```
@param (map) presets
@param (map) element-props
{:preset (keyword)(opt)}
```

```
@usage
(apply-preset {:my-preset {...}}
              {:preset :my-preset ...})
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [apply-preset]]))

(elements.api/apply-preset ...)
(apply-preset              ...)
```

</details>

---

### blank

```
@param (keyword) blank-id
@param (map) blank-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [blank]]))

(elements.api/blank ...)
(blank              ...)
```

</details>

---

### breadcrumbs

```
@param (keyword) breadcrumbs-id
@param (map) breadcrumbs-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [breadcrumbs]]))

(elements.api/breadcrumbs ...)
(breadcrumbs              ...)
```

</details>

---

### button

```
@param (keyword) button-id
@param (map) button-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [button]]))

(elements.api/button ...)
(button              ...)
```

</details>

---

### button-separator

```
@param (keyword) separator-id
@param (map) separator-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [button-separator]]))

(elements.api/button-separator ...)
(button-separator              ...)
```

</details>

---

### card

```
@param (keyword) card-id
@param (map) card-props
{:on-click (metamorphic-event)(opt)}
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [card]]))

(elements.api/card ...)
(card              ...)
```

</details>

---

### checkbox

```
@param (keyword) checkbox-id
@param (map) checkbox-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [checkbox]]))

(elements.api/checkbox ...)
(checkbox              ...)
```

</details>

---

### chip

```
@param (keyword) chip-id
@param (map) chip-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [chip]]))

(elements.api/chip ...)
(chip              ...)
```

</details>

---

### chip-group

```
@param (keyword) group-id
@param (map) group-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [chip-group]]))

(elements.api/chip-group ...)
(chip-group              ...)
```

</details>

---

### circle-diagram

```
@param (keyword) diagram-id
@param (map) diagram-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [circle-diagram]]))

(elements.api/circle-diagram ...)
(circle-diagram              ...)
```

</details>

---

### color-marker

```
@param (keyword) marker-id
@param (map) marker-props
{}
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [color-marker]]))

(elements.api/color-marker ...)
(color-marker              ...)
```

</details>

---

### color-stamp

```
@param (keyword) stamp-id
@param (map) stamp-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [color-stamp]]))

(elements.api/color-stamp ...)
(color-stamp              ...)
```

</details>

---

### column

```
@param (keyword) column-id
@param (map) column-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [column]]))

(elements.api/column ...)
(column              ...)
```

</details>

---

### combo-box

```
@param (keyword) box-id
@param (map) box-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [combo-box]]))

(elements.api/combo-box ...)
(combo-box              ...)
```

</details>

---

### counter

```
@param (keyword) counter-id
@param (map) counter-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [counter]]))

(elements.api/counter ...)
(counter              ...)
```

</details>

---

### digit-field

```
@param (keyword) field-id
@param (map) field-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [digit-field]]))

(elements.api/digit-field ...)
(digit-field              ...)
```

</details>

---

### expandable

```
@param (keyword) expandable-id
@param (map) expandable-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [expandable]]))

(elements.api/expandable ...)
(expandable              ...)
```

</details>

---

### file-drop-area

```
@param (keyword) area-id
@param (map) area-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [file-drop-area]]))

(elements.api/file-drop-area ...)
(file-drop-area              ...)
```

</details>

---

### ghost

```
@param (keyword) ghost-id
@param (map) ghost-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [ghost]]))

(elements.api/ghost ...)
(ghost              ...)
```

</details>

---

### horizontal-line

```
@param (keyword) line-id
@param (map) line-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [horizontal-line]]))

(elements.api/horizontal-line ...)
(horizontal-line              ...)
```

</details>

---

### horizontal-polarity

```
@param (keyword) polarity-id
@param (map) polarity-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [horizontal-polarity]]))

(elements.api/horizontal-polarity ...)
(horizontal-polarity              ...)
```

</details>

---

### icon

```
@param (keyword) icon-id
@param (map) icon-props
{:icon (keyword)}
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [icon]]))

(elements.api/icon ...)
(icon              ...)
```

</details>

---

### icon-button

```
@param (keyword) button-id
@param (map) button-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [icon-button]]))

(elements.api/icon-button ...)
(icon-button              ...)
```

</details>

---

### image

```
@param (keyword) image-id
@param (map) image-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [image]]))

(elements.api/image ...)
(image              ...)
```

</details>

---

### label

```
@param (keyword) label-id
@param (map) label-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [label]]))

(elements.api/label ...)
(label              ...)
```

</details>

---

### line-diagram

```
@param (keyword) diagram-id
@param (map) diagram-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [line-diagram]]))

(elements.api/line-diagram ...)
(line-diagram              ...)
```

</details>

---

### menu-bar

```
@param (keyword) bar-id
@param (map) bar-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [menu-bar]]))

(elements.api/menu-bar ...)
(menu-bar              ...)
```

</details>

---

### multi-combo-box

```
@param (keyword) box-id
@param (map) box-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [multi-combo-box]]))

(elements.api/multi-combo-box ...)
(multi-combo-box              ...)
```

</details>

---

### multi-field

```
@param (keyword) group-id
@param (map) group-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [multi-field]]))

(elements.api/multi-field ...)
(multi-field              ...)
```

</details>

---

### point-diagram

```
@param (keyword) diagram-id
@param (map) diagram-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [point-diagram]]))

(elements.api/point-diagram ...)
(point-diagram              ...)
```

</details>

---

### radio-button

```
@param (keyword) button-id
@param (map) button-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [radio-button]]))

(elements.api/radio-button ...)
(radio-button              ...)
```

</details>

---

### row

```
@param (keyword) row-id
@param (map) row-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [row]]))

(elements.api/row ...)
(row              ...)
```

</details>

---

### select

```
@param (keyword) select-id
@param (map) select-props
{:layout (keyword)}
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [select]]))

(elements.api/select ...)
(select              ...)
```

</details>

---

### slider

```
@param (keyword) slider-id
@param (map) slider-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [slider]]))

(elements.api/slider ...)
(slider              ...)
```

</details>

---

### stepper

```
@param (keyword) stepper-id
@param (map) stepper-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [stepper]]))

(elements.api/stepper ...)
(stepper              ...)
```

</details>

---

### switch

```
@param (keyword) switch-id
@param (map) switch-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [switch]]))

(elements.api/switch ...)
(switch              ...)
```

</details>

---

### text

```
@param (keyword) text-id
@param (map) text-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [text]]))

(elements.api/text ...)
(text              ...)
```

</details>

---

### text-field

```
@param (keyword) field-id
@param (map) field-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [text-field]]))

(elements.api/text-field ...)
(text-field              ...)
```

</details>

---

### thumbnail

```
@param (keyword) thumbnail-id
@param (map) thumbnail-props
{:on-click (metamorphic-event)(opt)}
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [thumbnail]]))

(elements.api/thumbnail ...)
(thumbnail              ...)
```

</details>

---

### toggle

```
@param (keyword) toggle-id
@param (map) toggle-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [toggle]]))

(elements.api/toggle ...)
(toggle              ...)
```

</details>

---

### vertical-line

```
@param (keyword) line-id
@param (map) line-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [vertical-line]]))

(elements.api/vertical-line ...)
(vertical-line              ...)
```

</details>

---

### vertical-polarity

```
@param (keyword) polarity-id
@param (map) polarity-props
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [vertical-polarity]]))

(elements.api/vertical-polarity ...)
(vertical-polarity              ...)
```

</details>

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine

