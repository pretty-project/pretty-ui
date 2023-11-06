
### components.api

Functional documentation of the components.api ClojureScript namespace

---

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > components.api

### Index

- [color-picker](#color-picker)

- [compact-list-header](#compact-list-header)

- [content-swapper-button](#content-swapper-button)

- [content-swapper-header](#content-swapper-header)

- [copyright-label](#copyright-label)

- [data-element](#data-element)

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

- [popup-menu-header](#popup-menu-header)

- [popup-menu-title](#popup-menu-title)

- [popup-progress-indicator](#popup-progress-indicator)

- [section-description](#section-description)

- [section-title](#section-title)

- [side-menu](#side-menu)

- [side-menu-footer](#side-menu-footer)

- [side-menu-header](#side-menu-header)

- [side-menu-label](#side-menu-label)

- [user-avatar](#user-avatar)

- [vector-item-controls](#vector-item-controls)

- [vector-item-list](#vector-item-list)

- [vector-items-header](#vector-items-header)

---

### color-picker

```
@param (keyword) picker-id
@param (map) picker-props
{:click-effect (keyword)(opt)
  :opacity
  Default: :opacity
 :color-stamp (map)(opt)
  {:border-radius (map)(opt)
   :gap (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
   :height (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
    Default: :l
   :width (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
    Default: :l}
 :hover-effect (keyword)(opt)
  :opacity
 :placeholder (metamorphic-content)(opt)
  Default: :choose-color!}
```

```
@usage
[color-picker {...}]
```

```
@usage
[color-picker :my-color-picker {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([picker-props]
   [component (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (fn [_ picker-props]       (let [picker-props (color-picker.prototypes/picker-props-prototype picker-id picker-props)]
            [color-picker picker-id picker-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [color-picker]]))

(components.api/color-picker ...)
(color-picker                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props]       (let [header-props (compact-list-header.prototypes/header-props-prototype header-props)]
            [compact-list-header header-id header-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [compact-list-header]]))

(components.api/compact-list-header ...)
(compact-list-header                ...)
```

</details>

---

### content-swapper-button

```
@param (keyword)(opt) button-id
@param (map) button-props
{:font-size (keyword)(opt)
  Default: :xs
 :gap (keyword)(opt)
  Default: :auto
 :horizontal-align (keyword)(opt)
  Default: :left
 :hover-color (keyword or string)(opt)
  Default: :highlight
 :icon (keyword)(opt)
  Default: :chevron_right
 :icon-position (keyword)(opt)
  Default: :right
 :icon-size (keyword)(opt)
  Default: :m
 :indent (map)(opt)
  Default: {:all :xs}
 :width (keyword)(opt)
  Default: :auto}
```

```
@usage
[content-swapper-button {...}]
```

```
@usage
[content-swapper-button :my-content-swapper-button {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (fn [_ button-props]       (let [button-props (content-swapper-button.prototypes/button-props-prototype button-props)]
            [pretty-elements/button button-id button-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [content-swapper-button]]))

(components.api/content-swapper-button ...)
(content-swapper-button                ...)
```

</details>

---

### content-swapper-header

```
@param (keyword)(opt) header-id
@param (map) header-props
{:border-color (keyword or string)(opt)
  Default: :highlight
 :border-position (keyword)(opt)
  Default: :bottom
 :font-size (keyword)(opt)
  Default: :xs
 :gap (keyword)(opt)
  Default: :auto
 :horizontal-align (keyword)(opt)
  Default: :left
 :hover-color (keyword or string)(opt)
  Default: :highlight
 :icon (keyword)(opt)
  Default: :chevron_left
 :icon-position (keyword)(opt)
  Default: :left
 :icon-size (keyword)(opt)
  Default: :xl
 :indent (map)(opt)
  Default: {:horizontal :xs}
 :width (keyword)(opt)
  Default: :auto}
```

```
@usage
[content-swapper-header {...}]
```

```
@usage
[content-swapper-header :my-content-swapper-header {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props]       (let [header-props (content-swapper-header.prototypes/header-props-prototype header-props)]
            [pretty-elements/button header-id header-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [content-swapper-header]]))

(components.api/content-swapper-header ...)
(content-swapper-header                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (fn [_ label-props]       (let [label-props (copyright-label.prototypes/label-props-prototype label-props)]
            [copyright-label label-id label-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [copyright-label]]))

(components.api/copyright-label ...)
(copyright-label                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([element-props]
   [component (random/generate-keyword) element-props])

  ([element-id element-props]
   (fn [_ element-props]       (let [element-props (data-element.prototypes/element-props-prototype element-props)]
            [data-element element-id element-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [data-element]]))

(components.api/data-element ...)
(data-element                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([content-props]
   [component (random/generate-keyword) content-props])

  ([content-id content-props]
   (fn [_ content-props]       (let []            [error-content content-id content-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [error-content]]))

(components.api/error-content ...)
(error-content                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (fn [_ label-props]       (let [label-props (error-label.prototypes/label-props-prototype label-props)]
            [pretty-elements/label label-id label-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [error-label]]))

(components.api/error-label ...)
(error-label                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([view-props]
   [component (random/generate-keyword) view-props])

  ([view-id view-props]
   (fn [_ view-props]       (let []            [ghost-view view-id view-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [ghost-view]]))

(components.api/ghost-view ...)
(ghost-view                ...)
```

</details>

---

### illustration

```
@param (keyword)(opt) illustration-id
@param (map) illustration-props
{:class (keyword or keywords in vector)(opt)
 :height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxl
 :uri (string)
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

<details>
<summary>Source code</summary>

```
(defn component
  ([illustration-props]
   [component (random/generate-keyword) illustration-props])

  ([illustration-id illustration-props]
   (fn [_ illustration-props]       (let [illustration-props (illustration.prototypes/illustration-props-prototype illustration-props)]
            [illustration illustration-id illustration-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [illustration]]))

(components.api/illustration ...)
(illustration                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([block-props]
   [component (random/generate-keyword) block-props])

  ([block-id block-props]
   (fn [_ block-props]       (let []            [input-block block-id block-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [input-block]]))

(components.api/input-block ...)
(input-block                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([table-props]
   [component (random/generate-keyword) table-props])

  ([table-id table-props]
   (fn [_ table-props]       (let [table-props (input-table.prototypes/table-props-prototype table-props)]
            [input-table table-id table-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [input-table]]))

(components.api/input-table ...)
(input-table                ...)
```

</details>

---

### item-list-header

```
@param (keyword)(opt) header-id
@param (map) header-props
{:border (keyword)(opt)
  :bottom, :top}
 :cells (maps in vector)
  [{:label (metamorphic-content)(opt)
    :on-click (Re-Frame metamorphic-event)(opt)
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

<details>
<summary>Source code</summary>

```
(defn component
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props]       (let [header-props (item-list-header.prototypes/header-props-prototype header-props)]
            [item-list-header header-id header-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [item-list-header]]))

(components.api/item-list-header ...)
(item-list-header                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([row-props]
   [component (random/generate-keyword) row-props])

  ([row-id row-props]
   (fn [_ row-props]       (let [row-props (item-list-row.prototypes/row-props-prototype row-props)]
            [item-list-row row-id row-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [item-list-row]]))

(components.api/item-list-row ...)
(item-list-row                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([avatar-props]
   [component (random/generate-keyword) avatar-props])

  ([avatar-id avatar-props]
   (fn [_ avatar-props]       (let []            [list-item-avatar avatar-id avatar-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [list-item-avatar]]))

(components.api/list-item-avatar ...)
(list-item-avatar                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (fn [_ button-props]       (let [button-props (list-item-button.prototypes/button-props-prototype button-props)]
            [list-item-button button-id button-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [list-item-button]]))

(components.api/list-item-button ...)
(list-item-button                ...)
```

</details>

---

### list-item-cell

```
@param (keyword)(opt) cell-id
@param (map) cell-props
{:on-click (Re-Frame metamorphic-event)(opt)
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

<details>
<summary>Source code</summary>

```
(defn component
  ([cell-props]
   [component (random/generate-keyword) cell-props])

  ([cell-id cell-props]
   (fn [_ cell-props]       (let []            [list-item-cell cell-id cell-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [list-item-cell]]))

(components.api/list-item-cell ...)
(list-item-cell                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([handle-props]
   [component (random/generate-keyword) handle-props])

  ([handle-id handle-props]
   (fn [_ handle-props]       (let []            [list-item-drag-handle handle-id handle-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [list-item-drag-handle]]))

(components.api/list-item-drag-handle ...)
(list-item-drag-handle                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([gap-props]
   [component (random/generate-keyword) gap-props])

  ([gap-id gap-props]
   (fn [_ gap-props]       (let []            [list-item-gap gap-id gap-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [list-item-gap]]))

(components.api/list-item-gap ...)
(list-item-gap                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([icon-props]
   [component (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   (fn [_ icon-props]       [list-item-icon icon-id icon-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [list-item-icon]]))

(components.api/list-item-icon ...)
(list-item-icon                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([thumbnail-props]
   [component (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   (fn [_ thumbnail-props]       (let []            [list-item-thumbnail thumbnail-id thumbnail-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [list-item-thumbnail]]))

(components.api/list-item-thumbnail ...)
(list-item-thumbnail                ...)
```

</details>

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
[menu-table {:rows [[{:content "Row #1"   :font-weight :semi-bold}
                     {:content "Value #1" :color :muted}
                     {:content "Value #2" :color :muted}]
                    [{...} {...}]]}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([table-props]
   [component (random/generate-keyword) table-props])

  ([table-id table-props]
   (fn [_ table-props]       (let [table-props (menu-table.prototypes/table-props-prototype table-props)]
            [menu-table table-id table-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [menu-table]]))

(components.api/menu-table ...)
(menu-table                ...)
```

</details>

---

### notification-bubble

```
@param (keyword) bubble-id
@param (map) bubble-props
{:border-color (keyword)(opt)
  Default: :secondary
 :border-radius (map)(opt)
  Default: {:all :m}
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
  Default: {:border-radius {:all :s}
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

<details>
<summary>Source code</summary>

```
(defn component
  ([bubble-props]
   [component (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   (fn [_ bubble-props]       (let [bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-id bubble-props)]
            [pretty-elements/notification-bubble bubble-id bubble-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [notification-bubble]]))

(components.api/notification-bubble ...)
(notification-bubble                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([preview-props]
   [component (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (fn [_ preview-props]       (let []            [pdf-preview preview-id preview-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [pdf-preview]]))

(components.api/pdf-preview ...)
(pdf-preview                ...)
```

</details>

---

### popup-label-bar

```
@param (keyword)(opt) bar-id
@param (map) bar-props
{:label (map)(opt)
  {:content (metamorphic-content)}
 :primary-button (map)(opt)
  {:label (metamorphic-content)
   :on-click (Re-Frame metamorphic-event)}
 :secondary-button (map)(opt)
  {:label (metamorphic-content)
   :on-click (Re-Frame metamorphic-event)}}
```

```
@usage
[popup-label-bar {...}]
```

```
@usage
[popup-label-bar :my-popup-label-bar {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([bar-props]
   [component (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   (fn [_ bar-props]       (let [bar-props (popup-label-bar.prototypes/bar-props-prototype bar-props)]
            [popup-label-bar bar-id bar-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [popup-label-bar]]))

(components.api/popup-label-bar ...)
(popup-label-bar                ...)
```

</details>

---

### popup-menu-header

```
@param (keyword) header-id
@param (map) header-props
{:close-button (map)(opt)
  Default: {:border-radius {:all :s}
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

<details>
<summary>Source code</summary>

```
(defn component
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props]       (let [header-props (popup-menu-header.prototypes/header-props-prototype header-props)]
            [popup-menu-header header-id header-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [popup-menu-header]]))

(components.api/popup-menu-header ...)
(popup-menu-header                ...)
```

</details>

---

### popup-menu-title

```
@param (keyword)(opt) title-id
@param (map) title-props
{:border-color (keyword)
  Default: :default
 :border-position (keyword)
  Default: :bottom
 :border-width (keyword)
  Default: :xs
 :font-weight (keyword)
  Default: :semi-bold
 :gap (keyword)
  Default: :auto
 :icon-position (keyword)
  Default: :right
 :icon-size (keyword)
  Default: :xl
 :indent (map)
  Default: {:bottom :xxs}
 :outdent (map)
  Default: {:bottom :s}
 :text-transform (keyword)(opt)
  Default: :uppercase
 :width (keyword)(opt)
  Default: :auto}
```

```
@usage
[popup-menu-title {...}]
```

```
@usage
[popup-menu-title :my-popup-menu-title {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([title-props]
   [component (random/generate-keyword) title-props])

  ([title-id title-props]
   (fn [_ title-props]       (let [title-props (popup-menu-title.prototypes/title-props-prototype title-props)]
            [pretty-elements/label title-id title-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [popup-menu-title]]))

(components.api/popup-menu-title ...)
(popup-menu-title                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([indicator-props]
   [component (random/generate-keyword) indicator-props])

  ([indicator-id indicator-props]
   (fn [_ indicator-props]       (let []            [popup-progress-indicator indicator-id indicator-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [popup-progress-indicator]]))

(components.api/popup-progress-indicator ...)
(popup-progress-indicator                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([description-props]
   [component (random/generate-keyword) description-props])

  ([description-id description-props]
   (fn [_ description-props]       (let [description-props (section-description.prototypes/description-props-prototype description-props)]
            [pretty-elements/label description-id description-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [section-description]]))

(components.api/section-description ...)
(section-description                ...)
```

</details>

---

### section-title

```
@param (keyword)(opt) title-id
@param (map) title-props
{:font-size (keyword)(opt)
  Default: :5xl
 :font-weight (keyword)(opt)
  Default: :semi-bold}
```

```
@usage
[section-title {...}]
```

```
@usage
[section-title :my-section-title {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([title-props]
   [component (random/generate-keyword) title-props])

  ([title-id title-props]
   (fn [_ title-props]       (let [title-props (section-title.prototypes/title-props-prototype title-props)]
            [section-title title-id title-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [section-title]]))

(components.api/section-title ...)
(section-title                ...)
```

</details>

---

### side-menu

```
@param (keyword)(opt) menu-id
@param (map) menu-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :all, :bottom, :top, :left, :right, :horizontal, :vertical
 :border-radius (map)(opt)
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

<details>
<summary>Source code</summary>

```
(defn component
  ([menu-props]
   [component (random/generate-keyword) menu-props])

  ([menu-id {:keys [threshold] :as menu-props}]
   (fn [_ menu-props]       (let [menu-props (side-menu.prototypes/menu-props-prototype menu-props)]
            (if (or (not                                 threshold)
                    (window-observer/viewport-width-min? threshold))
                [side-menu menu-id menu-props])))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [side-menu]]))

(components.api/side-menu ...)
(side-menu                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([footer-props]
   [component (random/generate-keyword) footer-props])

  ([footer-id footer-props]
   (fn [_ footer-props]       (let []            [side-menu-footer footer-id footer-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [side-menu-footer]]))

(components.api/side-menu-footer ...)
(side-menu-footer                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props]       (let []            [side-menu-header header-id header-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [side-menu-header]]))

(components.api/side-menu-header ...)
(side-menu-header                ...)
```

</details>

---

### side-menu-label

```
@param (keyword)(opt) label-id
@param (map) label-props
{:font-size (keyword)(opt)
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

<details>
<summary>Source code</summary>

```
(defn component
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (fn [_ label-props]       (let [label-props (side-menu-label.prototypes/label-props-prototype label-props)]
            [pretty-elements/label label-id label-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [side-menu-label]]))

(components.api/side-menu-label ...)
(side-menu-label                ...)
```

</details>

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

<details>
<summary>Source code</summary>

```
(defn component
  ([avatar-props]
   [component (random/generate-keyword) avatar-props])

  ([avatar-id avatar-props]
   (fn [_ avatar-props]       (let [avatar-props (user-avatar.prototypes/avatar-props-prototype avatar-props)]
            [user-avatar avatar-id avatar-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [user-avatar]]))

(components.api/user-avatar ...)
(user-avatar                ...)
```

</details>

---

### vector-item-controls

```
@param (keyword)(opt) controls-id
@param (map) controls-props
{:disabled? (boolean)(opt)
 :item-dex (integer)
 :on-change (Re-Frame metamorphic-event)(opt)
 :tooltip-position (keyword)(opt)
  :left, :right
  Default: :right
 :value-path (Re-Frame path vector)}
```

```
@usage
[vector-item-controls {...}]
```

```
@usage
[vector-item-controls :my-vector-item-controls {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([controls-props]
   [component (random/generate-keyword) controls-props])

  ([controls-id controls-props]
   (fn [_ controls-props]       (let [controls-props (vector-item-controls.prototypes/controls-props-prototype controls-props)]
            [vector-item-controls controls-id controls-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [vector-item-controls]]))

(components.api/vector-item-controls ...)
(vector-item-controls                ...)
```

</details>

---

### vector-item-list

```
@param (keyword)(opt) list-id
@param (map) list-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :indent (map)(opt)
 :item-element (Reagent component symbol)
 :outdent (map)(opt)
 :placeholder (map)(opt)
  {:illustration (string)(opt)
   :label (metamorphic-content)(opt)}
 :style (map)(opt)
 :value-path (Re-Frame path vector)}
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

<details>
<summary>Source code</summary>

```
(defn component
  ([list-props]
   [component (random/generate-keyword) list-props])

  ([list-id list-props]
   (fn [_ list-props]       (let []            [vector-item-list list-id list-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [vector-item-list]]))

(components.api/vector-item-list ...)
(vector-item-list                ...)
```

</details>

---

### vector-items-header

```
@param (keyword)(opt) header-id
@param (map) header-props
{:class (keyword or keywords in vector)(opt)
 :disabled? (boolean)(opt)
 :horizontal-align (keyword)(opt)
  :center, :left, :right
  Default: :center
 :indent (map)(opt)
 :initial-item (*)(opt)
  Default: {}
 :label (metamorphic-content)
 :on-change (Re-Frame metamorphic-event)(opt)
 :outdent (map)(opt)
 :style (map)(opt)
 :value-path (Re-Frame path vector)}
```

```
@usage
[vector-items-header {...}]
```

```
@usage
[vector-items-header :my-vector-items-header {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props]       (let [header-props (vector-items-header.prototypes/header-props-prototype header-props)]
            [vector-items-header header-id header-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [components.api :refer [vector-items-header]]))

(components.api/vector-items-header ...)
(vector-items-header                ...)
```

</details>

---

<sub>This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.</sub>

