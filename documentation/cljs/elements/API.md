
# elements.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > elements.api

### Index

- [blank](#blank)

- [breadcrumbs](#breadcrumbs)

- [button](#button)

- [card](#card)

- [checkbox](#checkbox)

- [chip](#chip)

- [chip-group](#chip-group)

- [color-selector](#color-selector)

- [column](#column)

- [combo-box](#combo-box)

- [content-swapper](#content-swapper)

- [counter](#counter)

- [data-table](#data-table)

- [date-field](#date-field)

- [digit-field](#digit-field)

- [dropdown-menu](#dropdown-menu)

- [element-label](#element-label)

- [expandable](#expandable)

- [ghost](#ghost)

- [horizontal-group](#horizontal-group)

- [horizontal-line](#horizontal-line)

- [horizontal-polarity](#horizontal-polarity)

- [horizontal-separator](#horizontal-separator)

- [horizontal-spacer](#horizontal-spacer)

- [icon](#icon)

- [icon-button](#icon-button)

- [image](#image)

- [label](#label)

- [menu-bar](#menu-bar)

- [multi-combo-box](#multi-combo-box)

- [multi-field](#multi-field)

- [multiline-field](#multiline-field)

- [notification-bubble](#notification-bubble)

- [number-field](#number-field)

- [password-field](#password-field)

- [plain-field](#plain-field)

- [radio-button](#radio-button)

- [row](#row)

- [search-field](#search-field)

- [select](#select)

- [slider](#slider)

- [stepper](#stepper)

- [switch](#switch)

- [text](#text)

- [text-field](#text-field)

- [thumbnail](#thumbnail)

- [toggle](#toggle)

- [vertical-group](#vertical-group)

- [vertical-line](#vertical-line)

- [vertical-polarity](#vertical-polarity)

- [vertical-spacer](#vertical-spacer)

### blank

```
@param (keyword)(opt) blank-id
@param (map) blank-props
{:background-pattern (keyword)(opt)
  :stripes
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :height (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[blank {...}]
```

```
@usage
[blank :my-blank {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([blank-props]
   [element (random/generate-keyword) blank-props])

  ([blank-id blank-props]
   (fn [_ blank-props]       (let [blank-props (blank.prototypes/blank-props-prototype blank-props)]
            [blank blank-id blank-props]))))
```

</details>

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
@param (keyword)(opt) breadcrumbs-id
@param (map) breadcrumbs-props
{:class (keyword or keywords in vector)(opt)
 :crumbs (maps in vector)
  [{:href (string)(opt)
    :label (metamorphic-content)(opt)
    :on-click (Re-Frame metamorphic-event)(opt)
    :placeholder (metamorphic-content)(opt)}]
 :disabled? (boolean)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  Default: :s
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[breadcrumbs {...}]
```

```
@usage
[breadcrumbs :my-breadcrumbs {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([breadcrumbs-props]
   [element (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   (fn [_ breadcrumbs-props]       (let [breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-props)]
            [breadcrumbs breadcrumbs-id breadcrumbs-props]))))
```

</details>

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
@warning
BUG#9912
If the keypress key-code is 13 (ENTER) the on-click event will fire multiple times during the key is pressed!
This phenomenon caused by:
1. The keydown event focuses the button via the 'button.side-effects/key-pressed' function.
2. One of the default actions of the 13 (ENTER) key is to fire the on-click
   function on a focused button element, therefore the on-click function
   fires repeatedly during the 13 (ENTER) key is pressed.
In case of using any other key than the 13 (ENTER) the on-click function fires only by
the 'button.side-effects/key-released' function.
```

```
@param (keyword)(opt) button-id
@param (map) button-props
{:badge-color (keyword)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :badge-content (metamorphic-content)(opt)
 :badge-position (keyword)(opt)
  :tl, :tr, :br, :bl, :left, :right, :bottom, :top
  Default: :tr
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :cursor (keyword)(opt)
  :default, :disabled, :grab, :grabbing, :move, :pointer, :progress}
  Default: :pointer
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  Default: :medium
 :gap (keyword)(opt)
  Distance between the icon and the label
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :hover-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :hover-effect (keyword)(opt)
  :opacity
 :href (string)(opt)
 :icon (keyword)(opt)
 :icon-color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :icon-position (keyword)(opt)
  :left, :right
  Default: :left
 :icon-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :keypress (map)(opt)
  {:exclusive? (boolean)(opt)
   :key-code (integer)
   :required? (boolean)(opt)
    Default: false}
 :label (metamorphic-content)(opt)
 :line-height (keyword)(opt)
  :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl, left, :right, bottom, :top
 :on-click (Re-Frame metamorphic-event)(opt)
 :on-mouse-over (Re-Frame metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :progress (percent)(opt)
 :progress-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :muted
 :progress-direction (keyword)(opt)
  :ltr, :rtl, :ttb, :btt
  Default: :ltr
 :progress-duration (ms)(opt)
  Default: 250
 :style (map)(opt)
 :text-overflow (keyword)(opt)
  :ellipsis, :hidden, :wrap
  Default: :no-wrap
 :text-transform (keyword)(opt)
  :capitalize, :lowercase, :uppercase
 :tooltip-content (metamorphic-content)(opt)
 :tooltip-position (keyword)(opt)
  :left, :right
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[button {...}]
```

```
@usage
[button :my-button {...}]
```

```
@usage
[button {:keypress {:key-code 13} :on-click [:my-event]}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   (fn [_ button-props]       (let [button-props (button.prototypes/button-props-prototype button-props)]
            [button button-id button-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [button]]))

(elements.api/button ...)
(button              ...)
```

</details>

---

### card

```
@param (keyword)(opt) card-id
@param (map) card-props
{:badge-color (keyword)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :badge-content (metamorphic-content)(opt)
 :badge-position (keyword)(opt)
  :tl, :tr, :br, :bl, :left, :right, :bottom, :top
  Default: :tr
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :content (metamorphic-content)(opt)
 :class (keyword or keywords in vector)(opt)
 :cursor (keyword)(opt)
  :default, :disabled, :grab, :grabbing, :move, :pointer, :progress
  Default: :default or :pointer
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :height (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
 :hover-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :hover-effect (keyword)(opt)
  :opacity
 :href (string)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl, left, :right, bottom, :top
 :max-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :max-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-click (Re-Frame metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :target (keyword)(opt)
  :blank, :self
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[card {...}]
```

```
@usage
[card :my-card {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([card-props]
   [element (random/generate-keyword) card-props])

  ([card-id card-props]
   (fn [_ card-props]       (let [card-props (card.prototypes/card-props-prototype card-props)]
            [card card-id card-props]))))
```

</details>

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
@param (keyword)(opt) checkbox-id
@param (map) checkbox-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  Default: {:all :xs}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xs
 :class (keyword or keywords in vector)(opt)
 :default-value (boolean)(opt)
 :disabled? (boolean)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :initial-options (vector)(opt)
 :initial-value (boolean)(opt)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :on-check (Re-Frame metamorphic-event)(opt)
 :on-uncheck (Re-Frame metamorphic-event)(opt)
 :option-helper-f (function)(opt)
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :vertical
 :options-path (Re-Frame path vector)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :value-path (Re-Frame path vector)(opt)}
```

```
@usage
[checkbox {...}]
```

```
@usage
[checkbox :my-checkbox {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([checkbox-props]
   [element (random/generate-keyword) checkbox-props])

  ([checkbox-id checkbox-props]
   (fn [_ checkbox-props]       (let [checkbox-props (checkbox.prototypes/checkbox-props-prototype checkbox-id checkbox-props)]
            [checkbox checkbox-id checkbox-props]))))
```

</details>

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
@param (keyword)(opt) chip-id
@param (map) chip-props
{:color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :href (string)(opt)
  TODO Makes the chip clickable
 :icon (keyword)(opt)
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :label (metamorphic-content)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-click (Re-Frame metamorphic-event)(opt)
  TODO Makes the chip clickable
 :outdent (map)(opt)
  Same as the :indent property.
 :primary-button (map)(opt)
  {:icon (keyword)
   :icon-family (keyword)(opt)
    :material-symbols-filled, :material-symbols-outlined
    Default: :material-symbols-outlined
   :on-click (Re-Frame metamorphic-event)}
 :style (map)(opt)
 :target (keyword)(opt)
  Makes the chip clickable
  :blank, :self
  TODO
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[chip {...}]
```

```
@usage
[chip :my-chip {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([chip-props]
   [element (random/generate-keyword) chip-props])

  ([chip-id chip-props]
   (fn [_ chip-props]       (let [chip-props (chip.prototypes/chip-props-prototype chip-props)]
            [chip chip-id chip-props]))))
```

</details>

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
@warning
The {:deletable? true} setting only works when the chip values are not statically provided
with the {:chips [...]} property but dinamically provided by using the {:chips-path [...]} property!
```

```
@param (keyword)(opt) group-id
@param (map) group-props
{:class (keyword or keywords in vector)(opt)
 :chip-label-f (function)(opt)
  Default: return
 :chips (maps in vector)(opt)
 :chips-path (Re-Frame path vector)(opt)
 :deletable? (boolean)(opt)
  Default: false
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :placeholder (metamorphic-content)(opt)
 :style (map)(opt)}
```

```
@usage
[chip-group {...}]
```

```
@usage
[chip-group :my-chip-group {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (fn [_ group-props]       (let [group-props (chip-group.prototypes/group-props-prototype group-id group-props)]
            [chip-group group-id group-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [chip-group]]))

(elements.api/chip-group ...)
(chip-group              ...)
```

</details>

---

### color-selector

```
@param (keyword)(opt) selector-id
@param (map) selector-props
{:class (keyword or keywords in vector)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :on-select (Re-Frame metamorphic-event)(opt)
 :options (strings in vector)(opt)
 :options-label (metamorphic-content)(opt)
 :options-path (Re-Frame path vector)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :popup (map)(opt)
  {:border-color (keyword or string)(opt)
   :border-position (keyword)(opt)
   :border-radius (map)(opt)
   :border-width (keyword)(opt)
   :cover-color (keyword or string)(opt)
    Default: :black
   :fill-color (keyword or string)(opt)
    Default: :default
   :indent (map)(opt)
   :label (metamorphic-content)(opt)
   :min-width (keyword)(opt)
   :outdent (map)(opt)}
 :style (map)(opt)
 :value-path (Re-Frame path vector)(opt)}
```

```
@usage
[color-selector {...}]
```

```
@usage
[color-selector :my-color-selector {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([selector-props]
   [element (random/generate-keyword) selector-props])

  ([selector-id selector-props]
   (fn [_ selector-props]       (let [selector-props (color-selector.prototypes/selector-props-prototype selector-id selector-props)]
            [color-selector selector-id selector-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [color-selector]]))

(elements.api/color-selector ...)
(color-selector              ...)
```

</details>

---

### column

```
@param (keyword)(opt) column-id
@param (map) column-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :gap (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
 :height (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :max-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :max-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :vertical-align (keyword)(opt)
  :top, :center, :bottom, :space-around, :space-between, :space-evenly
  Default: :top
 :wrap-items? (boolean)(opt)
  Default: false
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[elements/column {...}]
```

```
@usage
[elements/column :my-column {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([column-props]
   [element (random/generate-keyword) column-props])

  ([column-id column-props]
   (fn [_ column-props]       (let [column-props (column.prototypes/column-props-prototype column-props)]
            [column column-id column-props]))))
```

</details>

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
@description
The 'combo-box' element writes its actual value into the Re-Frame state
delayed after the user stopped typing or without a delay when the user
leaves the field!
```

```
@param (keyword)(opt) box-id
@param (map) box-props
{:field-content-f (function)(opt)
  Default: return
 :field-value-f (function)(opt)
  Default: return
 :initial-options (vector)(opt)
 :on-select (Re-Frame metamorphic-event)(opt)
 :option-component (Reagent component symbol)(opt)
  Default: elements.combo-box.views/default-option-component
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-path (Re-Frame path vector)(opt)}
```

```
@usage
[combo-box {...}]
```

```
@usage
[combo-box :my-combo-box {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([box-props]
   [element (random/generate-keyword) box-props])

  ([box-id box-props]
   (fn [_ box-props]       (let [box-props (combo-box.prototypes/box-props-prototype box-id box-props)]
            [combo-box box-id box-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [combo-box]]))

(elements.api/combo-box ...)
(combo-box              ...)
```

</details>

---

### content-swapper

```
@warning
XXX#0517
The 'content-swapper' element's pages have absolute positioning, therefore
the 'content-swapper' element and its body are stretched to their parents in order
to clear space for the pages because they are not doing it for themeself because
their absolute positioning.
```

```
@param (keyword)(opt) swapper-id
@param (map) swapper-props
{:class (keyword or keywords in vector)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :initial-page (metamorphic-content)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[content-swapper {...}]
```

```
@usage
[content-swapper :my-content-swapper {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([swapper-props]
   [element (random/generate-keyword) swapper-props])

  ([swapper-id swapper-props]
   (fn [_ swapper-props]       (let []            [content-swapper swapper-id swapper-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [content-swapper]]))

(elements.api/content-swapper ...)
(content-swapper              ...)
```

</details>

---

### counter

```
@param (keyword)(opt) counter-id
@param (map) counter-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  Default: {:all :m}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xs
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :initial-value (integer)(opt)
  Default: 0
 :label (metamorphic-content)(opt)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :max-value (integer)(opt)
 :min-value (integer)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :resetable? (boolean)(opt)
  Default: false
 :style (map)(opt)
 :value-path (Re-Frame path vector)(opt)}
```

```
@usage
[counter {...}]
```

```
@usage
[counter :my-counter {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([counter-props]
   [element (random/generate-keyword) counter-props])

  ([counter-id counter-props]
   (fn [_ counter-props]       (let [counter-props (counter.prototypes/counter-props-prototype counter-id counter-props)]
            [counter counter-id counter-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [counter]]))

(elements.api/counter ...)
(counter              ...)
```

</details>

---

### data-table

```
@param (keyword)(opt) table-id
@param (map) table-props
{:class (keyword or keywords in vector)(opt)
 :columns (maps in vector)(opt)
  [{:cells (maps in vector)
     [{:color (keyword or string)(opt)
        :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
        Default: :inherit
       :content (metamorphic-content)(opt)
       :font-size (keyword)(opt)
        :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
        Default: :s
       :font-weight (keyword)(opt)
        :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
        Default :normal
       :horizontal-align (keyword)(opt)
        :center, :left, :right
        Default: :left
       :indent (map)(opt)
       :line-height (keyword)(opt)
        :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
        Default: :text-block
       :placeholder (metamorphic-content)(opt)
       :preset (keyword)(opt)
       :selectable? (boolean)(opt)
        Default: true
       :text-overflow (keyword)(opt)
        :ellipsis, :hidden, :wrap
        Default: :ellipsis
       :text-transform (keyword)(opt)
        :capitalize, :lowercase, :uppercase
       :tooltip-content (metamorphic-content)(opt)
       :tooltip-position (keyword)(opt)
        :left, :right}]
    :preset (keyword)(opt)
    :template (string)(opt)
     Default: "repeat(*cell-count*, 1fr)"
    :width (keyword)(opt)
     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
     Default: :s}]
 :disabled? (boolean)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :presets (map)(opt)
  Cell, column and row presets.
 :rows (maps in vector)(opt)
  [{:cells (maps in vector)
    :height (keyword)(opt)
     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
     Default: :s
    :preset (keyword)(opt)
    :template (string)(opt)
     Default: "repeat(*cell-count*, 1fr)"}]
 :style (map)(opt)}
```

```
@usage
[data-table {...}]
```

```
@usage
[data-table :my-data-table {...}]
```

```
@usage
[data-table {:columns [{:cells [{:content "Cell #1"}
                                {:content "Cell #2" :horizontal-align :right}]
                        :line-height :m
                        :template "1fr 40px"}]}]
             :horizontal-gap :xs
             :vertical-gap :xs
```

<details>
<summary>Source code</summary>

```
(defn element
  ([table-props]
   [element (random/generate-keyword) table-props])

  ([table-id table-props]
   (fn [_ table-props]       (let []            [data-table table-id table-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [data-table]]))

(elements.api/data-table ...)
(data-table              ...)
```

</details>

---

### date-field

```
@description
The 'date-field' element writes its actual value into the Re-Frame state
delayed after the user stopped typing or without a delay when the user
leaves the field!
```

```
@param (keyword)(opt) field-id
@param (map) field-props
{:date-from (string)(opt)
 :date-to (string)(opt)}
```

```
@usage
[date-field {...}]
```

```
@usage
[date-field :my-date-field {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props]       (let [field-props (date-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/element field-id field-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [date-field]]))

(elements.api/date-field ...)
(date-field              ...)
```

</details>

---

### digit-field

```
@description
The 'digit-field' element writes its actual value into the Re-Frame state
delayed after the user stopped typing or without a delay when the user
leaves the field!
```

```
@param (keyword)(opt) field-id
@param (map) field-props
{:digit-count (integer)
  Default: 4
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :value-path (Re-Frame path vector)}
```

```
@usage
[digit-field {...}]
```

```
@usage
[digit-field :my-digit-field {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props]       (let []            [digit-field field-id field-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [digit-field]]))

(elements.api/digit-field ...)
(digit-field              ...)
```

</details>

---

### dropdown-menu

```
@param (keyword)(opt) menu-id
@param (map) menu-props
{:menu-items (maps in vector)
  [{:content (metamorphic-content)}]
 :surface (map)(opt)
  {:border-color (keyword or string)(opt)
   :border-position (keyword)(opt)
   :border-radius (map)(opt)
   :border-width (keyword)(opt)
   :fill-color (keyword or string)
   :indent (map)(opt)
   :outdent (map)(opt)}}
```

```
@usage
[dropdown-menu {...}]
```

```
@usage
[dropdown-menu :my-dropdown-menu {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([menu-props]
   [element (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   (fn [_ menu-props]       (let [menu-props (dropdown-menu.prototypes/menu-props-prototype menu-id menu-props)]
            [dropdown-menu menu-id menu-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [dropdown-menu]]))

(elements.api/dropdown-menu ...)
(dropdown-menu              ...)
```

</details>

---

### element-label

```
@description
This component is the default label component of the elements.
```

```
@param (keyword) element-id
@param (map) element-props
{:helper (metamorphic-content)(opt)
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :marker-color (keyword)(opt)}
```

<details>
<summary>Source code</summary>

```
(defn element-label
  [element-id {:keys [helper info-text label marker-color]}]
  (if label [label.views/element {:content      label
                                  :helper       helper
                                  :info-text    info-text
                                  :marker-color marker-color
                                  :target-id    element-id}]))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [element-label]]))

(elements.api/element-label ...)
(element-label              ...)
```

</details>

---

### expandable

```
@param (keyword)(opt) expandable-id
@param (map) expandable-props
{:class (keywords or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
 :expanded? (boolean)(opt)
  Default: true
 :icon (keyword)(opt)
 :icon-color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[expandable {...}]
```

```
@usage
[expandable :my-expandable {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([expandable-props]
   [element (random/generate-keyword) expandable-props])

  ([expandable-id expandable-props]
   (fn [_ expandable-props]       (let [expandable-props (expandable.prototypes/expandable-props-prototype expandable-props)]
            [expandable expandable-id expandable-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [expandable]]))

(elements.api/expandable ...)
(expandable              ...)
```

</details>

---

### ghost

```
@param (keyword)(opt) ghost-id
@param (map) ghost-props
{:border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :class (keyword or keywords in vector)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :width (keyword)(opt)
  :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto}
```

```
@usage
[ghost {...}]
```

```
@usage
[ghost :my-ghost {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([ghost-props]
   [element (random/generate-keyword) ghost-props])

  ([ghost-id ghost-props]
   (fn [_ ghost-props]       (let [ghost-props (ghost.prototypes/ghost-props-prototype ghost-props)]
            [ghost ghost-id ghost-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [ghost]]))

(elements.api/ghost ...)
(ghost              ...)
```

</details>

---

### horizontal-group

```
@param (keyword)(opt) group-id
@param (map) group-props
{:class (keyword or keywords in vector)(opt)
 :default-props (map)(opt)
 :element (Reagent component symbol)
 :group-items (maps in vector)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[horizontal-group {...}]
```

```
@usage
[horizontal-group :my-horizontal-group {...}]
```

```
@usage
[horizontal-group {:default-props {:hover-color :highlight}
                   :element #'elements.api/button
                   :group-items [{:label "First button"  :href "/first"}
                                 {:label "Second button" :href "/second"}]}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (fn [_ group-props]       (let [group-props (horizontal-group.prototypes/group-props-prototype group-props)]
            [horizontal-group group-id group-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [horizontal-group]]))

(elements.api/horizontal-group ...)
(horizontal-group              ...)
```

</details>

---

### horizontal-line

```
@param (keyword)(opt) line-id
@param (map) line-props
{:class (keyword or keywords in vector)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :strength (px)(opt)
  Default: 1
 :width (keyword)(opt)
  :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto}
```

```
@usage
[horizontal-line {...}]
```

```
@usage
[horizontal-line :my-horizontal-line {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   (fn [_ line-props]       (let [line-props (horizontal-line.prototypes/line-props-prototype line-props)]
            [:div (horizontal-line.attributes/line-attributes line-id line-props)
                  [:div (horizontal-line.attributes/line-body-attributes line-id line-props)]]))))
```

</details>

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
@param (keyword)(opt) polarity-id
@param (map) polarity-props
{:class (keyword or keywords in vector)(opt)
 :end-content (metamorphic-content)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :middle-content (metamorphic-content)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :start-content (metamorphic-content)(opt)
 :vertical-align (keyword)(opt)
  :bottom, :center, :top
  Default: :center
 :width (keyword)(opt)
  :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto}
```

```
@usage
[horizontal-polarity {...}]
```

```
@usage
[horizontal-polarity :my-horizontal-polarity {...}]
```

```
@usage
[horizontal-polarity {:start-content [:<> [label {:content "My label"}]
                                          [label {:content "My label"}]]}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([polarity-props]
   [element (random/generate-keyword) polarity-props])

  ([polarity-id polarity-props]
   (fn [_ polarity-props]       (let [polarity-props (horizontal-polarity.prototypes/polarity-props-prototype polarity-props)]
            [horizontal-polarity polarity-id polarity-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [horizontal-polarity]]))

(elements.api/horizontal-polarity ...)
(horizontal-polarity              ...)
```

</details>

---

### horizontal-separator

```
@param (keyword)(opt) separator-id
@param (map) separator-props
{:class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :width (keyword)(opt)
  :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto}
```

```
@usage
[horizontal-separator {...}]
```

```
@usage
[horizontal-separator :my-horizontal-separator {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([separator-props]
   [element (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   (fn [_ separator-props]       (let [separator-props (horizontal-separator.prototypes/separator-props-prototype separator-props)]
            [horizontal-separator separator-id separator-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [horizontal-separator]]))

(elements.api/horizontal-separator ...)
(horizontal-separator              ...)
```

</details>

---

### horizontal-spacer

```
@param (keyword)(opt) spacer-id
@param (map) spacer-props
{:class (keyword or keywords in vector)(opt)
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s
 :style (map)(opt)}
```

```
@usage
[horizontal-spacer {...}]
```

```
@usage
[horizontal-spacer :my-horizontal-spacer {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([spacer-props]
   [element (random/generate-keyword) spacer-props])

  ([spacer-id spacer-props]
   (fn [_ spacer-props]       (let [spacer-props (horizontal-spacer.prototypes/spacer-props-prototype spacer-props)]
            [:div (horizontal-spacer.attributes/spacer-attributes spacer-id spacer-props)]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [horizontal-spacer]]))

(elements.api/horizontal-spacer ...)
(horizontal-spacer              ...)
```

</details>

---

### icon

```
@param (keyword)(opt) icon-id
@param (map) icon-props
{:class (keyword or keywords in vector)(opt)
 :icon (keyword)
 :icon-color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :icon-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :m
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[icon {...}]
```

```
@usage
[icon :my-icon {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([icon-props]
   [element (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   (fn [_ icon-props]       (let [icon-props (icon.prototypes/icon-props-prototype icon-props)]
            [icon icon-id icon-props]))))
```

</details>

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
@warning
BUG#9912 (source-code/cljs/pretty_elements/button.views)
```

```
@param (keyword)(opt) button-id
@param (map) button-props
{:badge-color (keyword)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :badge-content (metamorphic-content)(opt)
 :badge-position (keyword)(opt)
  :tl, :tr, :br, :bl, :left, :right, :bottom, :top
  Default: :tr
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :cursor (keyword)(opt)
  :default, :disabled, :grab, :grabbing, :move, :pointer, :progress
  Default: :pointer
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :hover-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :hover-effect (keyword)(opt)
  :opacity
 :href (string)(opt)
 :icon (keyword)
 :icon-color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-filled
 :icon-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :m
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :keypress (map)(constant)(opt)
  {:exclusive? (boolean)(opt)
   :key-code (integer)
   :required? (boolean)(opt)
    Default: false}
 :label (metamorphic-content)(opt)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl, left, :right, bottom, :top
 :on-click (Re-Frame metamorphic-event)(opt)
 :on-mouse-over (Re-Frame metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :progress (percent)(opt)
 :progress-color (keyword or string)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :muted
 :progress-direction (keyword)(opt)
  :ltr, :rtl, :ttb, :btt
  Default: :ltr
 :progress-duration (ms)(opt)
  Default: 250
 :style (map)(opt)
 :target (keyword)(opt)
  :blank, :self
 :tooltip-content (metamorphic-content)(opt)
 :tooltip-position (keyword)(opt)
  :left, :right}
```

```
@usage
[icon-button {...}]
```

```
@usage
[icon-button :my-button {...}]
```

```
@usage
[icon-button {:keypress {:key-code 13} :on-click [:my-event]}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   (fn [_ button-props]       (let [button-props (icon-button.prototypes/button-props-prototype button-props)]
            [icon-button button-id button-props]))))
```

</details>

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
@param (keyword)(opt) image-id
@param (map) image-props
{:alt (string)(opt)
 :class (keyword or keywords in vector)(opt)
 :error-src (string)(opt)
  TODO
 :height (keyword)(opt)
  :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :lazy-load? (boolean)(opt)
  Default: false
  TODO
 :outdent (map)(opt)
  Same as the :indent property.
 :src (string)(opt)
 :style (map)(opt)
 :width (keyword)(opt)
  :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[image {...}]
```

```
@usage
[image :my-image {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([image-props]
   [element (random/generate-keyword) image-props])

  ([image-id image-props]
   (fn [_ image-props]       (let [image-props (image.prototypes/image-props-prototype image-props)]
            [image image-id image-props]))))
```

</details>

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
@param (keyword)(opt) label-id
@param (map) label-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  Default :medium
 :gap (keyword)(opt)
  Distance between the icon, the info-text button and the label
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
 :horizontal-position (keyword)(opt)
  :center, :left, :right
 :icon (keyword)(opt)
 :icon-color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :icon-position (keyword)(opt)
  :left, :right
  Default: :left
 :icon-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :line-height (keyword)(opt)
  :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl, left, :right, bottom, :top
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-copy (Re-Frame metamorphic-event)(opt)
  This event takes the label content as its last parameter
 :outdent (map)(opt)
  Same as the :indent property.
 :placeholder (metamorphic-content)(opt)
  Default: "\u00A0"
 :selectable? (boolean)(opt)
  Default: false
 :style (map)(opt)
 :target-id (keyword)(opt)
  The input element's ID, that you want to connect to the label with using the 'for' HTML attribute.
 :text-direction (keyword)(opt)
  :normal, :reversed
  Default :normal
 :text-overflow (keyword)(opt)
  :ellipsis, :hidden, :wrap
 :text-transform (keyword)(opt)
  :capitalize, :lowercase, :uppercase
 :tooltip-content (metamorphic-content)(opt)
 :tooltip-position (keyword)(opt)
  :left, :right
 :vertical-position (keyword)(opt)
  :bottom, :center, :top
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[label {...}]
```

```
@usage
[label :my-label {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([label-props]
   [element (random/generate-keyword) label-props])

  ([label-id label-props]
   (fn [_ label-props]       (let [label-props (label.prototypes/label-props-prototype label-props)]
            [label label-id label-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [label]]))

(elements.api/label ...)
(label              ...)
```

</details>

---

### menu-bar

```
@description
You can set the default item styles and settings by using the ':item-default'
property or you can specify these values on each item separately.
```

```
@param (keyword)(opt) bar-id
@param (map) bar-props
{:class (keyword or keywords in vector)(opt)
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
  W/ {:orientation :horizontal}
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :item-default (map)(opt)
  {:badge-position (keyword)(opt)
    :tl, :tr, :br, :bl, :left, :right, :bottom, :top
    Default: :tr
   :border-color (keyword or string)(opt)
    :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
   :border-radius (map)(opt)
   :border-width (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
   :color (keyword or string)(opt)
    :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
    Default: :inherit
   :fill-color (keyword or string)(opt)
    :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
   :font-size (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
    Default: :s
   :font-weight (keyword)(opt)
    :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
    Default :medium
   :hover-color (keyword or string)(opt)
    :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
   :hover-effect (keyword)(opt)
    :opacity
   :icon-color (keyword or string)(opt)
    :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
    Default: :inherit
   :icon-family (keyword)(opt)
    :material-symbols-filled, :material-symbols-outlined
    Default: :material-symbols-outlined
   :icon-size (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
    Default: :s
   :indent (map)(opt)
   :line-height (keyword)(opt)
    :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
    Default: :text-block
   :marker-color (keyword)(opt)
    :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
   :marker-position (keyword)(opt)
    :tl, :tr, :br, :bl, left, :right, bottom, :top
    Default: :tr
   :outdent (map)(opt)}
 :menu-items (maps in vector)
  [{:badge-color (keyword)(opt)
     :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
     Default: :primary
    :badge-content (metamorphic-content)(opt)
    :disabled? (boolean)(opt)
    :href (string)(opt)
    :icon (keyword)(opt)
    :label (metamorphic-content)(opt)
    :on-click (Re-Frame metamorphic-event)(opt)
    :on-mouse-over (Re-Frame metamorphic-event)(opt)
    :target (keyword)(opt)
     :blank, :self}]
 :orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :horizontal
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[menu-bar {...}]
```

```
@usage
[menu-bar :my-menu-bar {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([bar-props]
   [element (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   (fn [_ bar-props]       (let [bar-props (menu-bar.prototypes/bar-props-prototype bar-props)]
            [menu-bar bar-id bar-props]))))
```

</details>

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
@description
The 'multi-combo-box' element writes its actual value into the Re-Frame state
delayed after the user stopped typing or without a delay when the user
leaves the field!
```

```
@param (keyword)(opt) box-id
@param (map) box-props
{:chip-group (map)(opt)
  For more information check out the documentation of the chip-group element.
  {:deletable? (boolean)(opt)
    Default: true}
 :field-value-f (function)(opt)
  Default: return
 :initial-options (vector)(opt)
 :on-select (Re-Frame metamorphic-event)(opt)
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :option-component (Reagent component symbol)(opt)
  Default: elements.combo-box/default-option-component
 :options (vector)(opt)
 :options-path (Re-Frame path vector)(opt)}
```

```
@usage
[multi-combo-box {...}]
```

```
@usage
[multi-combo-box :my-multi-combo-box {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([box-props]
   [element (random/generate-keyword) box-props])

  ([box-id box-props]
   (fn [_ box-props]       (let [box-props (multi-combo-box.prototypes/box-props-prototype box-id box-props)
             box-props (assoc-in box-props [:surface :content] [combo-box.views/combo-box-surface-content box-id box-props])]
            [multi-combo-box box-id box-props]))))
```

</details>

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
@description
- The 'multi-field' element writes its actual value into the Re-Frame state delayed, after
  the user stopped typing or without a delay when the user leaves the field!
- In case of using the ':initial-options', ':options' or the ':options-path' properties, the 'multi-field'
  element implements the 'combo-box' element, otherwise it implements the 'text-field' element.
```

```
@param (keyword)(opt) group-id
@param (map) group-props
{:max-field-count (integer)(opt)
  Default: 8}
```

```
@usage
[multi-field {...}]
```

```
@usage
[multi-field :my-multi-field {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (fn [_ group-props]       (let [group-props (multi-field.prototypes/group-props-prototype group-id group-props)]
            [multi-field group-id group-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [multi-field]]))

(elements.api/multi-field ...)
(multi-field              ...)
```

</details>

---

### multiline-field

```
@description
The 'multiline-field' element writes its actual value into the Re-Frame state
delayed, after the user stopped typing or without a delay when the user leaves the field!
```

```
@param (keyword)(opt) field-id
@param (map) field-props
{:max-height (integer)(opt)
  TODO
  Max line count
  Default: 32
 :min-height (integer)(opt)
  TODO
  Min line count
  Default: 1}
```

```
@usage
[multiline-field {...}]
```

```
@usage
[multiline-field :my-multiline-field {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props]       (let [field-props (multiline-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/element field-id field-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [multiline-field]]))

(elements.api/multiline-field ...)
(multiline-field              ...)
```

</details>

---

### notification-bubble

```
@param (keyword) bubble-id
@param (map) bubble-props
{:border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  Default :medium
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :height (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :max-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :max-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :outdent (map)(opt)
  Same as the :indent property.
 :primary-button (map)(opt)
  {:layout (keyword)
    :button, :icon-button
    Default: :icon-button}
 :secondary-button (map)(opt)
  {:layout (keyword)
    :button, :icon-button
    Default: :icon-button}
 :selectable? (boolean)(opt)
  Default: false
 :style (map)(opt)
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[notification-bubble {...}]
```

```
@usage
[notification-bubble :my-notification-bubble {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([bubble-props]
   [element (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   (fn [_ bubble-props]       (let [bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-props)]
            [notification-bubble bubble-id bubble-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [notification-bubble]]))

(elements.api/notification-bubble ...)
(notification-bubble              ...)
```

</details>

---

### number-field

```
@description
The 'number-field' element writes its actual value into the Re-Frame state
delayed after the user stopped typing or without a delay when the user
leaves the field!
```

```
@param (keyword)(opt) field-id
@param (map) field-props
```

```
@usage
[number-field {...}]
```

```
@usage
[number-field :my-number-field {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props]       (let [field-props (number-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/element field-id field-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [number-field]]))

(elements.api/number-field ...)
(number-field              ...)
```

</details>

---

### password-field

```
@description
The 'password-field' element writes its actual value into the Re-Frame state
delayed after the user stopped typing or without a delay when the user
leaves the field!
```

```
@param (keyword)(opt) field-id
@param (map) field-props
```

```
@usage
[password-field {...}]
```

```
@usage
[password-field :my-password-field {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props]       (let [field-props (password-field.prototypes/field-props-prototype field-id field-props)]
            [password-field field-id field-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [password-field]]))

(elements.api/password-field ...)
(password-field              ...)
```

</details>

---

### plain-field

```
@description
The 'plain-field' element writes its actual value into the Re-Frame state
delayed after the user stopped typing or without a delay when the user
leaves the field!
```

```
@param (keyword)(opt) field-id
@param (map) field-props
{:autoclear? (boolean)(opt)
  Removes the value stored in the application state (on the value-path)
  when the element unmounts.
 :autofocus? (boolean)(opt)
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :field-content-f (function)(opt)
  Default: return
 :field-value-f (function)(opt)
  Default: return
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :initial-value (string)(opt)
 :modifier-f (function)(opt)
 :on-blur (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-change-f (function)(opt)
  This function takes the field-id, the field-props and the change event as its parameters.
 :on-changed (Re-Frame metamorphic-event)(opt)
  It happens BEFORE the application state gets updated with the actual value!
  If you want to get the ACTUAL value from the application state, use the
  :on-type-ended event instead!
  This event takes the field content as its last parameter.
 :on-focus (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-mount (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-type-ended (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-unmount (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :surface (map)(opt)
  {:border-radius (map)(opt)
   :content (metamorphic-content)(opt)
   :indent (map)(opt)}
 :value-path (Re-Frame path vector)(opt)}
```

```
@usage
[plain-field {...}]
```

```
@usage
[plain-field :my-plain-field {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props]       (let [field-props (plain-field.prototypes/field-props-prototype field-id field-props)]
            [plain-field field-id field-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [plain-field]]))

(elements.api/plain-field ...)
(plain-field              ...)
```

</details>

---

### radio-button

```
@param (keyword) button-id
@param (map) button-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  Default: {:all :m}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xs
 :class (keyword or keywords in vector)(opt)
 :default-value (*)(opt)
 :deselectable? (boolean)(opt)
 :disabled? (boolean)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :initial-options (vector)(opt)
 :initial-value (*)(opt)
 :label (metamorphic-content)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :on-select (Re-Frame metamorphic-event)(opt)
 :option-helper-f (function)(opt)
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :vertical
 :options-path (Re-Frame path vector)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :value-path (Re-Frame path vector)(opt)}
```

```
@usage
[radio-button {...}]
```

```
@usage
[radio-button :my-radio-button {...}]
```

```
@usage
[radio-button :my-radio-button {:options [{:value :foo :label "Foo"} {:value :bar :label "Bar"}]}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   (fn [_ button-props]       (let [button-props (radio-button.prototypes/button-props-prototype button-id button-props)]
            [radio-button button-id button-props]))))
```

</details>

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
@param (keyword)(opt) row-id
@param (map) row-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :gap (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
 :height (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto
 :horizontal-align (keyword)(opt)
  :center, :left, :right, :space-around, :space-between, :space-evenly
  Default: :left
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :max-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :max-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :vertical-align (keyword)(opt)
  :top, :center, :bottom
  Default: :center
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content
 :wrap-items? (boolean)(opt)}
```

```
@usage
[row {...}]
```

```
@usage
[row :my-row {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([row-props]
   [element (random/generate-keyword) row-props])

  ([row-id row-props]
   (fn [_ row-props]       (let [row-props (row.prototypes/row-props-prototype row-props)]
            [row row-id row-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [row]]))

(elements.api/row ...)
(row              ...)
```

</details>

---

### search-field

```
@description
The 'search-field' element writes its actual value into the Re-Frame state
delayed after the user stopped typing or without a delay when the user
leaves the field!
```

```
@param (keyword)(opt) field-id
@param (map) field-props
```

```
@usage
[search-field {...}]
```

```
@usage
[search-field :my-search-field {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props]       (let [field-props (search-field.prototypes/field-props-prototype field-id field-props)]
            [text-field.views/element field-id field-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [search-field]]))

(elements.api/search-field ...)
(search-field              ...)
```

</details>

---

### select

```
@param (keyword)(opt) select-id
@param (map) select-props
{:add-option-f (function)(opt)
  Default: return
 :autoclear? (boolean)(opt)
  Removes the value stored in the application state (on the value-path)
  when the element unmounts.
 :extendable? (boolean)(opt)
 :helper (metamorphic-content)(opt)
 :info-text (metamorphic-content)(opt)
 :initial-options (vector)(opt)
 :initial-value (*)(opt)
 :label (metamorphic-content)(opt)
  Label of the button if the :layout property is :button or :icon-button,
  otherwise it is displayed as the element label above the select button.
 :layout (keyword)(opt)
  :button, :icon-button, :select-button
  Default: :select-button
 :on-select (Re-Frame metamorphic-event)(opt)
 :option-field-placeholder (metamorphic-content)(opt)
  Default: :add!
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-label (metamorphic-content)(opt)
 :options-path (Re-Frame path vector)(opt)
 :options-placeholder (metamorphic-content)(opt)
  Default: :no-options
 :popup (map)(opt)
  {:border-color (keyword or string)(opt)
   :border-position (keyword)(opt)
   :border-radius (map)(opt)
   :border-width (keyword)(opt)
   :cover-color (keyword or string)(opt)
    Default: :black
   :fill-color (keyword or string)(opt)
    Default: :default
   :indent (map)(opt)
   :label (metamorphic-content)(opt)
   :min-width (keyword)(opt)
   :outdent (map)(opt)}
 :reveal-effect (keyword)(opt)
  :delayed, :opacity
 :value-path (Re-Frame path vector)(opt)}
```

```
@usage
[select {...}]
```

```
@usage
[select :my-select {...}]
```

```
@usage
[select {:icon         :sort
         :layout       :icon-button
         :options-path [:my-options]
         :value-path   [:my-selected-option]}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([select-props]
   [element (random/generate-keyword) select-props])

  ([select-id select-props]
   (fn [_ select-props]       (let [select-props (select.prototypes/select-props-prototype select-id select-props)]
            [select select-id select-props]))))
```

</details>

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
@warning
UNFINISHED! DO NOT USE!
```

```
@param (keyword)(opt) slider-id
@param (map) slider-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :initial-value (vector)(opt)
  Default: [0 100]
 :label (metamorphic-content)(opt)
 :max-value (integer)(opt)
  Default: 100
 :min-value (integer)(opt)
  Default: 0
 :outdent (map)(opt)
  Same as the :indent property.
 :resetable? (boolean)(opt)
 :style (map)(opt)
 :value-path (Re-Frame path vector)(opt)
 :width (keyword)(opt)
  :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto}
```

```
@usage
[slider {...}]
```

```
@usage
[slider :my-slider {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([slider-props]
   [element (random/generate-keyword) slider-props])

  ([slider-id slider-props]
   (fn [_ slider-props]       (let [slider-props (slider.prototypes/slider-props-prototype slider-id slider-props)]
            [slider slider-id slider-props]))))
```

</details>

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
@warning
UNFINISHED! DO NOT USE!
```

```
@param (keyword)(opt) stepper-id
@param (map) stepper-props
{}
```

```
@usage
[stepper {...}]
```

```
@usage
[stepper :my-stepper {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([stepper-props]
   [element (random/generate-keyword) stepper-props])

  ([stepper-id stepper-props]
   (fn [_ stepper-props]       (let []            [stepper stepper-id stepper-props]))))
```

</details>

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
@param (keyword)(opt) switch-id
@param (map) switch-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  Default: {:all :m}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xs
 :class (keyword or keywords in vector)(opt)
 :default-value (boolean)(opt)
 :disabled? (boolean)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :initial-options (vector)(opt)
 :initial-value (boolean)(opt)
 :label (metamorphic-content)(opt)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :on-check (Re-Frame metamorphic-event)(opt)
 :on-uncheck (Re-Frame metamorphic-event)(opt)
 :option-helper-f (function)(opt)
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :vertical
 :options-path (Re-Frame path vector)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :value-path (Re-Frame path vector)(opt)}
```

```
@usage
[switch {...}]
```

```
@usage
[switch :my-switch {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([switch-props]
   [element (random/generate-keyword) switch-props])

  ([switch-id switch-props]
   (fn [_ switch-props]       (let [switch-props (switch.prototypes/switch-props-prototype switch-id switch-props)]
            [switch switch-id switch-props]))))
```

</details>

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
@param (keyword)(opt) text-id
@param (map) text-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :content (metamorphic-content)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  Default: :normal
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
 :horizontal-position (keyword)(opt)
  :center, :left, :right
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :line-height (keyword)(opt)
  :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :max-lines (integer)(opt)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-copy (Re-Frame metamorphic-event)(opt)
  This event takes the text content as its last parameter.
 :outdent (map)(opt)
  Same as the :indent property.
 :placeholder (metamorphic-content)(opt)
  Default: "\u00A0"
 :selectable? (boolean)(opt)
  Default: true
 :style (map)(opt)
 :text-direction (keyword)(opt)
  :normal, :reversed
  Default :normal
 :text-overflow (keyword)(opt)
  :ellipsis, :wrap
  Default: :wrap
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[text {...}]
```

```
@usage
[text :my-text {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([text-props]
   [element (random/generate-keyword) text-props])

  ([text-id text-props]
   (fn [_ text-props]       (let [text-props (text.prototypes/text-props-prototype text-props)]
            [text text-id text-props]))))
```

</details>

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
@description
The 'text-field' element writes its actual value into the Re-Frame state delayed, after
the user stopped typing or without a delay when the user leaves the field!
```

```
@param (keyword)(opt) field-id
@param (map) field-props
{:autoclear? (boolean)(opt)
  Removes the value stored in the application state (on the value-path)
  when the element unmounts.
 :autofill-name (keyword)(opt)
  Helps the browser on what values to be suggested.
  Leave empty if you don't want autosuggestions.
 :autofocus? (boolean)(opt)
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :emptiable? (boolean)(opt)
 :end-adornments (maps in vector)(opt)
  [{:click-effect (keyword)(opt)
     :opacity
     Default: :opacity
    :color (keyword)(opt)
     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
     Default: :default
    :disabled? (boolean)(opt)
    :hover-effect (keyword)(opt)
     :opacity
    :icon (keyword)
    :icon-family (keyword)(opt)
     :material-symbols-filled, :material-symbols-outlined
     Default: :material-symbols-outlined
    :label (string)(opt)
    :on-click (Re-Frame metamorphic-event)(opt)
    :tab-indexed? (boolean)(opt)
     Default: true
    :timeout (ms)(opt)
     Disables the adornment for a specific time after the on-click event fired.
    :tooltip-content (metamorphic-content)(opt)}]
 :field-content-f (function)(opt)
  From application state to field content modifier function.
  Default: return
 :field-value-f (function)(opt)
  From field content to application state modifier function.
  Default: return
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  Default: :normal
 :form-id (keyword)(opt)
  Different inputs with the same form ID could be validated at the same time.
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :initial-value (string)(opt)
 :label (metamorphic-content)(opt)
 :line-height (keyword or px)(opt)
  :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl, left, :right, bottom, :top
 :max-length (integer)(opt)
 :modifier-f (function)(opt)
 :on-blur (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-changed (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
  It happens BEFORE the application state gets updated with the actual value!
  If you want to get the ACTUAL value read from the application state, use the
  :on-type-ended event instead!
 :on-empty (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-enter (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-focus (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-invalid (Re-Frame metamorphic-event)(opt)
  This event takes the field content and the invalid message as its last parameter.
 :on-mount (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-type-ended (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-unmount (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :on-valid (Re-Frame metamorphic-event)(opt)
  This event takes the field content as its last parameter.
 :outdent (map)(opt)
  Same as the :indent property.
 :placeholder (metamorphic-content)(opt)
 :reveal-effect (keyword)(opt)
  :delayed, :opacity
 :start-adornments (maps in vector)(opt)
  Same as the :end-adornments property.
 :style (map)(opt)
 :surface (map)(opt)
  {:border-radius (map)(opt)
   :content (metamorphic-content)(opt)
   :indent (map)(opt)}
 :type (keyword)(opt)
  :email, :number, :password, :tel, :text
  Default: :text
 :validate-when-change? (boolean)(opt)
  Validates the value when it changes.
 :validate-when-leave? (boolean)(opt)
  Validates the value and turns on the validation when the user leaves the field.
 :validators (maps in vector)(opt)
 [{:f (function)
   :invalid-message (metamorphic-content)(opt)}]
 :value-path (Re-Frame path vector)(opt)
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[text-field {...}]
```

```
@usage
[text-field :my-text-field {...}]
```

```
@usage
(defn my-surface [field-id])
[text-field {:surface #'my-surface}]
```

```
@usage
(defn my-surface [field-id])
[text-field {:surface {:content #'my-surface}}]
```

```
@usage
[text-field {:modifier-f clojure.string/lower-case}]
```

```
@usage
[text-field {:validators []}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([field-props]
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   (fn [_ field-props]       (let [field-props (text-field.prototypes/field-props-prototype field-id field-props)]
            [text-field field-id field-props]))))
```

</details>

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
@param (keyword)(opt) thumbnail-id
@param (map) thumbnail-props
{:background-size (keyword)(opt)
  :contain, :cover
  Default: :contain
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  Default: {:all :m}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s
 :helper (metamorphic-content)(opt)
 :href (string)(opt)
 :icon (keyword)(opt)
  Default: :icon
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :on-click (Re-Frame metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :target (keyword)(opt)
  :blank, :self
 :uri (string)(opt)
 :width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s}
```

```
@usage
[thumbnail {...}]
```

```
@usage
[thumbnail :my-thumbnail {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([thumbnail-props]
   [element (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   (fn [_ thumbnail-props]       (let [thumbnail-props (thumbnail.prototypes/thumbnail-props-prototype thumbnail-props)]
            [thumbnail thumbnail-id thumbnail-props]))))
```

</details>

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
@param (keyword)(opt) toggle-id
@param (map) toggle-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :cursor (keyword)(opt)
  :default, :disabled, :grab, :grabbing, :move, :pointer, :progress
  Default: :pointer
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :height (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto
 :hover-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :hover-effect (keyword)(opt)
  :opacity
 :href (string)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl, left, :right, bottom, :top
 :on-click (Re-Frame metamorphic-event)(opt)
 :on-right-click (Re-Frame metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :target (keyword)(opt)
  :blank, :self
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[toggle {...}]
```

```
@usage
[toggle :my-toggle {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([toggle-props]
   [element (random/generate-keyword) toggle-props])

  ([toggle-id toggle-props]
   (fn [_ toggle-props]       (let [toggle-props (toggle.prototypes/toggle-props-prototype toggle-props)]
            [toggle toggle-id toggle-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [toggle]]))

(elements.api/toggle ...)
(toggle              ...)
```

</details>

---

### vertical-group

```
@param (keyword)(opt) group-id
@param (map) group-props
{:class (keyword or keywords in vector)(opt)
 :default-props (map)(opt)
 :element (Reagent component symbol)
 :group-items (maps in vector)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :width (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :content}
```

```
@usage
[vertical-group {...}]
```

```
@usage
[vertical-group :my-vertical-group {...}]
```

```
@usage
[vertical-group {:default-props {:hover-color :highlight}
                 :element #'elements.api/button
                 :group-items [{:label "First button"  :href "/first"}
                               {:label "Second button" :href "/second"}]}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (fn [_ group-props]       (let [group-props (vertical-group.prototypes/group-props-prototype group-props)]
            [vertical-group group-id group-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [vertical-group]]))

(elements.api/vertical-group ...)
(vertical-group              ...)
```

</details>

---

### vertical-line

```
@param (keyword)(opt) line-id
@param (map) line-props
{:class (keyword or keywords in vector)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :height (keyword)(opt)
  :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :parent
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :strength (px)(opt)
  Default: 1}
```

```
@usage
[vertical-line {...}]
```

```
@usage
[vertical-line :my-vertical-line {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   (fn [_ line-props]       (let [line-props (vertical-line.prototypes/line-props-prototype line-props)]
            [:div (vertical-line.attributes/line-attributes line-id line-props)
                  [:div (vertical-line.attributes/line-body-attributes line-id line-props)]]))))
```

</details>

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
@param (keyword)(opt) polarity-id
@param (map) polarity-props
{:class (keyword or keywords in vector)(opt)
 :end-content (metamorphic-content)
 :height (keyword)(opt)
  :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :parent
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :middle-content (metamorphic-content)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :start-content (metamorphic-content)(opt)
```

```
@usage
[vertical-polarity {...}]
```

```
@usage
[vertical-polarity :my-vertical-polarity {...}]
```

```
@usage
[vertical-polarity {:start-content [:<> [label {:content "My label"}]
                                        [label {:content "My label"}]]}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([polarity-props]
   [element (random/generate-keyword) polarity-props])

  ([polarity-id polarity-props]
   (fn [_ polarity-props]       (let [polarity-props (vertical-polarity.prototypes/polarity-props-prototype polarity-props)]
            [vertical-polarity polarity-id polarity-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [vertical-polarity]]))

(elements.api/vertical-polarity ...)
(vertical-polarity              ...)
```

</details>

---

### vertical-spacer

```
@param (keyword)(opt) spacer-id
@param (map) spacer-props
{:class (keyword or keywords in vector)(opt)
 :style (map)(opt)
 :width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s}
```

```
@usage
[vertical-spacer {...}]
```

```
@usage
[vertical-spacer :my-vertical-spacer {...}]
```

<details>
<summary>Source code</summary>

```
(defn element
  ([spacer-props]
   [element (random/generate-keyword) spacer-props])

  ([spacer-id spacer-props]
   (fn [_ spacer-props]       (let [spacer-props (vertical-spacer.prototypes/spacer-props-prototype spacer-props)]
            [:div (vertical-spacer.attributes/spacer-attributes spacer-id spacer-props)]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [elements.api :refer [vertical-spacer]]))

(elements.api/vertical-spacer ...)
(vertical-spacer              ...)
```

</details>

---

This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.

