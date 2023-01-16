
# components.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > components.api

### Index

- [action-bar](#action-bar)

- [color-picker](#color-picker)

- [compact-list-header](#compact-list-header)

- [copyright-label](#copyright-label)

- [data-element](#data-element)

- [data-table](#data-table)

- [error-content](#error-content)

- [error-label](#error-label)

- [ghost-view](#ghost-view)

- [illustration](#illustration)

- [input-block](#input-block)

- [input-table](#input-table)

- [item-list-header](#item-list-header)

- [item-list-row](#item-list-row)

- [list-item-avatar](#list-item-avatar)

- [list-item-button](#list-item-button)

- [list-item-cell](#list-item-cell)

- [list-item-drag-handle](#list-item-drag-handle)

- [list-item-gap](#list-item-gap)

- [list-item-icon](#list-item-icon)

- [list-item-thumbnail](#list-item-thumbnail)

- [menu-table](#menu-table)

- [notification-bubble](#notification-bubble)

- [pdf-preview](#pdf-preview)

- [popup-label-bar](#popup-label-bar)

- [popup-menu-button](#popup-menu-button)

- [popup-menu-header](#popup-menu-header)

- [popup-menu-label](#popup-menu-label)

- [popup-progress-indicator](#popup-progress-indicator)

- [section-description](#section-description)

- [section-title](#section-title)

- [side-menu](#side-menu)

- [side-menu-button](#side-menu-button)

- [side-menu-footer](#side-menu-footer)

- [side-menu-header](#side-menu-header)

- [side-menu-label](#side-menu-label)

- [sidebar-button](#sidebar-button)

- [surface-box](#surface-box)

- [user-avatar](#user-avatar)

- [vector-item-controls](#vector-item-controls)

- [vector-item-list](#vector-item-list)

- [vector-items-header](#vector-items-header)

### action-bar

```
@param (keyword)(opt) bar-id
@param (map) bar-props
{:disabled? (boolean)(opt)}
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
{:class (keyword or keywords in vector)(opt)
 :color-stamp (map)(opt)
  {:border-radius (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
   :gap (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
   :height (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
    Default: :l
   :width (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
    Default: :l}
 :disabled? (boolean)(opt)
 :indent (map)(opt)
 :label (metamorphic-content)(opt)
 :on-select (metamorphic-event)(opt)
 :outdent (map)(opt)
 :placeholder (metamorphic-content)(opt)
  Default: :choose-color!
 :style (map)(opt)
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

### compact-list-header

```
@param (keyword)(opt) header-id
@param (map) header-props
{:class (keyword or keywords in vector)(opt)
 :hide-button (map)
 :indent (map)(opt)
 :label (metamorphic-content)
 :order-button (map)
 :outdent (map)(opt)
 :search-field (map)
 :style (map)(opt)}
```

```
@usage
[compact-list-header {...}]
```

```
@usage
[compact-list-header :my-compact-list-header {...}]
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
 :disabled? (boolean)(opt)
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
[data-table {:rows [[{:content "Row #1"   :font-weight :bold}
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
{:color (keyword or string)(opt)
  Default: :warning
 :font-size (keyword)(opt)
  Default: :xs}
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

### illustration

```
@param (keyword)(opt) illustration-id
@param (map) illustration-props
{:class (keyword or keywords in vector)(opt)
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxl
 :illustration (keyword)
 :style (map)(opt)
 :width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxl}
```

```
@usage
[illustration {...}]
```

```
@usage
[illustration :my-illustration {...}]
```

---

### input-block

```
@description
When displaying a 'text-field' element in an 'input-block' by using the
same ID for both the field and the block, the label of the block can targets the field.
```

```
@param (keyword)(opt) block-id
@param (map) block-props
{:input (metamorphic-content)
 :label (metamorphic-content)}
```

```
@usage
[input-block {...}]
```

```
@usage
[input-block :my-input-block {...}]
```

---

### input-table

```
@param (keyword)(opt) table-id
@param (map) table-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :indent (map)(opt)
 :input-params (vector)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
 :rows (vectors in vector)
  [[(string) row-template
    (list of metamorphic-contents or vectors) row-blocks
     [(metamorphic-content) input-label
      (keyword) input-id
      (metamorphic-content) input]]]
 :style (map)(opt)}
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

```
@usage
(defn my-name-field [my-param] [text-field ::my-name-field  {...}])
[input-table {:input-params ["My param"]
              :rows [["160px 1fr" [:name ::my-name-field #'my-name-field]]]}]
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
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :cells (metamorphic-contents in vector)
 :disabled? (boolean)(opt)
 :drag-attributes (map)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-color (keyword)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :marker-position (keyword)(opt)
  :tl, :tr, :br, :bl
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
```

```
@usage
[list-item-avatar {...}]
```

```
@usage
[list-item-avatar :my-list-item-avatar {...}]
```

---

### list-item-button

```
@param (keyword)(opt) button-id
@param (map) button-props
{:fill-color (keyword)(opt)
  Default: :highlight
 :font-size (keyword)(opt)
  Default: :xs
 :hover-color (keyword)(opt)
  Default: :highlight}
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

### list-item-icon

```
@param (keyword)(opt) icon-id
@param (map) icon-props
```

```
@usage
[list-item-icon {...}]
```

```
@usage
[list-item-icon :my-list-item-icon {...}]
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
[menu-table {:rows [[{:content "Row #1"   :font-weight :bold}
                     {:content "Value #1" :color :muted}
                     {:content "Value #2" :color :muted}]
                    [{...} {...}]]}]
```

---

### notification-bubble

```
@param (keyword) bubble-id
@param (map) bubble-props
{:border-color (keyword)(opt)
  Default: :secondary
 :border-radius (keyword)(opt)
  Default: :m
 :border-width (keyword)(opt)
  Default: :xs
 :fill-color (keyword or string)(opt)
  Default: :default
 :indent (map)(opt)
  Default: {:horizontal :xs :vertical :xs}
 :min-width (keyword)(opt)
  Default: :m
 :outdent (map)(opt)
  Default: {:bottom :xs :vertical :xs}
 :primary-button (map)(opt)
  Default: {:border-radius :s
            :icon          :close
            :hover-color   :highlight
            :layout        :icon-button
            :on-click      [:x.ui/remove-bubble! :my-bubble]}}
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

### popup-menu-button

```
@param (keyword)(opt) button-id
@param (map) button-props
{:border-radius (keyword)(opt)
  Default: :s
 :gap (keyword)(opt)
  Default: :xs
 :horizontal-align (keyword)(opt)
  Default: :left
 :hover-color (keyword or string)(opt)
  Default: :highlight
 :icon-size (keyword)(opt)
  Default: :m
 :indent (map)(opt)
  Default: {:horizontal :xxs :vertical :xxs}
 :outdent (map)(opt)
  Default: {:vertical :xs}}
```

```
@usage
[popup-menu-button {...}]
```

```
@usage
[popup-menu-button :my-popup-menu-button {...}]
```

---

### popup-menu-header

```
@param (keyword) header-id
@param (map) header-props
{:close-button (map)(opt)
  Default: {:border-radius :s
            :hover-color   :highlight
            :icon          :close
            :keypress      {:key-code 27}}
 :label (map)(opt)
  Default: {:color     :muted
            :font-size :xs
            :indent    {:horizontal :xs :vertical :s}}}
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

### popup-menu-label

```
@param (keyword)(opt) label-id
@param (map) label-props
{:color (keyword or string)(opt)
  Default: :muted
 :gap (keyword)(opt)
  Default: :xs
 :horizontal-align (keyword)(opt)
  Default: :left
 :icon-size (keyword)(opt)
  Default: :m
 :outdent (map)(opt)
  Default: {:horizontal :xxs :vertical :s}}
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

### popup-progress-indicator

```
@param (keyword)(opt) indicator-id
@param (map) indicator-props
{:class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :disabled? (boolean)(opt)
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

### section-description

```
@param (keyword)(opt) description-id
@param (map) description-props
{:color (keyword or string)(opt)
  Default: :muted
 :font-size (keyword)(opt)
  Default: :xxs
 :horizontal-align (keyword)(opt)
  Default: :center
 :indent (map)(opt)
  Default: {:horizontal :xs :vertical :xs}}
```

```
@usage
[section-description {...}]
```

```
@usage
[section-description :my-section-description {...}]
```

---

### section-title

```
@param (keyword)(opt) title-id
@param (map) title-props
{:font-size (keyword)(opt)
  Default: :5xl
 :font-weight (keyword)(opt)
  Default: :bold}
```

```
@usage
[section-title {...}]
```

```
@usage
[section-title :my-section-title {...}]
```

---

### side-menu

```
@param (keyword)(opt) menu-id
@param (map) menu-props
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
{:font-size (keyword)(opt)
  Default: :xs
 :gap (keyword)(opt)
  Default: :xs
 :horizontal-align (keyword)(opt)
  Default: :left
 :hover-color (keyword or string)(opt)
  Default: :highlight
 :icon-size (keyword)(opt)
  Default: :m
 :indent (map)(opt)
  Default: {:horizontal :xs :left :s :right :xl}
 :preset (keyword)(opt)}
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
{:color (keyword or string)(opt)
  Default: :muted
 :font-size (keyword)(opt)
  Default: :xs
 :gap (keyword)(opt)
  Default: :xs
 :horizontal-align (keyword)(opt)
  Default: :left
 :icon-size (keyword)(opt)
  Default: :m
 :indent (map)(opt)
  Default: {:horizontal :xs :vertical :s}
 :style (map)(opt)
  Default: {:max-width "var(--element-size-m)"}}
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
{:color (keyword or string)(opt)
  Default: :invert
 :font-size (keyword)(opt)
  Default: :xs
 :font-weight (keyword)(opt)
  Default: :medium
 :hover-color (keyword or string)(opt)
  Default: :invert
 :icon-size (keyword)(opt)
  Default: :m
 :horizontal-align (keyword)(opt)
  Default: :left
 :hover-color (keyword)(opt)
  Default: invert}
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

### user-avatar

```
@param (keyword)(opt) avatar-id
@param (map) avatar-props
{:class (keyword or keywords in vector)(opt)
 :colors (strings in vector)(opt)
 :disabled? (boolean)(opt)
 :icon (keyword)(opt)
  Default: :person
 :icon-family (keyword)(opt)
  :material-symbols-filled, :material-symbols-outlined
  Default: :material-symbols-outlined
 :indent (map)(opt)
 :initials (string)(opt)
  Displays initial letters instead of displaying an icon
 :outdent (map)(opt)
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

---

### vector-item-controls

```
@param (keyword)(opt) controls-id
@param (map) controls-props
{:disabled? (boolean)(opt)
 :item-dex (integer)
 :tooltip-position (keyword)(opt)
  :left, :right
  Default: :right
 :value-path (vector)}
```

```
@usage
[vector-item-controls {...}]
```

```
@usage
[vector-item-controls :my-vector-item-controls {...}]
```

---

### vector-item-list

```
@param (keyword)(opt) list-id
@param (map) list-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :indent (map)(opt)
 :item-element (symbol)
 :outdent (map)(opt)
 :placeholder (metamorphic-content)(opt)
 :style (map)(opt)
 :value-path (vector)}
```

```
@usage
[vector-item-list {...}]
```

```
@usage
[vector-item-list :my-vector-item-list {...}]
```

```
@usage
(defn my-item-element [item-dex] ...)
[vector-item-list :my-vector-item-list {:item-element #'my-item-element
                                        :value-path   [:my-items]}]
```

---

### vector-items-header

```
@param (keyword)(opt) header-id
@param (map) header-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :left
 :indent (map)(opt)
 :initial-item (*)(opt)
  Default: {}
 :label (metamorphic-content)
 :outdent (map)(opt)
 :style (map)(opt)
 :value-path (vector)}
```

```
@usage
[vector-items-header {...}]
```

```
@usage
[vector-items-header :my-vector-items-header {...}]
```