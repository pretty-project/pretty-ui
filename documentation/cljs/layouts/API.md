
# layouts.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > layouts.api

### Index

- [box-popup](#box-popup)

- [plain-popup](#plain-popup)

- [plain-surface](#plain-surface)

- [sidebar](#sidebar)

- [struct-popup](#struct-popup)

### box-popup

```
@param (keyword)(opt) popup-id
@param (map) popup-props
{:border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :content (metamorphic-content)
 :cover-color (keyword or string)(opt)
 :fill-color (keyword or string)(opt)
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
 :lock-scroll? (boolean)(opt)
 :max-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :max-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-cover (Re-Frame metamorphic-event)(opt)
 :on-mount (Re-Frame metamorphic-event)(opt)
 :on-unmount (Re-Frame metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :stretch-orientation (keyword)(opt)
  :both, :horizontal, :vertical
 :style (map)(opt)}
```

```
@usage
[box-popup {...}]
```

```
@usage
[box-popup :my-box-popup {...}]
```

<details>
<summary>Source code</summary>

```
(defn layout
  ([popup-props]
   [layout (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   (let [popup-props (box-popup.prototypes/popup-props-prototype popup-props)]
        [box-popup popup-id popup-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [layouts.api :refer [box-popup]]))

(layouts.api/box-popup ...)
(box-popup             ...)
```

</details>

---

### plain-popup

```
@param (keyword)(opt) popup-id
@param (map) popup-props
{:content (metamorphic-content)(opt)
 :cover-color (keyword or string)(opt)
 :lock-scroll? (boolean)(opt)
 :on-cover (Re-Frame metamorphic-event)(opt)
 :on-mount (Re-Frame metamorphic-event)(opt)
 :on-unmount (Re-Frame metamorphic-event)(opt)
 :style (map)(opt)}
```

```
@usage
[plain-popup {...}]
```

```
@usage
[plain-popup :my-plain-popup {...}]
```

<details>
<summary>Source code</summary>

```
(defn layout
  ([popup-props]
   [layout (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   (let [popup-props (plain-popup.prototypes/popup-props-prototype popup-props)]
        [plain-popup popup-id popup-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [layouts.api :refer [plain-popup]]))

(layouts.api/plain-popup ...)
(plain-popup             ...)
```

</details>

---

### plain-surface

```
@param (keyword)(opt) surface-id
@param (map) surface-props
{:content (metamorphic-content)
 :content-orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :vertical
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :on-mount (Re-Frame metamorphic-event)(opt)
 :on-unmount (Re-Frame metamorphic-event)(opt)
 :style (map)(opt)}
```

```
@usage
[plain-surface {...}]
```

```
@usage
[plain-surface :my-plain-surface {...}]
```

<details>
<summary>Source code</summary>

```
(defn layout
  ([surface-props]
   [layout (random/generate-keyword) surface-props])

  ([surface-id surface-props]
   (let [layout-props (plain-surface.prototypes/surface-props-prototype surface-props)]
        [plain-surface surface-id surface-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [layouts.api :refer [plain-surface]]))

(layouts.api/plain-surface ...)
(plain-surface             ...)
```

</details>

---

### sidebar

```
@param (keyword)(opt) sidebar-id
@param (map) sidebar-props
{:border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :content (metamorphic-content)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-mount (Re-Frame metamorphic-event)(opt)
 :on-unmount (Re-Frame metamorphic-event)(opt)
 :position (keyword)(opt)
  :left, :right
  Default: :left
 :style (map)(opt)
 :threshold (px)(opt)
  Default: 720}
```

```
@usage
[sidebar {...}]
```

```
@usage
[sidebar :my-sidebar {...}]
```

<details>
<summary>Source code</summary>

```
(defn layout
  ([sidebar-props]
   [layout (random/generate-keyword) sidebar-props])

  ([sidebar-id sidebar-props]
   (let [sidebar-props (sidebar.prototypes/sidebar-props-prototype sidebar-props)]
        [sidebar sidebar-id sidebar-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [layouts.api :refer [sidebar]]))

(layouts.api/sidebar ...)
(sidebar             ...)
```

</details>

---

### struct-popup

```
@param (keyword)(opt) popup-id
@param (map) popup-props
{:body (metamorphic-content)
 :border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :cover-color (keyword or string)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :footer (metamorphic-content)(opt)
 :header (metamorphic-content)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :lock-scroll? (boolean)(opt)
 :max-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :max-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-cover (Re-Frame metamorphic-event)(opt)
 :on-mount (Re-Frame metamorphic-event)(opt)
 :on-unmount (Re-Frame metamorphic-event)(opt)
 :outdent (map)(opt)
  Same as the :indent property.
 :stretch-orientation (keyword)(opt)
  :both, :horizontal, :vertical
 :style (map)(opt)}
```

```
@usage
[struct-popup {...}]
```

```
@usage
[struct-popup :my-struct-popup {...}]
```

<details>
<summary>Source code</summary>

```
(defn layout
  ([popup-props]
   [layout (random/generate-keyword) popup-props])

  ([popup-id popup-props]
   (let [popup-props (struct-popup.prototypes/popup-props-prototype popup-props)]
        [struct-popup popup-id popup-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [layouts.api :refer [struct-popup]]))

(layouts.api/struct-popup ...)
(struct-popup             ...)
```

</details>

---

This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.

