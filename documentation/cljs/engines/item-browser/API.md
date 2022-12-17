
# engines.item-browser.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > engines.item-browser.api

### Index

- [body](#body)

- [browsing-item?](#browsing-item)

- [export-selection](#export-selection)

- [export-single-selection](#export-single-selection)

- [get-copy-item-id](#get-copy-item-id)

- [get-default-item-id](#get-default-item-id)

- [get-deleted-item-id](#get-deleted-item-id)

- [get-item](#get-item)

- [get-item-route](#get-item-route)

- [get-parent-item-id](#get-parent-item-id)

- [get-recovered-item-id](#get-recovered-item-id)

- [get-selected-item-count](#get-selected-item-count)

- [import-selection!](#import-selection)

- [import-single-selection!](#import-single-selection)

- [item-downloaded?](#item-downloaded)

- [item-selected?](#item-selected)

- [parent-item-browsed?](#parent-item-browsed)

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
 :item-id (string)(opt)
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

---

### get-copy-item-id

```
@param (keyword) browser-id
@param (map) server-response
```

```
@example
(r get-duplicated-item-id :my-browser {my-handler/duplicate-item! {:my-type/id "my-item"}})
=>
"my-item"
```

```
@return (string)
```

---

### get-default-item-id

```
@param (keyword) browser-id
```

```
@usage
(r get-default-item-id db :my-browser)
```

```
@return (string)
```

---

### get-deleted-item-id

```
@param (keyword) browser-id
@param (map) server-response
```

```
@example
(r get-deleted-item-id :my-browser {my-handler/delete-item! "my-item"})
=>
"my-item"
```

```
@return (string)
```

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

---

### get-parent-item-id

```
@param (keyword) browser-id
```

```
@usage
(r get-parent-item-id db :my-browser)
```

```
@return (string)
```

---

### get-recovered-item-id

```
@param (keyword) browser-id
@param (map) server-response
```

```
@example
(r get-recovered-item-id :my-browser {my-handler/undo-delete-item! {:my-type/id "my-item"}})
=>
"my-item"
```

```
@return (string)
```

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

---

### item-downloaded?

```
@param (keyword) browser-id
@param (string) item-id
```

```
@usage
(r item-downloaded? db :my-browser "my-item")
```

```
@return (boolean)
```

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

---

### parent-item-browsed?

```
@param (keyword) browser-id
@param (keyword) action-key
@param (map) server-response
```

```
@usage
(r parent-item-browsed? db :my-browser :delete-item! {...})
```

```
@return (boolean)
```

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

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine

