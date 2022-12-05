
# engines.item-lister.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > engines.item-lister.api

### Index

- [body](#body)

- [export-selection](#export-selection)

- [export-single-selection](#export-single-selection)

- [get-item](#get-item)

- [get-selected-item-count](#get-selected-item-count)

- [import-selection!](#import-selection)

- [import-single-selection!](#import-single-selection)

- [item-selected?](#item-selected)

- [toggle-item-selection!](#toggle-item-selection)

- [toggle-limited-item-selection!](#toggle-limited-item-selection)

- [toggle-single-item-selection!](#toggle-single-item-selection)

### body

```
@param (keyword) lister-id
@param (map) body-props
{:default-order-by (namespaced keyword)
 :display-progress? (boolean)(opt)
  Default: true
 :download-limit (integer)(opt)
  Default: core.config/DEFAULT-DOWNLOAD-LIMIT
 :error-element (metamorphic-content)(opt)
 :ghost-element (metamorphic-content)(opt)
 :items-path (vector)(opt)
  Default: core.helpers/default-items-path
 :list-element (metamorphic-content)
 :order-key (keyword)(opt)
  Default: :order
 :placeholder (metamorphic-content)(opt)
  Default: :no-items-to-show
 :prefilter (map)(opt)
 :query (vector)(opt)
 :transfer-id (keyword)(opt)}
```

```
@usage
[body :my-lister {...}]
```

```
@usage
(defn my-list-element [lister-id] [:div ...])
[body :my-lister {:list-element #'my-list-element
                  :prefilter    {:my-type/color "red"}}]
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [body]]))

(engines.item-lister.api/body ...)
(body                         ...)
```

</details>

---

### export-selection

```
@param (keyword) lister-id
```

```
@example
(r export-selection db :my-lister)
=>
["my-item" "your-item"]
```

```
@return (strings in vector)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [export-selection]]))

(engines.item-lister.api/export-selection ...)
(export-selection                         ...)
```

</details>

---

### export-single-selection

```
@param (keyword) lister-id
```

```
@example
(r export-single-selection db :my-lister)
=>
"my-item"
```

```
@return (string)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [export-single-selection]]))

(engines.item-lister.api/export-single-selection ...)
(export-single-selection                         ...)
```

</details>

---

### get-item

```
@param (keyword) lister-id
@param (string) item-id
```

```
@usage
(r get-item db :my-lister "my-item")
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [get-item]]))

(engines.item-lister.api/get-item ...)
(get-item                         ...)
```

</details>

---

### get-selected-item-count

```
@param (keyword) lister-id
```

```
@usage
(r get-selected-item-count db :my-lister)
```

```
@return (integer)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [get-selected-item-count]]))

(engines.item-lister.api/get-selected-item-count ...)
(get-selected-item-count                         ...)
```

</details>

---

### import-selection!

```
@param (keyword) lister-id
@param (strings in vector) selected-item-ids
```

```
@usage
(r import-selection! db :my-lister ["my-item" "your-item"])
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [import-selection!]]))

(engines.item-lister.api/import-selection! ...)
(import-selection!                         ...)
```

</details>

---

### import-single-selection!

```
@param (keyword) lister-id
@param (string) selected-item-id
```

```
@usage
(r import-single-selection! db :my-lister "my-item")
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [import-single-selection!]]))

(engines.item-lister.api/import-single-selection! ...)
(import-single-selection!                         ...)
```

</details>

---

### item-selected?

```
@param (keyword) lister-id
@param (string) item-id
```

```
@usage
(r item-selected? db :my-lister "my-item")
```

```
@return (boolean)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [item-selected?]]))

(engines.item-lister.api/item-selected? ...)
(item-selected?                         ...)
```

</details>

---

### toggle-item-selection!

```
@param (keyword) lister-id
@param (string) item-id
```

```
@usage
(r toggle-item-selection! :my-lister "my-item")
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [toggle-item-selection!]]))

(engines.item-lister.api/toggle-item-selection! ...)
(toggle-item-selection!                         ...)
```

</details>

---

### toggle-limited-item-selection!

```
@param (keyword) lister-id
@param (string) item-id
@param (integer) selection-limit
```

```
@usage
(r toggle-limited-item-selection! :my-lister "my-item" 8)
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [toggle-limited-item-selection!]]))

(engines.item-lister.api/toggle-limited-item-selection! ...)
(toggle-limited-item-selection!                         ...)
```

</details>

---

### toggle-single-item-selection!

```
@param (keyword) lister-id
@param (string) item-id
```

```
@usage
(r toggle-single-item-selection! :my-lister "my-item")
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-lister.api :refer [toggle-single-item-selection!]]))

(engines.item-lister.api/toggle-single-item-selection! ...)
(toggle-single-item-selection!                         ...)
```

</details>

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine

