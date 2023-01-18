
# dnd-kit.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > dnd-kit.api

### Index

- [body](#body)

### body

```
@param (keyword)(opt) sortable-id
@param (map) sortable-props
{:item-element (component or symbol)
 :item-id-f (function)(opt)
  Default: return
 :items (vector)
 :on-drag-end (function)(opt)
 :on-drag-start (function)(opt)
 :on-order-changed (function)(opt)}
```

```
@usage
[body {...}]
```

```
@usage
[body :my-sortable {...}]
```

```
@usage
(defn my-item-element [item-dex item drag-props]
                      (let [{:keys [dragging? handle-attributes item-attributes]} drag-props]))
[body :my-sortable {:item-element #'my-item-element
                    :items        ["My item" "Your item"]}]
```

```
@usage
(defn my-item-element [my-param item-dex item drag-props]
                      (let [{:keys [dragging? handle-attributes item-attributes]} drag-props]))
[body :my-sortable {:item-element [my-item-element "My value"]
                    :items        ["My item" "Your item"]}]
```

```
@usage
(defn on-drag-start-f    [sortable-id sortable-props])
(defn on-drag-end-f      [sortable-id sortable-props])
(defn on-order-changed-f [sortable-id sortable-props items])
[body :my-sortable {:on-drag-start    on-drag-start-f
                    :on-drag-end      on-drag-end-f
                    :on-order-changed on-order-changed-f}]
```