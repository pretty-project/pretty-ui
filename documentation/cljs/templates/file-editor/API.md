
# templates.file-editor.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.file-editor.api

### Index

- [body](#body)

- [header](#header)

### body

```
@param (keyword) editor-id
@param (map) body-props
{:form-element (component or symbol)}
```

```
@usage
[body :my-editor {...}]
```

```
@usage
(defn my-form-element [] ...)
[body :my-editor {:form-element #'my-form-element}]
```

---

### header

```
@param (keyword) editor-id
@param (map) header-props
{:crumbs (maps in vector)
  [{:label (metamorphic-content)(opt)
    :placeholder (metamorphic-content)(opt)
    :route (string)(opt)}]
 :menu-items (maps in vector)(opt)
  [{:change-keys (keywords in vector)(opt)
    :disabled? (boolean)(opt)
    :label (metamorphic-content)}]
 :placeholder (metamorphic-content)(opt)
 :title (metamorphic-content)(opt)}
```

```
@usage
[header :my-editor {...}]
```