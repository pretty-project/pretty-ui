
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

- [color-selector](#color-selector)

- [color-stamp](#color-stamp)

- [column](#column)

- [combo-box](#combo-box)

- [counter](#counter)

- [date-field](#date-field)

- [digit-field](#digit-field)

- [expandable](#expandable)

- [file-drop-area](#file-drop-area)

- [ghost](#ghost)

- [horizontal-line](#horizontal-line)

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

- [password-field](#password-field)

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

- [vertical-line](#vertical-line)

- [vertical-polarity](#vertical-polarity)

- [vertical-separator](#vertical-separator)

### anchor

```
@param (keyword)(opt) anchor-id
@param (map) anchor-props
{:color (keyword or string)(opt)
  :default, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
  Default: false
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  Default: :s
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
  :block, :normal
  Default: :normal
 :on-click (metamorphic-event)(opt)
 :stop-propagation? (boolean)(opt)
  Default: false
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

---

### blank

```
@param (keyword)(opt) blank-id
@param (map) blank-props
{:content (metamorphic-content)(opt)
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}:style (map)(opt)
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
  Default: false
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
{:badge-color (keyword or string)(opt)
  :primary, :secondary, :success, :warning
 :badge-content (metamorphic-content)(opt)
 :background-color (keyword or string)(opt)
  :highlight, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :border-color (keyword or string)(opt)
  :highlight, :invert, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :border-radius (keyword)(opt)
  :none, :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :s
 :class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :disabled? (boolean)(opt)
  Default: false
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :bold, :extra-bold, :inherit
  Default: :bold
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :hover-color (keyword or string)(opt)
  :highlight, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :icon (keyword)(opt)
 :icon-family (keyword)(opt)
  :material-icons-filled, :material-icons-outlined
  Default: :material-icons-filled
 :icon-position (keyword)(opt)
  :left, :right
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
 :keypress (map)(opt)
  {:key-code (integer)
   :required? (boolean)(opt)
    Default: false}
 :label (metamorphic-content)(opt)
 :on-click (metamorphic handler)(opt)
 :on-mouse-over (metamorphic handler)(opt)
 :preset (keyword)(opt)
 :stop-propagation? (boolean)(opt)
  Default: false
 :style (map)(opt)}
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

### button-separator

```
@param (keyword)(opt) separator-id
@param (map) separator-props
{:class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :highlight, :muted
  Default: :muted
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :style (map)(opt)}
```

```
@usage
[button-separator {...}]
```

```
@usage
[button-separator :my-separator {...}]
```

---

### card

```
@param (keyword)(opt) card-id
@param (map) card-props
XXX#3240
{:background-color (keyword or string)(opt)
  :highlight, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :badge-color (keyword or string)(opt)
  :primary, :secondary, :success, :warning
 :badge-content (metamorphic-content)(opt)
 :border-color (keyword or string)(opt)
  :highlight, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :border-radius (keyword)(opt)
  :none, :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :s
 :content (metamorphic-content)(opt)
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :hover-color (keyword or string)(opt)
  :highlight, :invert, :muted, :none, :primary, :secondary, :success, :warning
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
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  Default: :none
 :on-click (metamorphic-event)(opt)
 :stretch-orientation (keyword)(opt)
  :horizontal, :vertical, :both, :none
  Default: :vertical
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
  :default, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :class (keyword or keywords in vector)(opt)
 :default-value (boolean)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :font-size (keyword)(opt)
  :xs, :s, :inherit
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
 :marked? (boolean)(opt)
  Default: false
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
 :required? (boolean or keyword)(opt)
  true, false, :unmarked
  Default: false
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
{:background-color (keyword or string)(opt)
  :highlight, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :icon (keyword)(opt)
 :icon-family (keyword)(opt)
  :material-icons-filled, :material-icons-outlined
  Default: :material-icons-filled
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
  TODO A chip elem egésze kattintható
 :primary-button-icon (keyword)(opt)
  Default: :close
 :primary-button-event (metamorphic-event)(opt)
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
 :sections (maps in vector)}
  [{:color (keyword or string)
     :default, :highlight, :muted, :primary, :secondary, :success, :warning
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

### color-marker

```
@param (keyword)(opt) marker-id
@param (map) marker-props
{:class (keyword or keywords in vector)(opt)
 :colors (keywords or strings in vector)(opt)
  Default: [:highlight]
 :disabled? (boolean)(opt)
  Default: false
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :size (keyword)(opt)
  :m, :l, :xl, :xxl
  Default: :s
 :style (map)(opt)}
```

```
@usage
[color-marker {...}]
```

```
@usage
[color-marker :my-color-marker {...}]
```

---

### color-selector

```
@param (keyword)(opt) selector-id
@param (map) selector-props
{:class (keyword or keywords in vector)(opt)
 :options (strings in vector)(opt)
 :options-label (metamorphic-content)(opt)
 :options-path (vector)(opt)
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

### color-stamp

```
@param (keyword)(opt) stamp-id
@param (map) stamp-props
{:class (keyword or keywords in vector)(opt)
 :colors (keywords or strings in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :helper (metamorphic-content)(opt)
 :info-text (metamorphic-content)(opt)
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
 :size (keyword)(opt)
  :m, :l, :xl, :xxl
  Default: :s
 :style (map)(opt)}
```

```
@usage
[color-stamp {...}]
```

```
@usage
[color-stamp :my-color-stamp {...}]
```

```
@usage
[color-stamp :my-color-stamp {:colors ["red" "green" "blue"]}]
```

---

### column

```
@param (keyword)(opt) column-id
@param (map) column-props
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :gap (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
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
  :default, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
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
 :initial-value (integer)(opt)
  Default: 0
 :label (metamorphic-content)(opt)
 :marked? (boolean)(opt)
  Default: false
 :max-value (integer)(opt)
 :min-value (integer)(opt)
 :resetable? (boolean)(opt)
  Default: false
 :required? (boolean or keyword)(opt)
  true, false, :unmarked
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

### expandable

```
@param (keyword)(opt) expandable-id
@param (map) expandable-props
{:content (metamorphic-content)
 :disabled? (boolean)(opt)
  Default: false
 :expanded? (boolean)(opt)
  Default: true
 :icon (keyword)(opt)
 :icon-family (keyword)(opt)
  :material-icons-filled, :material-icons-outlined
  Default: :material-icons-filled
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :label (metamorphic-content)(opt)}
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

### file-drop-area

```
@param (keyword)(opt) area-id
@param (map) area-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
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
  Default: :drop-files-here-to-upload
 :on-click (metamorphic-event)(opt)
 :style (map)(opt)}
```

```
@usage
[file-drop-area {...}]
```

```
@usage
[file-drop-area :my-file-drop-area {...}]
```

---

### ghost

```
@param (keyword)(opt) ghost-id
@param (map) ghost-props
{:border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :s
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

### horizontal-line

```
@param (keyword)(opt) line-id
@param (map) line-props
{:class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :highlight, :muted, :primary, :secondary
  Default: :muted
 :strength (px)(opt)
  Default: 1
 :style (map)(opt)}
```

```
@usage
[horizontal-line {...}]
```

```
@usage
[horizontal-line :my-horizontal-line {...}]
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
 :style (map)(opt)
 :start-content (metamorphic-content)(opt)
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
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :icon (keyword)
 :icon-family (keyword)(opt)
  :material-icons-filled, :material-icons-outlined
  Default: :material-icons-filled
 :indent (map)(opt)
  {:bottom (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :left (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :right (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl
   :top (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl}
 :layout (keyword)(opt)
  :touch-target Az ikont tartalmazó elem méretei megegyeznek az icon-button típus méreteivel
 :size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :m
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
{:badge-color (keyword or string)(opt)
  :primary, :secondary, :success, :warning
 :badge-content (metamorphic-content)(opt)
 :background-color (keyword or string)(opt)
  :highlight, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :border-color (keyword or string)(opt)
  :highlight, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :border-radius (keyword)(opt)
  :none, :xxs, :xs, :s, :m, :l, :xl
  Default: :xs
 :class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :disabled? (boolean)(opt)
  Default: false
 :height (keyword)(opt)
  :m, :l, :xl, :xxl, :3xl
  Default: :xxl
 :hover-color (keyword or string)(opt)
  :highlight, :invert, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :icon (keyword)
 :icon-family (keyword)(opt)
  :material-icons-filled, :material-icons-outlined
  Default: :material-icons-filled
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
 :on-click (metamorphic handler)(opt)
 :on-mouse-over (metamorphic handler)(opt)
 :preset (keyword)(opt)
 :progress (percent)(opt)
 :progress-duration (ms)(opt)
  W/ {:progress ...}
 :stop-propagation? (boolean)(opt)
  Default: false
 :style (map)(opt)
 :tooltip (metamorphic-content)(opt)
 :variant (keyword)(opt)
  :placeholder
 :width (keyword)(opt)
  :m, :l, :xl, :xxl, :3xl
  Default: :xxl}
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
  Default: :default
 :content (metamorphic-content)
 :copyable? (boolean)(opt)
  Default: false
 :disabled? (boolean)(opt)
  Default: false
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :bold, extra-bold, :inherit, :normal
  Default :bold
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
 :horizontal-position (keyword)(opt)
  :center, :left, :none, :right
  Default: :none
 :icon (keyword)(opt)
 :icon-family (keyword)(opt)
  :material-icons-filled, :material-icons-outlined
  Default: :material-icons-filled
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
  :block, :normal
  Default: :normal
 :marked? (boolean)(opt)
  Default: false
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  Default: :none
 :overflow-direction (keyword)(opt)
  :normal, :reversed
  Default :normal
 :placeholder (metamorphic-content)(opt)
 :required? (boolean)(opt)
  Default: false
 :selectable? (boolean)(opt)
  Default: false
 :style (map)(opt)
 :target-id (keyword)(opt)
 :vertical-position (keyword)(opt)
  :bottom, :center, :none, :top
  Default: :none}
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
 :sections (maps in vector)}
  [{:color (keyword or string)(opt)
     :default, :highlight, :muted, :primary, :secondary, :success, :warning
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
  (XXX#5406 overflow-x: scroll & {:horizontal-align :space-between} nem lehetséges)
  W/ {:orientation :horizontal}
 :font-size (keyword)(opt)
  :xs, :s
  Default: :s
 :height (keyword)(opt)
  :m, :l, :xl, :xxl
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
     :primary, :secondary, :success, :warning
    :badge-content (metamorphic-content)(opt)
    :disabled? (boolean)(opt)
     Default: false
    :href (string)(opt)
     XXX#7004
     A {:href "..."} tulajdonság használata esetén a menu elemek [:a] elemként
     renderelődnek és az {:on-click ...} valamint az {:on-mouse-over ...}
     tulajdonságok figyelmen kívűl hagyódnak!
    :icon (keyword)(opt)
    :icon-family (keyword)(opt)
     :material-icons-filled, :material-icons-outlined
     Default: :material-icons-filled
    :label (metamorphic-content)(opt)
    :on-click (metamorphic-event)(opt)}]
 :orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :horizontal
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
{:chip-label-f (function)(opt)
  Default: return
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

### point-diagram

```
@param (keyword)(opt) diagram-id
@param (map) diagram-props
{:color (keyword or string)(opt)
  :default, :muted, :primary, :secondary
  Default: :default
  W/ {:label ...}
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
  :default, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :class (keyword or keywords in vector)(opt)
 :default-value (*)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :font-size (keyword)(opt)
  :xs, :s, :inherit
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
 :marked? (boolean)(opt)
  Default: false
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
 :required? (boolean or keyword)(opt)
  true, false, :unmarked
  Default: false
 :style (map)(opt)
 :unselectable? (boolean)(opt)
  Default: false
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
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)(opt)
 :gap (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
 :horizontal-align (keyword)(opt)
  :center, :left, :right, :space-around, :space-between, :space-evenly
  Default: :left
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
  Default: false
 :border-radius (keyword)(opt)
  :none, :xxs, :xs, :s, :m, :l
  Default: :s
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :extendable? (boolean)(opt)
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
 :initial-options (vector)(opt)
 :initial-value (*)(opt)
 :label (metamorphic-content)(opt)
 :layout (keyword)(opt)
  :button, :icon-button, :select
  Default: :select
 :marked? (boolean)(opt)
  Default: false
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  Default: :none
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
 :required? (boolean or keyword)(opt)
  true, false, :unmarked
  Default: false
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
 :initial-value (vector)(opt)
  Default: [0 100]
 :label (metamorphic-content)(opt)
 :marked? (boolean)(opt)
  Default: false
 :max-value (integer)(opt)
  Default: 100
 :min-value (integer)(opt)
  Default: 0
 :resetable? (boolean)(opt)
  Default: false
 :required? (boolean or keyword)(opt)
  true, false, :unmarked
  Default: false
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
  :default, :muted, :primary, :secondary, :success, :warning
  Default: :primary
 :class (keyword or keywords in vector)(opt)
 :default-value (boolean)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :font-size (keyword)(opt)
  :xs, :s, :inherit
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
 :marked? (boolean)(opt)
  Default: false
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
 :required? (boolean or keyword)(opt)
  true, false, :unmarked
  Default: false
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
  :default, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :content (metamorphic-content)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  Default: :s
 :font-weight (keyword)(opt)
  :bold, :extra-bold, :inherit, :normal
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
  :block, :normal
  Default: :normal
 :max-lines (integer)(opt)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  Default: :none
 :placeholder (metamorphic-content)(opt)
 :selectable? (boolean)(opt)
  Default: true
 :style (map)(opt)}
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
  Default: false
 :autofill-name (keyword)(opt)
 :autofocus? (boolean)(opt)
 :border-color (keyword or string)(opt)
  :default, :primary, :secondary, :success, :warning
  Default: :default
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :none
  Default: :s
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :emptiable? (boolean)(opt)
  Default: false
 :end-adornments (maps in vector)(opt)
  [{:color (keyword)(opt)
     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
     Default: :default
    :disabled? (boolean)(opt)
     Default: false
    :icon (keyword)
    :icon-family (keyword)(opt)
     :material-icons-filled, :material-icons-outlined
     Default: :material-icons-filled
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
  :xs, :s
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
 :initial-value (string)(opt)
 :label (metamorphic-content)(opt)
 :marked? (boolean)(opt)
  Default: false
 :max-length (integer)(opt)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  Default: :none
 :modifier (function)(opt)
 :on-blur (metamorphic-event)(opt)
 :on-change (metamorphic-event)(opt)
  Az esemény utolsó paraméterként megkapja a mező aktuális értékét.
 :on-empty (metamorphic-event)(opt)
 :on-focus (metamorphic-event)(opt)
 :on-mount (metamorphic-event)(opt)
  Az esemény utolsó paraméterként megkapja a mező aktuális értékét.
 :on-type-ended (metamorphic-event)(opt)
  Az esemény utolsó paraméterként megkapja a mező aktuális értékét.
 :on-unmount (metamorphic-event)(opt)
  Az esemény utolsó paraméterként megkapja a mező aktuális értékét.
 :placeholder (metamorphic-content)(opt)
 :required? (boolean or keyword)(opt)
  true, false, :unmarked
  Default: false
 :start-adornments (maps in vector)(opt)
  [{:color (keyword)(opt)
     :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
     Default: :default
    :disabled? (boolean)(opt)
     Default: false
    :icon (keyword)(opt)
    :icon-family (keyword)(opt)
     :material-icons-filled, :material-icons-outlined
     Default: :material-icons-filled
    :label (string)(opt)
    :on-click (metamorphic-event)
    :tab-indexed? (boolean)(opt)
     Default: true
    :tooltip (metamorphic-content)}]
 :stretch-orientation (keyword)(opt)
  :horizontal, :none
  Default: :none
 :style (map)(opt)
 :surface (metamorphic-content)(opt)
 :unemptiable? (boolean)(opt)
  Default: false
  TODO
  A field on-blur esemény pillanatában, ha üres a value-path, akkor
  az eltárolt backup-value értéket beállítja a value-path -re.
 :validator (map)(opt)
  {:f (function)
   :invalid-message (metamorphic-content)(opt)
   :invalid-message-f (function)(opt)
   :prevalidate? (boolean)(opt)
    A mező kitöltése közben validálja annak értékét, még mielőtt a mező
    {:visited? true} állapotba lépne.
    Default: false}
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
[text-field {:validator {:f               #(not (empty? %))
                         :invalid-message "Invalid value"}}]
```

```
@usage
(defn get-invalid-message [value] "Invalid value")
[text-field {:validator {:f                 #(not (empty? %))
                         :invalid-message-f get-invalid-message}}]
```

```
@usage
(defn my-surface [field-id])
[text-field {:surface {:content #'my-surface}}]
```

```
@usage
(defn my-surface [field-id surface-props])
[text-field {:surface #'my-surface}]
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
 :border-radius (keyword)(opt)
  :none, :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :none
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :s
 :helper (metamorphic-content)(opt)
 :icon (keyword)(opt)
  Default: :icon
 :icon-family (keyword)(opt)
  Default: :material-icons-filled
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
 :required? (boolean)(opt)
  Default: false
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
{:background-color (keyword)(opt)
  :highlight, :muted, :none, :primary, :secondary, :success, :warning
  Default: :none
 :border-radius (keyword)(opt)
  :none, :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :none
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
  Default: false
 :hover-color (keyword or string)(opt)
  :highlight, :invert, :muted, :none, :primary, :secondary, :success, :warning
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
 :on-click (metamorphic-event)
 :on-right-click (metamorphic-event)(opt)
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

### vertical-line

```
@param (keyword)(opt) line-id
@param (map) line-props
{:class (keyword or keywords in vector)(opt)
 :color (keyword or string)(opt)
  :highlight, :muted, :primary, :secondary
  Default: :muted
 :strength (px)(opt)
  Default: 1
 :style (map)(opt)}
```

```
@usage
[vertical-line {...}]
```

```
@usage
[vertical-line :my-vertical-line {...}]
```

---

### vertical-polarity

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

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine

