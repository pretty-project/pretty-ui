
# engines.item-preview.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > engines.item-preview.api

### Index

- [body](#body)

### body

```
@param (keyword) preview-id
@param (map) body-props
{:clear-behaviour (keyword)(opt)
  :none, :on-leave, :on-item-change
  Default: :none
 :display-progress? (boolean)(opt)
  Default: false
 :error-element (metamorphic-content)(opt)
 :ghost-element (metamorphic-content)(opt)
 :item-id (string)
 :items-path (vector)(opt)
  Default: core.helpers/default-items-path
 :preview-element (metamorphic-content)
 :query (vector)(opt)
 :transfer-id (keyword)(opt)}
```

```
@usage
[body :my-preview {...}]
```

```
@usage
(defn my-preview-element [] [:div ...])
[body :my-preview {:item-id "my-item"
                   :preview-element #'my-preview-element}]
```