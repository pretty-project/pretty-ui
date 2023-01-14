
# templates.item-selector.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.item-selector.api

### Index

- [body](#body)

- [control-bar](#control-bar)

- [footer](#footer)

- [item-counter](#item-counter)

- [item-marker](#item-marker)

- [label-bar](#label-bar)

### body

```
@param (keyword) selector-id
@param (map) body-props
{:default-order-by (namespaced keyword)(opt)
  Default: :modified-at/descending
 :engine (keyword)(opt)
  :item-browser, :item-lister
  Default: :item-lister
 :list-item-element (component or symbol)}
```

```
@usage
[body :my-selector {...}]
```

```
@usage
(defn my-list-item-element [selector-id selector-props item-dex item] ...)
[body :my-selector {:list-item-element #'my-list-item-element}]
```

---

### control-bar

```
@param (keyword) selector-id
@param (map) bar-props
{:disabled? (boolean)(opt)
 :order-by-options (namespaced keywords in vector)
 :search-field-placeholder (metamorphic-content)(opt)
 :search-keys (keywords in vector)}
```

```
@usage
[control-bar :my-selector {...}]
```

---

### footer

```
@param (keyword) selector-id
@param (map) footer-props
{:on-discard (metamorphic-event)
 :selected-item-count (integer)}
```

```
@usage
[footer :my-selector {...}]
```

---

### item-counter

```
@param (keyword) selector-id
@param (integer) item-dex
@param (map) counter-props
```

```
@usage
[item-counter :my-selector 42 {...}]
```

---

### item-marker

```
@param (keyword) selector-id
@param (integer) item-dex
@param (map) marker-props
{:disabled? (boolean)(opt)
 :item-id (string)}
```

```
@usage
[item-marker :my-selector 42 {...}]
```

---

### label-bar

```
@param (keyword) selector-id
@param (map) bar-props
{:label (metamorphic-content)
 :on-close (metamorphic-event)}
```

```
@usage
[label-bar :my-selector {...}]
```