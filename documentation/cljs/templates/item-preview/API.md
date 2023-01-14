
# templates.item-preview.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.item-preview.api

### Index

- [body](#body)

### body

```
@warning
Pass the preview-id!
BUG#9980
```

```
@param (keyword)(opt) preview-id
@param (map) preview-props
{:disabled? (boolean)(opt)
 :import-id-f (function)(opt)
  Default: (fn [{:keys [id] :as item-link}] id)
 :indent (map)(opt)
 :info-text (metamorphic-content)(opt)
 :item-link (namespaced map)(opt)
 :item-path (vector)(opt)
 :label (metamorphic-content)(opt)
 :outdent (map)(opt)
 :placeholder (metamorphic-content)(opt)
 :preview-element (component or symbol)
 :transfer-id (keyword)(opt)
  Az elemet megjelenítő engines.item-preview.api/body komponens paramétere.
```

```
@usage
[body {...}]
```

```
@usage
[body :my-item-preview {...}]
```

```
@usage
(defn my-preview-element [preview-id preview-props] ...)
[body :my-item-preview {:preview-element #'my-preview-element}]
```

```
@usage
[body :my-item-preview {:placeholder "My placeholder"}]
```

```
@usage
(defn my-placeholder [preview-id preview-props] ...)
[body :my-item-preview {:placeholder #'my-placeholder}]
```