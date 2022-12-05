
# engines.item-browser.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > engines.item-browser.api

### Index

- [body](#body)

- [browsing-item?](#browsing-item)

- [export-selection](#export-selection)

- [export-single-selection](#export-single-selection)

- [get-item](#get-item)

- [get-item-route](#get-item-route)

- [get-selected-item-count](#get-selected-item-count)

- [import-selection!](#import-selection)

- [import-single-selection!](#import-single-selection)

- [item-selected?](#item-selected)

- [toggle-item-selection!](#toggle-item-selection)

- [toggle-limited-item-selection!](#toggle-limited-item-selection)

- [toggle-single-item-selection!](#toggle-single-item-selection)

### body

```
@param (keyword) browser-id
@param (map) body-props
{:auto-title? (boolean)(opt)
  Default: false
  W/ {:label-key ...}
 :default-item-id (string)
 :default-order-by (namespaced keyword)
 :display-progress? (boolean)(opt)
  Default: true
 :download-limit (integer)(opt)
  Default: engines.item-lister.core.config/DEFAULT-DOWNLOAD-LIMIT
 :error-element (metamorphic-content)(opt)
 :ghost-element (metamorphic-content)(opt)
 :item-path (vector)(opt)
  Default: core.helpers/default-item-path
 :items-key (keyword)
 :items-path (vector)(opt)
  Default: core.helpers/default-items-path
 :label-key (keyword)
  W/ {:auto-title? true}
 :list-element (metamorphic-content)
 :path-key (keyword)
 :placeholder (metamorphic-content)(opt)
  Default: :no-items-to-show
 :prefilter (map)(opt)
 :query (vector)(opt)
 :search-keys (keywords in vector)(opt)
  Default: engines.item-lister.core.config/DEFAULT-SEARCH-KEYS
 :transfer-id (keyword)(opt)}
```

```
@usage
[body :my-browser {...}]
```

```
@usage
(defn my-list-element [browser-id] [:div ...])
[body :my-browser {:list-element #'my-list-element
                   :prefilter    {:my-type/color "red"}}]
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [body]]))

(engines.item-browser.api/body ...)
(body                          ...)
```

</details>

---

### browsing-item?

```
@param (keyword) browser-id
@param (string) item-id
```

```
@usage
(r item-browser/browsing-item? db :my-browser "my-item")
```

```
@return (boolean)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [browsing-item?]]))

(engines.item-browser.api/browsing-item? ...)
(browsing-item?                          ...)
```

</details>

---

### export-selection

```
@param (keyword) browser-id
```

```
@example
(r export-selection db :my-browser)
=>
["my-item" "your-item"]
```

```
@return (strings in vector)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [export-selection]]))

(engines.item-browser.api/export-selection ...)
(export-selection                          ...)
```

</details>

---

### export-single-selection

```
@param (keyword) browser-id
```

```
@example
(r export-single-selection db :my-browser)
=>
"my-item"
```

```
@return (string)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [export-single-selection]]))

(engines.item-browser.api/export-single-selection ...)
(export-single-selection                          ...)
```

</details>

---

### get-item

```
@param (keyword) browser-id
@param (string) item-id
```

```
@usage
(r get-item db :my-browser "my-item")
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [get-item]]))

(engines.item-browser.api/get-item ...)
(get-item                          ...)
```

</details>

---

### get-item-route

```
@param (keyword) browser-id
@param (string) item-id
```

```
@example
(r get-item-route db :my-browser "my-item")
=>
"/@app-home/my-browser/my-item"
```

```
@return (string)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [get-item-route]]))

(engines.item-browser.api/get-item-route ...)
(get-item-route                          ...)
```

</details>

---

### get-selected-item-count

```
@param (keyword) browser-id
```

```
@usage
(r get-selected-item-count db :my-browser)
```

```
@return (integer)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [get-selected-item-count]]))

(engines.item-browser.api/get-selected-item-count ...)
(get-selected-item-count                          ...)
```

</details>

---

### import-selection!

```
@param (keyword) browser-id
@param (strings in vector) selected-item-ids
```

```
@usage
(r import-selection! db :my-browser ["my-item" "your-item"])
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [import-selection!]]))

(engines.item-browser.api/import-selection! ...)
(import-selection!                          ...)
```

</details>

---

### import-single-selection!

```
@param (keyword) browser-id
@param (string) selected-item-id
```

```
@usage
(r import-single-selection! db :my-browser "my-item")
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [import-single-selection!]]))

(engines.item-browser.api/import-single-selection! ...)
(import-single-selection!                          ...)
```

</details>

---

### item-selected?

```
@param (keyword) browser-id
@param (string) item-id
```

```
@usage
(r item-selected? db :my-browser "my-item")
```

```
@return (boolean)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [item-selected?]]))

(engines.item-browser.api/item-selected? ...)
(item-selected?                          ...)
```

</details>

---

### toggle-item-selection!

```
@param (keyword) browser-id
@param (string) item-id
```

```
@usage
(r toggle-item-selection! :my-browser "my-item")
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [toggle-item-selection!]]))

(engines.item-browser.api/toggle-item-selection! ...)
(toggle-item-selection!                          ...)
```

</details>

---

### toggle-limited-item-selection!

```
@param (keyword) browser-id
@param (string) item-id
@param (integer) selection-limit
```

```
@usage
(r toggle-limited-item-selection! :my-browser "my-item" 8)
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [toggle-limited-item-selection!]]))

(engines.item-browser.api/toggle-limited-item-selection! ...)
(toggle-limited-item-selection!                          ...)
```

</details>

---

### toggle-single-item-selection!

```
@param (keyword) browser-id
@param (string) item-id
```

```
@usage
(r toggle-single-item-selection! :my-browser "my-item")
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [toggle-single-item-selection!]]))

(engines.item-browser.api/toggle-single-item-selection! ...)
(toggle-single-item-selection!                          ...)
```

</details>

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine

