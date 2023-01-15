
# elements.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > elements.api

### Index

- [anchor](#anchor)

- [blank](#blank)

- [breadcrumbs](#breadcrumbs)

- [button](#button)

- [card](#card)

- [checkbox](#checkbox)

- [chip](#chip)

- [chip-group](#chip-group)

- [circle-diagram](#circle-diagram)

- [color-selector](#color-selector)

- [column](#column)

- [combo-box](#combo-box)

- [counter](#counter)

- [date-field](#date-field)

- [digit-field](#digit-field)

- [element-label](#element-label)

- [expandable](#expandable)

- [ghost](#ghost)

- [horizontal-polarity](#horizontal-polarity)

- [horizontal-separator](#horizontal-separator)

- [icon](#icon)

- [icon-button](#icon-button)

- [image](#image)

- [label](#label)

- [line-diagram](#line-diagram)

- [menu-bar](#menu-bar)

- [multi-combo-box](#multi-combo-box)

- [multi-field](#multi-field)

- [multiline-field](#multiline-field)

- [notification-bubble](#notification-bubble)

- [password-field](#password-field)

- [plain-field](#plain-field)

- [point-diagram](#point-diagram)

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

- [vertical-polarity](#vertical-polarity)

- [vertical-separator](#vertical-separator)

### anchor

```
@param (keyword)(opt) anchor-id
@param (map) anchor-props
{:color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  Default: :medium
 :href (string)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :line-height (keyword)(opt)
  :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :on-click (metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[anchor {...}]
```

```
@usage
[anchor :my-anchor {...}]
```

---

### blank

```
@param (keyword)(opt) blank-id
@param (map) blank-props
{:content (metamorphic-content)(opt)
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}:style (map)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[blank {...}]
```

```
@usage
[blank :my-blank {...}]
```

---

### breadcrumbs

```
@param (keyword)(opt) breadcrumbs-id
@param (map) breadcrumbs-props
{:class (keyword or keywords in vector)(opt)
 :crumbs (maps in vector)
  [{:label (metamorphic-content)(opt)
    :placeholder (metamorphic-content)(opt)
    :route (string)(opt)}]
 :disabled? (boolean)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  Default: :s
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
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

---

### button

```
@param (keyword)(opt) button-id
@param (map) button-props
{:badge-color (keyword)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :primary
  W/ {:badge-content ...}
 :badge-content (metamorphic-content)(opt)
 :badge-position (keyword)(opt)
  :tl, :tr, :br, :bl
  Default: :tr
  W/ {:badge-content ...}
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  Default: :medium
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :hover-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
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
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :keypress (map)(opt)
  {:key-code (integer)
   :required? (boolean)(opt)
    Default: false}
 :label (metamorphic-content)(opt)
 :line-height (keyword)(opt)
  :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl
 :on-click (metamorphic handler)(opt)
 :on-mouse-over (metamorphic handler)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :tooltip (metamorphic-content)(opt)
 :tooltip-position (keyword)(opt)
  :left, :right}
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

---

### card

```
@param (keyword)(opt) card-id
@param (map) card-props
{:badge-color (keyword)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :primary
  W/ {:badge-content ...}
 :badge-content (metamorphic-content)(opt)
 :badge-position (keyword)(opt)
  :tl, :tr, :br, :bl
  Default: :tr
  W/ {:badge-content ...}
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :content (metamorphic-content)(opt)
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
 :hover-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-click (metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :stretch-orientation (keyword)(opt)
  :horizontal, :vertical, :both
 :style (map)(opt)}
```

```
@usage
[card {...}]
```

```
@usage
[card :my-card {...}]
```

---

### checkbox

```
@param (keyword)(opt) checkbox-id
@param (map) checkbox-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xs
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
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :initial-options (vector)(opt)
 :initial-value (boolean)(opt)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :on-check (metamorphic-event)(opt)
 :on-uncheck (metamorphic-event)(opt)
 :option-helper-f (function)(opt)
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :vertical
 :options-path (vector)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :value-path (vector)(opt)}
```

```
@usage
[checkbox {...}]
```

```
@usage
[checkbox :my-checkbox {...}]
```

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
 :icon (keyword)(opt)
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :label (metamorphic-content)
 :on-click (metamorphic-event)(opt)
  TODO Makes the chip element clickable
 :outdent (map)(opt)
  Same as the :indent property.
 :primary-button (map)(opt)
  {:icon (keyword)
   :icon-family (keyword)(opt)
    :material-symbols-filled, :material-symbols-outlined
    Default: :material-symbols-outlined
   :on-click (metamorphic-event)}
 :style (map)(opt)}
```

```
@usage
[chip {...}]
```

```
@usage
[chip :my-chip {...}]
```

---

### chip-group

```
@param (keyword)(opt) group-id
@param (map) group-props
{:class (keyword or keywords in vector)(opt)
 :chip-label-f (function)(opt)
  Default: return
 :deletable? (boolean)(opt)
  Default: false
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :placeholder (metamorphic-content)(opt)
 :style (map)(opt)
 :value-path (vector)(opt)}
```

```
@usage
[chip-group {...}]
```

```
@usage
[chip-group :my-chip-group {...}]
```

---

### circle-diagram

```
@param (keyword)(opt) diagram-id
@param (map) diagram-props
{:class (keyword or keywords in vector)(opt)
 :diameter (px)(opt)
  Default: 48
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :sections (maps in vector)}
  [{:color (keyword or string)
     :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
    :label (metamorphic-content)(opt)
     TODO
    :value (integer)}]
 :strength (px)(opt)
   Default: 2
   Min: 1
   Max: 6
 :style (map)(opt)}
```

```
@usage
[circle-diagram {...}]
```

```
@usage
[circle-diagram :my-circle-diagram {...}]
```

---

### color-selector

```
@param (keyword)(opt) selector-id
@param (map) selector-props
{:class (keyword or keywords in vector)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :on-select (metamorphic-event)(opt)
 :options (strings in vector)(opt)
 :options-label (metamorphic-content)(opt)
 :options-path (vector)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :value-path (vector)(opt)}
```

```
@usage
[color-selector {...}]
```

```
@usage
[color-selector :my-color-selector {...}]
```

---

### column

```
@param (keyword)(opt) column-id
@param (map) column-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :gap (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :stretch-orientation (keyword)(opt)
  :horizontal, :vertical, :both, :none
  Default: :vertical
 :vertical-align (keyword)(opt)
  :top, :center, :bottom, :space-around, :space-between, :space-evenly
  Default: :top
 :wrap-items? (boolean)(opt)
  Default: false}
```

```
@usage
[elements/column {...}]
```

```
@usage
[elements/column :my-column {...}]
```

---

### combo-box

```
@param (keyword)(opt) box-id
@param (map) box-props
{:field-content-f (function)(opt)
  Default: return
 :field-value-f (function)(opt)
  Default: return
 :initial-options (vector)(opt)
 :on-select (metamorphic-event)(opt)
 :option-component (component)(opt)
  Default: elements.combo-box.views/default-option-component
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-path (vector)(opt)}
```

```
@usage
[combo-box {...}]
```

```
@usage
[combo-box :my-combo-box {...}]
```

---

### counter

```
@param (keyword)(opt) counter-id
@param (map) counter-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :m
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xs
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
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
 :value-path (vector)(opt)}
```

```
@usage
[counter {...}]
```

```
@usage
[counter :my-counter {...}]
```

---

### date-field

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

---

### digit-field

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
 :value-path (vector)}
```

```
@usage
[digit-field {...}]
```

```
@usage
[digit-field :my-digit-field {...}]
```

---

### element-label

```
@param (keyword) element-id
@param (map) element-props
{:helper (metamorphic-content)(opt)
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :marker-color (keyword)(opt)}
```

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
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
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

---

### ghost

```
@param (keyword)(opt) ghost-id
@param (map) ghost-props
{:border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
 :class (keyword or keywords in vector)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[ghost {...}]
```

```
@usage
[ghost :my-ghost {...}]
```

---

### horizontal-polarity

```
@param (keyword)(opt) polarity-id
@param (map) polarity-props
{:class (keyword or keywords in vector)(opt)
 :end-content (metamorphic-content)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :middle-content (metamorphic-content)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :start-content (metamorphic-content)(opt)
 :vertical-align (keyword)(opt)
  :bottom, :center, :top
  Default: :center}
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

---

### horizontal-separator

```
@param (keyword)(opt) separator-id
@param (map) separator-props
{:height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s}
```

```
@usage
[horizontal-separator {...}]
```

```
@usage
[horizontal-separator :my-horizontal-separator {...}]
```

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
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
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

---

### icon-button

```
@param (keyword)(opt) button-id
@param (map) button-props
{:badge-color (keyword)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :primary
  W/ {:badge-content ...}
 :badge-content (metamorphic-content)(opt)
 :badge-position (keyword)(opt)
  :tl, :tr, :br, :bl
  Default: :tr
  W/ {:badge-content ...}
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :height (keyword)(opt)
  :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :3xl
 :hover-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :icon (keyword)
 :icon-color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-filled
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :keypress (map)(constant)(opt)
  {:key-code (integer)
   :required? (boolean)(opt)
    Default: false}
 :label (metamorphic-content)(opt)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl
 :on-click (metamorphic handler)(opt)
 :on-mouse-over (metamorphic handler)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :progress (percent)(opt)
 :progress-duration (ms)(opt)
  W/ {:progress ...}
 :style (map)(opt)
 :tooltip (metamorphic-content)(opt)
 :tooltip-position (keyword)(opt)
  :left, :right
 :width (keyword)(opt)
  :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :3xl}
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

---

### image

```
@param (keyword)(opt) image-id
@param (map) image-props
{:alt (string)(opt)
 :class (keyword or keywords in vector)(opt)
 :error-src (string)(opt)
  TODO
 :height (string)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :lazy-loading? (boolean)(opt)
  Default: false
  TODO
 :outdent (map)(opt)
  Same as the :indent property.
 :src (string)(opt)
 :style (map)(opt)
 :width (string)(opt)}
```

```
@usage
[image {...}]
```

```
@usage
[image :my-image {...}]
```

---

### label

```
@param (keyword)(opt) label-id
@param (map) label-props
{:class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :inherit
 :content (metamorphic-content)
 :copyable? (boolean)(opt)
  Default: false
 :disabled? (boolean)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  Default :medium
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
 :icon-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :line-height (keyword)(opt)
  :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :outdent (map)(opt)
  Same as the :indent property.
 :placeholder (metamorphic-content)(opt)
 :selectable? (boolean)(opt)
  Default: false
 :style (map)(opt)
 :target-id (keyword)(opt)
  The input element's id, that you want to connect with the label.
 :vertical-position (keyword)(opt)
  :bottom, :center, :top
 :text-direction (keyword)(opt)
  :normal, :reversed
  Default :normal
 :text-overflow (keyword)(opt)
  :ellipsis, :wrap
  Default: :ellipsis}
```

```
@usage
[label {...}]
```

```
@usage
[label :my-label {...}]
```

---

### line-diagram

```
@param (keyword)(opt) diagram-id
@param (map) diagram-props
{:indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :outdent (map)(opt)
  Same as the :indent property.
 :sections (maps in vector)}
  [{:color (keyword or string)(opt)
     :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
     Default: primary
    :label (metamorphic-content)(opt)
     TODO
    :value (integer)}]
 :strength (px)(opt)
   Default: 2
   Min: 1
   Max: 6
 :total-value (integer)(opt)
  Default: A szakaszok aktuális értékének összege}
```

```
@usage
[line-diagram {...}]
```

```
@usage
[line-diagram :my-line-diagram {...}]
```

---

### menu-bar

```
@param (keyword)(opt) bar-id
@param (map) bar-props
{:class (keyword or keywords in vector)(opt)
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
  W/ {:orientation :horizontal}
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  Default :medium
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxl
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :menu-items (maps in vector)
  [{:active? (boolean)(opt)
     Default: false
    :badge-color (keyword)(opt)
     :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
     Default: :primary
     W/ {:badge-content ...}
    :badge-content (metamorphic-content)(opt)
    :badge-position (keyword)(opt)
     :tl, :tr, :br, :bl
     Default: :tr
     W/ {:badge-content ...}
    :disabled? (boolean)(opt)
    :href (string)(opt)
    :icon (keyword)(opt)
    :icon-family (keyword)(opt)
     :material-symbols-filled, :material-symbols-outlined
     Default: :material-symbols-outlined
    :label (metamorphic-content)(opt)
    :marker-color (keyword)(opt)
     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
    :marker-position (keyword)(opt)
     :tl, :tr, :br, :bl
     Default: :tr
     W/ {:marker-color ...}
    :on-click (metamorphic-event)(opt)}]
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

---

### multi-combo-box

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
 :on-select (metamorphic-event)(opt)
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :option-component (component)(opt)
  Default: elements.combo-box/default-option-component
 :options (vector)(opt)
 :options-path (vector)(opt)
 :placeholder (metamorphic-content)(opt)}
```

```
@usage
[multi-combo-box {...}]
```

```
@usage
[multi-combo-box :my-multi-combo-box {...}]
```

---

### multi-field

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

---

### multiline-field

```
@param (keyword)(opt) field-id
@param (map) field-props
{:max-height (integer)(opt)
  TODO
  Max lines count
  Default: 32
 :min-height (integer)(opt)
  TODO
  Min lines count
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

---

### notification-bubble

```
@param (keyword) bubble-id
@param (map) bubble-props
{:border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
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
  :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  Default :medium
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
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
 :style (map)(opt)}
```

```
@usage
[notification-bubble {...}]
```

```
@usage
[notification-bubble :my-notification-bubble {...}]
```

---

### password-field

```
@param (keyword)(opt) field-id
@param (map) field-props
{:validate? (boolean)(opt)
  Default: false}
```

```
@usage
[password-field {...}]
```

```
@usage
[password-field :my-password-field {...}]
```

---

### plain-field

```
@param (keyword)(opt) field-id
@param (map) field-props
{:autoclear? (boolean)(opt)
 :autofocus? (boolean)(opt)
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :field-content-f (function)(opt)
  Default: return
 :field-value-f (function)(opt)
  Default: return
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :initial-value (string)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :modifier (function)(opt)
 :on-blur (metamorphic-event)(opt)
 :on-changed (metamorphic-event)(opt)
  It happens BEFORE the application state gets updated with the actual value!
  If you have to get the ACTUAL value from the application state, use the
  :on-type-ended event instead!
  This event takes the field content as its last parameter
 :on-focus (metamorphic-event)(opt)
 :on-mount (metamorphic-event)(opt)
  This event takes the field content as its last parameter
 :on-type-ended (metamorphic-event)(opt)
  This event takes the field content as its last parameter
 :on-unmount (metamorphic-event)(opt)
  This event takes the field content as its last parameter
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :surface (metamorphic-content)(opt)
 :value-path (vector)(opt)}
```

```
@usage
[plain-field {...}]
```

```
@usage
[plain-field :my-plain-field {...}]
```

---

### point-diagram

```
@param (keyword)(opt) diagram-id
@param (map) diagram-props
{:color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
  W/ {:label ...}
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :outdent (map)(opt)
  Same as the :indent property.
 :points (integers in vector)
 :strength (px)(opt)
   Default: 2
   Min: 1
   Max: 6}
```

```
@usage
[line-diagram {...}]
```

```
@usage
[point-diagram :my-point-diagram {...}]
```

---

### radio-button

```
@param (keyword) button-id
@param (map) button-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :m
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
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :initial-options (vector)(opt)
 :initial-value (*)(opt)
 :label (metamorphic-content)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :on-select (metamorphic-event)(opt)
 :option-helper-f (function)(opt)
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :vertical
 :options-path (vector)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :value-path (vector)(opt)}
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

---

### row

```
@param (keyword)(opt) row-id
@param (map) row-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :gap (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
 :horizontal-align (keyword)(opt)
  :center, :left, :right, :space-around, :space-between, :space-evenly
  Default: :left
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :stretch-orientation (keyword)(opt)
  :horizontal, :vertical, :both, :none
  Default: :horizontal
 :vertical-align (keyword)(opt)
  :top, :center, :bottom
  Default: :center
 :wrap-items? (boolean)(opt)
  Default: true}
```

```
@usage
[row {...}]
```

```
@usage
[row :my-row {...}]
```

---

### search-field

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

---

### select

```
@param (keyword)(opt) select-id
@param (map) select-props
{:add-option-f (function)(opt)
  Default: return
 :autoclear? (boolean)(opt)
 :border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :extendable? (boolean)(opt)
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :initial-options (vector)(opt)
 :initial-value (*)(opt)
 :label (metamorphic-content)(opt)
 :layout (keyword)(opt)
  :button, :icon-button, :select
  Default: :select
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-select (metamorphic-event)(opt)
 :option-field-placeholder (metamorphic-content)(opt)
  Default: :add!
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-label (metamorphic-content)(opt)
 :options-path (vector)(opt)
 :options-placeholder (metamorphic-content)(opt)
  Default: :no-options
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :value-path (vector)(opt)}
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

---

### slider

```
@param (keyword)(opt) slider-id
@param (map) slider-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
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
 :value-path (vector)(opt)}
```

```
@usage
[slider {...}]
```

```
@usage
[slider :my-slider {...}]
```

---

### stepper

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

---

### switch

```
@param (keyword)(opt) switch-id
@param (map) switch-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :m
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
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :initial-options (vector)(opt)
 :initial-value (boolean)(opt)
 :label (metamorphic-content)(opt)
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :on-check (metamorphic-event)(opt)
 :on-uncheck (metamorphic-event)(opt)
 :option-helper-f (function)(opt)
 :option-label-f (function)(opt)
  Default: return
 :option-value-f (function)(opt)
  Default: return
 :options (vector)(opt)
 :options-orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :vertical
 :options-path (vector)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
 :value-path (vector)(opt)}
```

```
@usage
[switch {...}]
```

```
@usage
[switch :my-switch {...}]
```

---

### text

```
@param (keyword)(opt) text-id
@param (map) text-props
{:class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :content (metamorphic-content)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  Default: :normal
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :line-height (keyword)(opt)
  :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :max-lines (integer)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :placeholder (metamorphic-content)(opt)
 :selectable? (boolean)(opt)
  Default: true
 :style (map)(opt)
 :text-direction (keyword)(opt)
  :normal, :reversed
  Default :normal
 :text-overflow (keyword)(opt)
  :ellipsis, :wrap
  Default: :wrap}
```

```
@usage
[text {...}]
```

```
@usage
[text :my-text {...}]
```

---

### text-field

```
@param (keyword)(opt) field-id
@param (map) field-props
{:autoclear? (boolean)(opt)
 :autofill-name (keyword)(opt)
 :autofocus? (boolean)(opt)
 :border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :emptiable? (boolean)(opt)
 :end-adornments (maps in vector)(opt)
  [{:color (keyword)(opt)
     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
     Default: :default
    :disabled? (boolean)(opt)
    :icon (keyword)
    :icon-family (keyword)(opt)
     :material-symbols-filled, :material-symbols-outlined
     Default: :material-symbols-outlined
    :label (string)(opt)
    :on-click (metamorphic-event)(opt)
    :tab-indexed? (boolean)(opt)
     Default: true
    :tooltip (metamorphic-content)(opt)}]
 :field-content-f (function)(opt)
  Default: return
 :field-value-f (function)(opt)
  Default: return
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  Default: :normal
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :initial-value (string)(opt)
 :label (metamorphic-content)(opt)
 :line-height (keyword or px)(opt)
  :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :text-block
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl
 :max-length (integer)(opt)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :modifier (function)(opt)
 :on-blur (metamorphic-event)(opt)
 :on-changed (metamorphic-event)(opt)
  It happens BEFORE the application state gets updated with the actual value!
  If you have to get the ACTUAL value from the application state, use the
  :on-type-ended event instead!
  It happens BEFORE the application state gets updated with the actual value!
  This event takes the field content as its last parameter
 :on-empty (metamorphic-event)(opt)
  This event takes the field content as its last parameter
 :on-enter (metamorphic-event)(opt)
  This event takes the field content as its last parameter
 :on-focus (metamorphic-event)(opt)
 :on-mount (metamorphic-event)(opt)
  This event takes the field content as its last parameter
 :on-type-ended (metamorphic-event)(opt)
  This event takes the field content as its last parameter
 :on-unmount (metamorphic-event)(opt)
  This event takes the field content as its last parameter
 :outdent (map)(opt)
  Same as the :indent property.
 :placeholder (metamorphic-content)(opt)
 :start-adornments (maps in vector)(opt)
  Same as the :end-adornments property.
 :stretch-orientation (keyword)(opt)
  :horizontal, :none
  Default: :none
 :style (map)(opt)
 :surface (metamorphic-content)(opt)
 :validator (map)(opt)
  {:f (function)
   :invalid-message (metamorphic-content)(opt)
   :invalid-message-f (function)(opt)
   :prevalidate? (boolean)(opt)}
 :value-path (vector)(opt)}
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
[text-field {:validator {:f #(not (empty? %))
                         :invalid-message "Invalid value"}}]
```

```
@usage
(defn get-invalid-message [value] "Invalid value")
[text-field {:validator {:f #(not (empty? %))
                         :invalid-message-f get-invalid-message}}]
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
[text-field {:modifier #(string/starts-with! % "/")}]
```

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
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :m
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s
 :helper (metamorphic-content)(opt)
 :icon (keyword)(opt)
  Default: :icon
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :on-click (metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)
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

---

### toggle

```
@param (keyword)(opt) toggle-id
@param (map) toggle-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :hover-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :marker-color (keyword)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl
 :on-click (metamorphic-event)
 :on-right-click (metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :style (map)(opt)}
```

```
@usage
[toggle {...}]
```

```
@usage
[toggle :my-toggle {...}]
```

---

### vertical-polarity

```
@param (keyword)(opt) polarity-id
@param (map) polarity-props
{:class (keyword or keywords in vector)(opt)
 :end-content (metamorphic-content)
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
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

---

### vertical-separator

```
@param (keyword)(opt) separator-id
@param (map) separator-props
{:width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s}
```

```
@usage
[vertical-separator {...}]
```

```
@usage
[vertical-separator :my-vertical-separator {...}]
```