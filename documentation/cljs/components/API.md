
# components.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > components.api

### Index

- [action-bar](#action-bar)

- [color-picker](#color-picker)

- [copyright-label](#copyright-label)

- [data-element](#data-element)

- [data-table](#data-table)

- [error-content](#error-content)

- [error-label](#error-label)

- [ghost-view](#ghost-view)

- [input-table](#input-table)

- [item-list-header](#item-list-header)

- [item-list-row](#item-list-row)

- [list-item-avatar](#list-item-avatar)

- [list-item-button](#list-item-button)

- [list-item-cell](#list-item-cell)

- [list-item-drag-handle](#list-item-drag-handle)

- [list-item-gap](#list-item-gap)

- [list-item-thumbnail](#list-item-thumbnail)

- [menu-table](#menu-table)

- [pdf-preview](#pdf-preview)

- [popup-close-bar](#popup-close-bar)

- [popup-label-bar](#popup-label-bar)

- [popup-menu-header](#popup-menu-header)

- [popup-progress-indicator](#popup-progress-indicator)

- [side-menu](#side-menu)

- [side-menu-button](#side-menu-button)

- [side-menu-footer](#side-menu-footer)

- [side-menu-header](#side-menu-header)

- [side-menu-label](#side-menu-label)

- [sidebar-button](#sidebar-button)

- [surface-box](#surface-box)

- [surface-description](#surface-description)

- [surface-title](#surface-title)

- [user-avatar](#user-avatar)

### action-bar

```
@param (keyword)(opt) bar-id
@param (map) bar-props
{:disabled? (boolean)(opt)}
  Default: false
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :s
 :indent (map)(opt)
 :label (metamorphic-content)
 :outdent (map)(opt)
 :placeholder (metamorphic-content)(opt)
 :value (metamorphic-content)(opt)}
```

```
@usage
[action-bar {...}]
```

```
@usage
[action-bar :my-action-bar {...}]
```

---

### color-picker

```
@param (keyword) picker-id
@param (map) picker-props
{:disabled? (boolean)(opt)
 :indent (map)(opt)
 :label (metamorphic-content)(opt)
 :on-select (metamorphic-event)(opt)
 :outdent (map)(opt)
 :value-path (vector)}
```

```
@usage
[color-picker {...}]
```

```
@usage
[color-picker :my-color-picker {...}]
```

---

### copyright-label

```
@param (keyword)(opt) label-id
@param (map) label-props
{:font-size (keyword)(opt)
  Default: :xxs}
```

```
@usage
[copyright-label {...}]
```

```
@usage
[copyright-label :my-copyright-label {...}]
```

```
@usage
[copyright-label {...}]
```

---

### data-element

```
@param (keyword)(opt) element-id
@param (map) element-props
{:copyable? (boolean)(opt)
  Default: false
 :disabled? (boolean)(opt)
  Default: false
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :s
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
 :info-text (metamorphic-content)(opt)
 :marked? (boolean)(opt)
  Default: false
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
 :placeholder (metamorphic-content)(opt)
 :value (metamorphic-content or metamorphic-contents in vector)(opt)}
```

```
@usage
[data-element {...}]
```

```
@usage
[data-element :my-data-element {...}]
```

---

### data-table

```
@param (keyword)(opt) table-id
@param (map) table-props
{:class (keyword or keywords in vector)(opt)
 :columns (label-props maps in vectors in vector)(opt)
 :disabled? (boolean)(opt)}
  Default: false
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :s
 :indent (map)(opt)
 :label (metamorphic-content)
 :outdent (map)(opt)
 :placeholder (metamorphic-content)(opt)
 :rows (label-props maps in vectors in vector)(opt)
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
[data-table {:rows [[{:content "Row #1"   :font-weight :extra-bold}
                     {:content "Value #1" :color :muted}
                     {:content "Value #2" :color :muted}]
                    [{...} {...}]]}]
```

---

### error-content

```
@param (keyword)(opt) content-id
@param (map) content-props
{:content (metamorphic-content)}
```

```
@usage
[error-content {...}]
```

```
@usage
[error-content :my-error-content {...}]
```

---

### error-label

```
@param (keyword)(opt) label-id
@param (map) label-props
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :indent (map)(opt)
 :outdent (map)(opt)
 :style (map)(opt)}
```

```
@usage
[error-label {...}]
```

```
@usage
[error-label :my-error-label {...}]
```

---

### ghost-view

```
@param (keyword)(opt) view-id
@param (map) view-props
{:box-count (integer)(opt)
  TODO
  W/ {:layout :box-surface}
 :breadcrumb-count (integer)(opt)
  W/ {:layout :box-surface, :box-surface-header}
 :indent (map)(opt)
 :item-count (integer)(opt)
  W/ {:layout :item-list :item-count 3
 :layout (keyword)
  :box-surface, :box-surface-body, :box-surface-header, :item-list
 :outdent (map)(opt)}
```

```
@usage
[ghost-view {...}]
```

```
@usage
[ghost-view :my-ghost-view {...}]
```

---

### input-table

```
@param (keyword)(opt) table-id
@param (map) table-props
{:border (keyword)(opt)
  :both, :bottom, :top
 :rows (vectors in vectors in vector)
  [[(string) template
    [(metamorphic-content) label
     (keyword) input-id
     (metamorphic-content) input]]]}
```

```
@usage
[input-table {...}]
```

```
@usage
[input-table :my-input-table {...}]
```

```
@usage
(defn my-name-field  [] [text-field ::my-name-field  {...}])
(defn my-color-field [] [text-field ::my-color-field {...}])
(defn my-age-field   [] [text-field ::my-age-field   {...}])
[input-table {:rows [["160px 1fr 160px 1fr" [:name  ::my-name-field  #'my-name-field]
                                            [:color ::my-color-field #'my-color-field]]
                     ["160px 1fr"           [:age   ::my-age-field   #'my-age-field]]]}]
```

---

### item-list-header

```
@param (keyword)(opt) header-id
@param (map) header-props
{:border (keyword)(opt)
  :bottom, :top}
 :cells (maps in vector)
  [{:label (metamorphic-content)(opt)
    :on-click (metamorphic-event)(opt)
    :width (px)(opt)}]
 :template (string)}
```

```
@usage
[item-list-header {...}]
```

```
@usage
[item-list-header :my-item-list-header {...}]
```

```
@usage
[item-list-header :my-item-list-header {:cells [[:div ]]}]
```

---

### item-list-row

```
@param (keyword)(opt) row-id
@param (map) row-props
{:border (keyword)(opt)
  :bottom, :top}
 :cells (metamorphic-contents in vector)
 :disabled? (boolean)(opt)
  Default: false
 :drag-attributes (map)(opt)
 :highlighted? (boolean)(opt)
  Default: false
 :marker-color (keyword)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl
   Default: :tr
   W/ {:marker-color ...}
 :template (string)}
```

```
@usage
[item-list-row {...}]
```

```
@usage
[item-list-row :my-item-list-row {...}]
```

```
@usage
[item-list-row :my-item-list-row {:cells [[:div ]]}]
```

---

### list-item-avatar

```
@param (keyword)(opt) avatar-id
@param (map) avatar-props
{:colors (strings in vector)(opt)
 :first-name (string)(opt)
 :last-name (string)(opt)
 :size (px)(opt)
  Default: 60}
```

```
@usage
[list-item-avatar {...}]
```

```
@usage
[list-item-avatar :my-avatar {...}]
```

---

### list-item-button

```
@param (keyword)(opt) button-id
@param (map) button-props
{:fill-color (keyword)(opt)
  Default: :highlight
 :hover-color (keyword)(opt)
  Default: :highlight
 :icon (keyword)(opt)
 :icon-family (keyword)(opt)
  Default: :material-icons-filled
  W/ {:icon ...}
 :icon-position (keyword)(opt)
  :left, :right
  Default: :left
  W/ {:icon ...}
 :label (metamorphic-content)
 :on-click (metamorphic-event)}
```

```
@usage
[list-item-button {...}]
```

```
@usage
[list-item-button :my-button {...}]
```

---

### list-item-cell

```
@param (keyword)(opt) cell-id
@param (map) cell-props
{:on-click (metamorphic-event)(opt)
 :rows (maps in vector)
  [{:color (keyword or string)(opt)
     Default: :default
    :content (metamorphic-content)
    :font-size (keyword)(opt)
      Default: :s
    :font-weight (keyword)(opt)
     :bold, extra-bold, :normal
     Default: :bold
    :placeholder (metamorphic-content)(opt)}]}
```

```
@usage
[list-item-cell {...}]
```

```
@usage
[list-item-cell :my-cell {...}]
```

```
@usage
[list-item-cell :my-cell {:rows [{:content "Row #1"}]}]
```

---

### list-item-drag-handle

```
@param (keyword)(opt) handle-id
@param (map)(opt) handle-props
{:drag-attributes (map)}
```

```
@usage
[list-item-drag-handle {...}]
```

```
@usage
[list-item-drag-handle :my-handle {...}]
```

---

### list-item-gap

```
@param (keyword)(opt) gap-id
@param (map) gap-props
{}
```

```
@usage
[list-item-gap {...}]
```

```
@usage
[list-item-gap :my-gap {...}]
```

---

### list-item-thumbnail

```
@param (keyword)(opt) thumbnail-id
@param (map) thumbnail-props
{:icon (keyword)(opt)
 :icon-family (keyword)(opt)
 :thumbnail (string)(opt)}
```

```
@usage
[list-item-thumbnail {...}]
```

```
@usage
[list-item-thumbnail :my-thumbnail {...}]
```

```
@usage
[list-item-thumbnail {:thumbnail "/my-thumbnail.png"}]
```

```
@usage
[list-item-thumbnail {:icon :people}]
```

---

### menu-table

```
@param (keyword)(opt) table-id
@param (map) table-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)}
  Default: false
 :indent (map)(opt)
 :items (maps in vector)(opt)
 [{:label (metamorphic-content)(opt)
   :link (string)(opt)
   :target (keyword)
    :blank, :self}]
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
 :placeholder (metamorphic-content)(opt)
  Default: :no-items-to-show
 :style (map)(opt)}
```

```
@usage
[menu-table {...}]
```

```
@usage
[menu-table :my-menu-table {...}]
```

```
@usage
[menu-table {:rows [[{:content "Row #1"   :font-weight :extra-bold}
                     {:content "Value #1" :color :muted}
                     {:content "Value #2" :color :muted}]
                    [{...} {...}]]}]
```

---

### pdf-preview

```
@param (keyword)(opt) preview-id
@param (map) preview-props
{:height (string)
 :src (string)
 :width (string)}
```

```
@usage
[pdf-preview {...}]
```

```
@usage
[pdf-preview :my-pdf-preview {...}]
```

---

### popup-close-bar

```
@param (keyword)(opt) bar-id
@param (map) bar-props
{:on-close (metamorphic-event)}
```

```
@usage
[popup-close-bar {...}]
```

```
@usage
[popup-close-bar :my-popup-close-bar {...}]
```

---

### popup-label-bar

```
@param (keyword)(opt) bar-id
@param (map) bar-props
{:label (metamorphic-content)(opt)
 :primary-button (map)(opt)
  {:label (metamorphic-content)
   :on-click (metamorphic-even)}
 :secondary-button (map)(opt)
  {:label (metamorphic-content)
   :on-click (metamorphic-even)}}
```

```
@usage
[popup-label-bar {...}]
```

```
@usage
[popup-label-bar :my-popup-label-bar {...}]
```

---

### popup-menu-header

```
@param (keyword) header-id
@param (map) header-props
{:label (metamorphic-content)(opt)
 :on-close (metamorphic-event)
 :placeholder (metamorphic-content)(opt)}
```

```
@usage
[popup-menu-header {...}]
```

```
@usage
[popup-menu-header :my-popup-menu-header {...}]
```

---

### popup-progress-indicator

```
@param (keyword)(opt) indicator-id
@param (map) indicator-props
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
  Default: false
 :fill-color (string)(opt)
  Default: "var( --fill-color-default )"
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
 :overflow (keyword)(opt)
  :hidden, :visible
  Default: :visible
 :query (vector)(opt)
 :refresh-interval (ms)(opt)
  W/ {:query ...}
 :style (map)(opt)}
```

```
@usage
[popup-progress-indicator {...}]
```

```
@usage
[popup-progress-indicator :my-popup-progress-indicator {...}]
```

---

### side-menu

```
@param (keyword)(opt) menu-id
@param (map) menu-props
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
  Default: false
 :indent (map)(opt)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :outdent (map)(opt)
 :position (keyword)(opt)
  :left, :right
  Default: :left
 :style (map)(opt)
 :threshold (px)(opt)}
```

```
@usage
[side-menu {...}]
```

```
@usage
[side-menu :my-side-menu {...}]
```

---

### side-menu-button

```
@param (keyword)(opt) button-id
@param (map) button-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :icon (keyword)
 :icon-color (string or keyword)
 :icon-family (keyword)(opt)
  Default: :material-icons-filled
 :label (metamorphic-content)
 :on-click (metamorphic-event)
 :preset (keyword)(opt)
 :style (map)(opt)}
```

```
@usage
[side-menu-button {...}]
```

```
@usage
[side-menu-button :my-side-menu-button {...}]
```

---

### side-menu-footer

```
@param (keyword)(opt) footer-id
@param (map) footer-props
{}
```

```
@usage
[side-menu-footer {...}]
```

```
@usage
[side-menu-footer :my-side-menu-footer {...}]
```

---

### side-menu-header

```
@param (keyword)(opt) header-id
@param (map) header-props
{}
```

```
@usage
[side-menu-header {...}]
```

```
@usage
[side-menu-header :my-side-menu-header {...}]
```

---

### side-menu-label

```
@param (keyword)(opt) label-id
@param (map) label-props
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
  Default: false
 :icon (keyword)
 :icon-family (keyword)(opt)
  Default: :material-icons-filled
 :style (map)(opt)}
```

```
@usage
[side-menu-label {...}]
```

```
@usage
[side-menu-label :my-side-menu-label {...}]
```

---

### sidebar-button

```
@param (keyword)(opt) button-id
@param (map) button-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :hover-color (keyword or string)(opt)
  Default: :invert
 :icon (keyword)
 :icon-color (string or keyword)
 :icon-family (keyword)(opt)
  Default: :material-icons-filled
 :label (metamorphic-content)
 :on-click (metamorphic-event)
 :style (map)(opt)}
```

```
@usage
[sidebar-button {...}]
```

```
@usage
[sidebar-button :my-sidebar-button {...}]
```

---

### surface-box

```
@param (keyword)(opt) box-id
@param (map) box-props
{:fill-color (string)(opt)
  Default: "var( --fill-color-default )"
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
  Default: false
 :helper (metamorphic-content)(opt)
 :indent (map)(opt)
 :info-text (metamorphic-content)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
 :overflow (keyword)(opt)
  :hidden, :visible
  Default: :visible
 :query (vector)(opt)
 :refresh-interval (ms)(opt)
  W/ {:query ...}
 :style (map)(opt)}
```

```
@usage
[surface-box {...}]
```

```
@usage
[surface-box :my-surface-box {...}]
```

---

### surface-description

```
@param (keyword)(opt) description-id
@param (map) description-props
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
 :horizontal-align (keyword)(opt)
  :left, :center, :right
  Default: :center
 :indent (map)(opt)
  Default: {:horizontal :xs :vertical :xs}
 :outdent (map)(opt)
 :style (map)(opt)}
```

```
@usage
[surface-description {...}]
```

```
@usage
[surface-description :my-surface-description {...}]
```

---

### surface-title

```
@param (keyword)(opt) title-id
@param (map) title-props
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :indent (map)(opt)
 :outdent (map)(opt)
 :placeholder (metamorphic-content)(opt)
 :style (map)(opt)}
```

```
@usage
[surface-title {...}]
```

```
@usage
[surface-title :my-surface-title {...}]
```

---

### user-avatar

```
@param (keyword)(opt) avatar-id
@param (map) avatar-props
{:class (keyword or keywords in vector)(opt)
 :colors (strings in vector)(opt)
 :disabled? (boolean)(opt)
  Default: false
 :first-name (string)(opt)
 :indent (map)(opt)
 :outdent (map)(opt)
 :last-name (string)(opt)
 :size (px)(opt)
  Default: 60
 :style (map)(opt)
 :width (px)(opt)}
```

```
@usage
[user-avatar {...}]
```

```
@usage
[user-avatar :my-user-avatar {...}]
```