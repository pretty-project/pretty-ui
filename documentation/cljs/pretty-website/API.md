
### pretty-website.api

Functional documentation of the pretty-website.api ClojureScript namespace

---

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > pretty-website.api

### Index

- [contacts](#contacts)

- [follow-us-links](#follow-us-links)

- [language-selector](#language-selector)

- [multi-menu](#multi-menu)

- [scroll-icon](#scroll-icon)

- [scroll-sensor](#scroll-sensor)

- [sidebar](#sidebar)

---

### contacts

```
@param (keyword)(opt) contacts-id
@param (map) contacts-props
{:contact-groups (maps in vector)(opt)
  [{:addresses (strings in vector)(opt)
    :email-addresses (strings in vector)(opt)
    :info (metamorphic-content)(opt)
    :label (metamorphic-content)(opt)
    :phone-numbers (numbers or strings in vector)(opt)}]
 :class (keyword or keywords in vector)(opt)
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
 :preset (keyword)(opt)
 :style (map)(opt)}
```

```
@usage
[contacts {...}]
```

```
@usage
[contacts :my-contacts {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([contacts-props]
   [component (random/generate-keyword) contacts-props])

  ([contacts-id contacts-props]
   (fn [_ contacts-props]       (let [contacts-props (pretty-presets/apply-preset contacts-props)]
            [contacts contacts-id contacts-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-website.api :refer [contacts]]))

(pretty-website.api/contacts ...)
(contacts                    ...)
```

</details>

---

### follow-us-links

```
@warning
To use this component you have to add the Font Awesome icon set to your project!
```

```
@description
This component uses Font Awesome brand icons for social media provider links.
It converts the given provider name to an icon class:
:instagram => .fab.fa-instagram
```

```
@param (keyword)(opt) links-id
@param (map) links-props
{:class (keyword or keywords in vector)(opt)
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :links (vectors in vector)
  [[(keyword) provider
    (string) link]
   [...]]
 :outdent (map)(opt)
  Same as the :indent property.
 :preset (keyword)(opt)
 :style (map)(opt)}
```

```
@usage
[follow-us-links {...}]
```

```
@usage
[follow-us-links :my-follow-us-links {...}]
```

```
@usage
[follow-us-links {:links [[:facebook "facebook.com/my-profile"]
                          [:instagram "instagram.com/my-profile"]]}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([links-props]
   [component (random/generate-keyword) links-props])

  ([links-id links-props]
   (fn [_ links-props]       (let [links-props (pretty-presets/apply-preset links-props)]
            [follow-us-links links-id links-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-website.api :refer [follow-us-links]]))

(pretty-website.api/follow-us-links ...)
(follow-us-links                    ...)
```

</details>

---

### language-selector

```
@param (keyword)(opt) selector-id
@param (map) selector-props
{:class (keyword or keywords in vector)(opt)
 :font-size (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  Default: :s
 :gap (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :languages (keywords in vector)
 :outdent (map)(opt)
  Same as the :indent property.
 :preset (keyword)(opt)
 :style (map)(opt)}
```

```
@usage
[language-selector {...}]
```

```
@usage
[language-selector :my-language-selector {...}]
```

```
@usage
[language-selector {:languages [:en :hu]}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([selector-props]
   [component (random/generate-keyword) selector-props])

  ([selector-id selector-props]
   (fn [_ selector-props]       (let [selector-props (pretty-presets/apply-preset                           selector-props)
             selector-props (language-selector.prototypes/selector-props-prototype selector-props)]
            [language-selector selector-id selector-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-website.api :refer [language-selector]]))

(pretty-website.api/language-selector ...)
(language-selector                    ...)
```

</details>

---

### multi-menu

```
@description
This component implements the 'dropdown-menu' element and in case of the viewport
width is smaller than the given threshold, it displays the menu items
on a sidebar menu and replaces the menu bar with a single menu button.
```

```
@param (keyword)(opt) menu-id
@param (map) menu-props
{:class (keyword or keywords in vector)(opt)
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
 :preset (keyword)(opt)
 :style (map)(opt)
 :threshold (px)(opt)
  Default: 0}
```

```
@usage
[multi-menu {...}]
```

```
@usage
[multi-menu :my-multi-menu {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([menu-props]
   [component (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   (fn [_ menu-props]       (let [menu-props (pretty-presets/apply-preset                menu-props)
             menu-props (multi-menu.prototypes/menu-props-prototype menu-props)]
            [multi-menu menu-id menu-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-website.api :refer [multi-menu]]))

(pretty-website.api/multi-menu ...)
(multi-menu                    ...)
```

</details>

---

### scroll-icon

```
@param (keyword)(opt) icon-id
@param (map) icon-props
{:class (keyword or keywords in vector)(opt)
 :color (string)(opt)
  Default: "#FFFFFF"
 :outdent (map)(opt)
  Same as the :indent property.
 :preset (keyword)(opt)
 :style (map)(opt)}
```

```
@usage
[scroll-icon {...}]
```

```
@usage
[scroll-icon :my-scroll-icon {...}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([icon-props]
   [component (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   (fn [_ icon-props]       (let [icon-props (pretty-presets/apply-preset                 icon-props)
             icon-props (scroll-icon.prototypes/icon-props-prototype icon-props)]
            [scroll-icon icon-id icon-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-website.api :refer [scroll-icon]]))

(pretty-website.api/scroll-icon ...)
(scroll-icon                    ...)
```

</details>

---

### scroll-sensor

```
@param (keyword)(opt) sensor-id
@param (map) sensor-props
{:callback-f (function)
 :preset (keyword)(opt)
 :style (map)(opt)}
```

```
@usage
[scroll-sensor {...}]
```

```
@usage
[scroll-sensor :my-sensor {...}]
```

```
@usage
(defn my-scroll-f [intersecting?] ...)
[scroll-sensor {:callback-f my-scroll-f}]
```

<details>
<summary>Source code</summary>

```
(defn component
  ([sensor-props]
   [component (random/generate-keyword) sensor-props])

  ([sensor-id sensor-props]
   (fn [_ sensor-props]       (let [sensor-props (pretty-presets/apply-preset sensor-props)]
            [scroll-sensor sensor-id sensor-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-website.api :refer [scroll-sensor]]))

(pretty-website.api/scroll-sensor ...)
(scroll-sensor                    ...)
```

</details>

---

### sidebar

```
@param (keyword)(opt) sidebar-id
@param (map) sidebar-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :left, :right
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :cover-color (keyword or string)(opt)
  Default: :black
 :fill-color (keyword or string)(opt)
  Default: :white
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
 :position (keyword)(opt)
  :left, :right
  Default: :left
 :preset (keyword)(opt)
 :style (map)(opt)}
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
(defn component
  ([sidebar-props]
   [component (random/generate-keyword) sidebar-props])

  ([sidebar-id sidebar-props]
   (fn [_ sidebar-props]       (let [sidebar-props (pretty-presets/apply-preset                sidebar-props)
             sidebar-props (sidebar.prototypes/sidebar-props-prototype sidebar-props)]
            [sidebar sidebar-id sidebar-props]))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-website.api :refer [sidebar]]))

(pretty-website.api/sidebar ...)
(sidebar                    ...)
```

</details>

---

<sub>This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.</sub>

