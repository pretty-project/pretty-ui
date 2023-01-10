
# templates.item-handler.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.item-handler.api

### Index

- [body](#body)

- [control-bar](#control-bar)

- [footer](#footer)

- [header](#header)

- [label-bar](#label-bar)

- [menu](#menu)

- [menu-bar](#menu-bar)

- [wrapper](#wrapper)

### body

```
@param (keyword) handler-id
@param (map) body-props
{:auto-title? (boolean)(opt)
  Default: true
 :item-element (component or symbol)
 :item-id (string)
 :label-key (keyword)(opt)
  Default: :name
 :suggestion-keys (keywords in vector)(opt)
  Default: [:name]}
```

```
@usage
[body :my-handler {...}]
```

```
@usage
(defn my-item-element [] ...)
[body :my-handler {:item-element #'my-item-element}]
```

---

### control-bar

```
@param (keyword) handler-id
@param (map) bar-props
{:base-route (string)
 :lister-id (keyword)}
```

```
@usage
[control-bar handler-id {...}]
```

---

### footer

```
@param (keyword) handler-id
@param (map) footer-props
```

```
@usage
[footer :my-handler {...}]
```

---

### header

---

### label-bar

```
@param (keyword) handler-id
@param (map) bar-props
{:crumbs (maps in vector)
  [{:label (metamorphic-content)(opt)
    :placeholder (metamorphic-content)
    :route (string)(opt)}]
 :placeholder (metamorphic-content)(opt)
 :title (metamorphic-content)(opt)}
```

```
@usage
[label-bar :my-handler {...}]
```

---

### menu

```
@param (keyword) handler-id
@param (map) menu-props
{:content (metamorphic-content)(opt)
 :group-icon (keyword)
 :group-icon-family (keyword)(opt)
  Default: :material-icons-filled
 :group-label (metamorphic-content)}
```

```
@usage
[menu :my-handler {...}]
```

---

### menu-bar

```
@param (keyword) handler-id
@param (map) bar-props
{:menu-items (maps in vector)
  [{:disabled? (boolean)(opt)
     Default: false
    :label (metamorphic-content)}]}
```

```
@usage
[menu-bar :my-handler {...}]
```

---

### wrapper

```
@param (keyword) handler-id
@param (map) wrapper-props
{:body (metamorphic content)
 :footer (metamorphic content)
 :header (metamorphic content)}
```