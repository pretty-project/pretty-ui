
# templates.item-picker.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.item-picker.api

### Index

- [body](#body)

### body

```
@param (keyword)(opt) picker-id
@param (map) picker-props
{:autosave? (boolean)(opt)
  Default: false
 :disabled? (boolean)(opt)
 :export-filter-f (function)(opt)
  Default: (fn [item-id] {:id item-id})
  W/ {:multi-select? true}
 :import-id-f (function)(opt)
  Default: (fn [{:keys [id] :as item-link}] id)
  W/ {:multi-select? true}
 :indent (map)(opt)
 :info-text (metamorphic-content)(opt)
 :item-element (component or symbol)(opt)
  W/ {:multi-select? false}
 :item-list-header (component or symbol)(opt)
  W/ {:multi-select? true}
 :items-path (vector)(opt)
  Az elemeket megjelenítő engines.item-lister.api/body komponens paramétere.
  W/ {:multi-select? true}
 :label (metamorphic-content)(opt)
 :list-item-element (component or symbol)(opt)
  W/ {:multi-select? true}
 :max-count (integer)(opt)
  Default: 8
  W/ {:multi-select? true}
 :multi-select? (boolean)(opt)
  Default: false
 :on-change (metamorphic-event)(opt)
  This event takes the selected item(s) as its last parameter
 :on-save (metamorphic-event)(opt)
  This event takes the selected item(s) as its last parameter
 :on-select (metamorphic-event)
 :outdent (map)(opt)
 :required? (boolean)(opt)
  Default: false
 :placeholder (metamorphic-content)(opt)
 :sortable? (boolean)(opt)
  Default: false
 :toggle-label (metamorphic-content)
 :transfer-id (keyword)(opt)
  Az elemeket megjelenítő engines.item-lister.api/body komponens paramétere.
  W/ {:multi-select? true}
 :value-path (vector)}
```

```
@usage
[body {...}]
```

```
@usage
[body :my-item-picker {...}]
```

```
@usage
(defn my-item-element [picker-id picker-props item-link] ...)
[body :my-item-picker {:item-element  #'my-item-element
                       :multi-select? false}]
```

```
@usage
(defn my-list-item-element [picker-id picker-props item-dex item-link] ...)
[body :my-item-picker {:list-item-element #'my-list-item-element
                       :multi-select?     true}]
```

```
@usage
(defn my-item-list-header  [picker-id picker-props] ...)
(defn my-list-item-element [picker-id picker-props item-dex item-link drag-props] ...)
[body :my-item-picker {:item-list-header  #'my-item-list-header
                       :list-item-element #'my-list-item-element
                       :multi-select?     true
                       :sortable?         true}]
```