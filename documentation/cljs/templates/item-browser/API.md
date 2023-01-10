
# templates.item-browser.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.item-browser.api

### Index

- [body](#body)

- [compact-bar](#compact-bar)

- [footer](#footer)

- [header](#header)

- [item-info](#item-info)

- [menu](#menu)

- [search-description](#search-description)

- [search-field](#search-field)

### body

```
@param (keyword) browser-id
@param (map) body-props
{:auto-title? (boolean)(opt)
  Default: true
 :default-order-by (namespaced keyword)(opt)
  Default: :modified-at/descending
 :item-list-header (component or symbol)(opt)
 :label-key (keyword)(opt)
  Default: :name
 :list-item-element (component or symbol)}
```

```
@usage
[body {...}]
```

```
@usage
[body :my-browser {...}]
```

```
@usage
(defn my-item-element [browser-id body-props item-dex item] ...)
[body :my-browser {:item-element  #'my-item-element}]
```

```
@usage
(defn my-item-list-header  [browser-id body-props] ...)
(defn my-list-item-element [browser-id body-props item-dex item] ...)
[body :my-browser {:item-list-header  #'my-item-list-header
                   :list-item-element #'my-list-item-element}]
```

---

### compact-bar

```
@param (keyword) browser-id
@param (map) bar-props
{:order-by-options (keywords or namespaced keywords in vector)}
```

---

### footer

```
@param (keyword) lister-id
@param (map) browser-props
```

```
@usage
[footer :my-browser {...}]
```

---

### header

```
@param (keyword) browser-id
@param (map) header-props
{:controls (metamorphic-content)(opt)
 :crumbs (maps in vector)
  [{:label (metamorphic-content)(opt)
    :placeholder (metamorphic-content)
    :route (string)(opt)}]
 :placeholder (metamorphic-content)(opt)
 :title (metamorphic-content)(opt)}
```

```
@usage
[header :my-browser {...}]
```

---

### item-info

```
@param (keyword) browser-id
@param (map) header-props
{:item-info (metamorphic-content)(opt)}
```

---

### menu

```
@param (keyword) browser-id
@param (map) menu-props
{:content (metamorphic-content)(opt)
 :group-icon (keyword)
 :group-label (metamorphic-content)}
```

```
@usage
[menu :my-lister {...}]
```

---

### search-description

```
@param (keyword) browser-id
@param (map) header-props
```

---

### search-field

```
@param (keyword) browser-id
@param (map) header-props
```