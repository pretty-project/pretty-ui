
# engines.item-handler.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > engines.item-handler.api

### Index

- [current-item-changed](#current-item-changed)

- [downloader](#downloader)

- [get-copy-item-id](#get-copy-item-id)

- [get-deleted-item-id](#get-deleted-item-id)

- [get-recovered-item-id](#get-recovered-item-id)

- [get-saved-item-id](#get-saved-item-id)

- [handling-item?](#handling-item)

### current-item-changed

```
@description
Checks whether the item really changed, if yes the function stores the
{:changed? true} meta-item in the item.
```

```
@param (keyword) handler-id
```

```
@usage
(r current-item-changed! db handler-id)
```

```
@return (map)
```

---

### downloader

```
@param (keyword) handler-id
@param (map) body-props
{:auto-title? (boolean)(opt)
  Default: false
  W/ {:label-key ...}
 :clear-behaviour (keyword)(opt)
  :none, :on-leave, :on-item-change
  Default: :none
 :default-item (map)(opt)
 :display-progress? (boolean)(opt)
  Default: true
 :initial-item (map)(opt)
 :item-id (string)(opt)
 :items-path (vector)(opt)
  Default: core.helpers/default-items-path
 :label-key (keyword)(opt)
  W/ {:auto-title? true}
 :query (vector)(opt)
  XXX#7059 (source-code/cljs/engines/engine_handler/core/subs.cljs)
 :suggestion-keys (keywords in vector)(opt)
 :suggestions-path (vector)(opt)
  Default: core.helpers/default-suggestions-path
 :transfer-id (keyword)(opt)}
```

```
@usage
[body :my-handler {...}]
```

---

### get-copy-item-id

```
@param (keyword) handler-id
@param (map) server-response
```

```
@example
(r get-copy-item-id :my-handler {my-handler/duplicate-item! {:my-type/id "my-item"}})
=>
"my-item"
```

```
@return (string)
```

---

### get-deleted-item-id

```
@param (keyword) handler-id
@param (map) server-response
```

```
@example
(r get-deleted-item-id :my-handler {my-handler/delete-item! "my-item"})
=>
"my-item"
```

```
@return (string)
```

---

### get-recovered-item-id

```
@param (keyword) viewer-id
@param (map) server-response
```

```
@example
(r get-recovered-item-id :my-viewer {my-handler/undo-delete-item! {:my-type/id "my-item"}})
=>
"my-item"
```

```
@return (string)
```

---

### get-saved-item-id

```
@param (keyword) handler-id
@param (map) server-response
```

```
@example
(r get-saved-item-id :my-handler {my-handler/save-item! {:my-type/id "my-item"}})
=>
"my-item"
```

```
@return (string)
```

---

### handling-item?

```
@param (keyword) handler-id
@param (string) item-id
```

```
@usage
(r handling-item? db :my-handler "my-item")
```

```
@return (boolean)
```