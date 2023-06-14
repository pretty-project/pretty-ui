
# diagrams.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > diagrams.api

### Index

- [circle-diagram](#circle-diagram)

- [line-diagram](#line-diagram)

- [point-diagram](#point-diagram)

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

<details>
<summary>Source code</summary>

```
(defn diagram
  ([diagram-props]
   [diagram (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (circle-diagram.prototypes/diagram-props-prototype diagram-props)]
        [circle-diagram diagram-id diagram-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [diagrams.api :refer [circle-diagram]]))

(diagrams.api/circle-diagram ...)
(circle-diagram              ...)
```

</details>

---

### line-diagram

```
@param (keyword)(opt) diagram-id
@param (map) diagram-props
{:indent (map)(opt)
  {:bottom (keyword)(opt)
   :left (keyword)(opt)
   :right (keyword)(opt)
   :top (keyword)(opt)
   :horizontal (keyword)(opt)
   :vertical (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
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
  Default: Sum of the section values
 :width (keyword)(opt)
  auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :auto}
```

```
@usage
[line-diagram {...}]
```

```
@usage
[line-diagram :my-line-diagram {...}]
```

<details>
<summary>Source code</summary>

```
(defn diagram
  ([diagram-props]
   [diagram (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (line-diagram.prototypes/diagram-props-prototype diagram-props)]
        [line-diagram diagram-id diagram-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [diagrams.api :refer [line-diagram]]))

(diagrams.api/line-diagram ...)
(line-diagram              ...)
```

</details>

---

### point-diagram

```
@warning
UNFINISHED! DO NOT USE!
```

```
@param (keyword)(opt) diagram-id
@param (map) diagram-props
{:color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
  W/ {:label ...}
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
 :points (integers in vector)
 :strength (px)(opt)
   Default: 2
   Min: 1
   Max: 6}
```

```
@usage
[point-diagram {...}]
```

```
@usage
[point-diagram :my-point-diagram {...}]
```

<details>
<summary>Source code</summary>

```
(defn diagram
  ([diagram-props]
   [diagram (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let []        [point-diagram diagram-id diagram-props])))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [diagrams.api :refer [point-diagram]]))

(diagrams.api/point-diagram ...)
(point-diagram              ...)
```

</details>

---

This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.

