
# engines.item-lister.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > engines.item-lister.api

### Index

- [downloader](#downloader)

- [first-data-received?](#first-data-received)

- [get-deleted-item-ids](#get-deleted-item-ids)

- [get-duplicated-item-ids](#get-duplicated-item-ids)

### downloader

```
@param (keyword) lister-id
@param (map) body-props
{:default-order-by (namespaced keyword)
 :display-progress? (boolean)(opt)
  Default: true
 :clear-behaviour (keyword)(opt)
  :none, :on-leave
  Default: :none
 :download-limit (integer)(opt)
  Default: 15
 :items-path (vector)(opt)
  Default: core.helpers/default-items-path
 :order-key (keyword)(opt)
  Default: :order
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
[body :my-lister {:prefilter {:my-type/color "red"}}]
```

---

### first-data-received?

```
@param (keyword) lister-id
```

```
@return (boolean)
```

---

### get-deleted-item-ids

```
@param (keyword) lister-id
@param (map) server-response
```

```
@example
(r get-deleted-item-ids :my-lister {my-handler/delete-items! ["my-item"]})
=>
["my-item"]
```

```
@return (strings in vector)
```

---

### get-duplicated-item-ids

```
@param (keyword) lister-id
@param (map) server-response
```

```
@example
(r get-duplicated-item-ids :my-lister {my-handler/duplicate-items! [{:my-type/id "my-item"}]})
=>
["my-item"]
```

```
@return (strings in vector)
```