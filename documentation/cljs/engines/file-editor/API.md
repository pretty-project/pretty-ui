
# engines.file-editor.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > engines.file-editor.api

### Index

- [body](#body)

- [form-changed?](#form-changed)

### body

```
@param (keyword) editor-id
@param (map) body-props
{:content-path (vector)(opt)
  Default: core.helpers/default-content-path
 :clear-behaviour (keyword)(opt)
  :none, :on-leave
  Default: :none
 :default-content (map)(opt)
 :display-progress? (boolean)(opt)
  Default: true
 :error-element (metamorphic-content)(opt)
 :form-element (metamorphic-content)
 :ghost-element (metamorphic-content)(opt)
 :query (vector)(opt)
 :transfer-id (keyword)(opt)}
```

```
@usage
[body :my-editor {...}]
```

```
@usage
(defn my-form-element [] [:div ...])
[body :my-editor {:form-element #'my-form-element}]
```

---

### form-changed?

```
@param (keyword) editor-id
@param (keywords in vector) change-keys
```

```
@usage
(r form-changed? db :my-editor [:name :email-address])
```

```
@return (boolean)
```