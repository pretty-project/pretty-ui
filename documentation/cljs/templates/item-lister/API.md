
# templates.item-lister.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.item-lister.api

### Index

- [body](#body)

- [compact-bar](#compact-bar)

- [compact-wrapper](#compact-wrapper)

- [create-item-button](#create-item-button)

- [footer](#footer)

- [header](#header)

- [label-bar](#label-bar)

- [list-header](#list-header)

- [menu](#menu)

- [search-bar](#search-bar)

- [search-description](#search-description)

- [search-field](#search-field)

- [wide-wrapper](#wide-wrapper)

### body

```
@param (keyword) lister-id
@param (map) body-props
{:default-order-by (namespaced keyword)(opt)
  Default: :modified-at/descending
 :list-item-element (component or symbol)
 :list-item-header (component or symbol)(opt)
 :items-path (vector)
 :on-order-changed (metamorphic-event)(opt)
  This event takes the reordered items as its last parameter
  W/ {:sortable? true}
 :sortable? (boolean)(opt)
  Default: false}
```

```
@usage
[body {...}]
```

```
@usage
[body :my-lister {...}]
```

```
@usage
(defn my-item-element [lister-id body-props item-dex item] ...)
[body :my-lister {:item-element  #'my-item-element}]
```

```
@usage
(defn my-item-list-header  [lister-id body-props] ...)
(defn my-list-item-element [lister-id body-props item-dex item drag-props] ...)
[body :my-lister {:item-list-header  #'my-item-list-header
                  :list-item-element #'my-list-item-element
                  :sortable?         true}]
```

---

### compact-bar

```
@param (keyword) lister-id
@param (map) bar-props
{:on-search (metamorphic-event)
 :order-by-options (keywords or namespaced keywords in vector)
 :search-placeholder (metamorphic-content)}
```

```
@usage
[compact-bar :my-lister {...}]
```

---

### compact-wrapper

```
@param (keyword) lister-id
@param (map) wrapper-props
{:body (metamorphic content)
 :footer (metamorphic content)
 :header (metamorphic content)}
```

```
@usage
[compact-wrapper :my-lister {...}]
```

---

### create-item-button

```
@param (keyword) lister-id
@param (map) menu-props
{:on-click (metamorphic-event)}
```

```
@usage
[create-item-button :my-lister {...}]
```

---

### footer

```
@param (keyword) lister-id
@param (map) footer-props
```

```
@usage
[footer :my-lister {...}]
```

---

### header

---

### label-bar

```
@param (keyword) lister-id
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
[label-bar :my-lister {...}]
```

---

### list-header

```
@param (keyword) lister-id
@param (map) header-props
{:cells (maps in vector)
  [{:label (metamorphic-content)(opt)
    :order-by-key (keyword)}]
 :template (string)}
```

```
@usage
[list-header :my-lister {...}]
```

---

### menu

```
@param (keyword) lister-id
@param (map) menu-props
{:content (metamorphic-content)(opt)
 :group-icon (keyword)
 :group-icon-family (keyword)(opt)
  Default: :material-icons-filled
 :group-label (metamorphic-content)}
```

```
@usage
[menu :my-lister {...}]
```

---

### search-bar

```
@param (keyword) lister-id
@param (map) bar-props
{:on-search (metamorphic-event)
 :placeholder (metamorphic-content)}
```

```
@usage
[search-bar :my-lister {...}]
```

---

### search-description

```
@param (keyword) lister-id
@param (map) bar-props
```

---

### search-field

```
@param (keyword) lister-id
@param (map) bar-props
{:on-search (metamorphic-event)
 :search-placeholder (metamorphic-content)}
```

---

### wide-wrapper

```
@param (keyword) lister-id
@param (map) wrapper-props
{:body (metamorphic content)
 :footer (metamorphic content)
 :header (metamorphic content)}
```

```
@usage
[wide-wrapper :my-lister {...}]
```