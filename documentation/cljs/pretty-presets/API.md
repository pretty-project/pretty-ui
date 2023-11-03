
# pretty-presets.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > pretty-presets.api

### Index

- [apply-preset](#apply-preset)

- [reg-preset!](#reg-preset)

### apply-preset

```
@param (map) element-props
{:preset (keyword)(opt)}
```

```
@usage
(apply-preset {:fill-color :muted
               :preset     :my-preset})
```

```
@example
(reg-preset! :my-preset {:hover-color :highlight})
(apply-preset {:fill-color :muted
               :preset     :my-preset})
=>
{:fill-color  :muted
 :hover-color :highlight
 :preset      :my-preset}
```

```
@return (map)
```

<details>
<summary>Source code</summary>

```
(defn apply-preset
  [{:keys [preset] :as element-props}]
  (if-let [preset (get @preset-pool.state/PRESETS preset)]
          (cond-> element-props :avoiding-infinite-loops (dissoc :preset)
                                (-> preset fn?)          (preset)
                                (-> preset map?)         (map/reversed-merge preset)
                                :recursivelly-applying   (apply-preset))
          (-> element-props)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-presets.api :refer [apply-preset]]))

(pretty-presets.api/apply-preset ...)
(apply-preset                    ...)
```

</details>

---

### reg-preset!

```
@param (keyword) preset-id
@param (function or map) preset
```

```
@usage
(reg-preset! :my-button {:fill-color  :muted 
                         :hover-color :muted})
```

```
@usage
(reg-preset! :my-button #(assoc % :fill-color  :muted
                                  :hover-color :muted))
```

<details>
<summary>Source code</summary>

```
(defn reg-preset!
  [preset-id preset-props]
  (swap! preset-pool.state/PRESETS assoc preset-id preset-props))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [pretty-presets.api :refer [reg-preset!]]))

(pretty-presets.api/reg-preset! ...)
(reg-preset!                    ...)
```

</details>

---

This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.

