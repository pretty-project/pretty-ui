
# templates.static-page.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.static-page.api

### Index

- [body](#body)

- [footer](#footer)

- [header](#header)

- [side-menu](#side-menu)

### body

```
@param (keyword) page-id
@param (map) body-props
{:content (metamorphic-content)}
```

```
@usage
[body :my-static-page {...}]
```

```
@usage
(defn my-content [page-id] ...)
[body :my-static-page {:content #'my-content}]
```

---

### footer

```
@param (keyword) page-id
@param (map) footer-props
{:content (metamorphic-content)}
```

```
@usage
[footer :my-static-page {...}]
```

---

### header

```
@param (keyword) page-id
@param (map) header-props
{:crumbs (maps in vector)
  [{:label (metamorphic-content)(opt)
    :placeholder (metamorphic-content)(opt)
    :route (string)(opt)}]
 :disabled? (boolean)(opt)
 :placeholder (metamorphic-content)(opt)
 :title (metamorphic-content)(opt)}
```

```
@usage
[header :my-static-page {...}]
```

---

### side-menu

```
@param (keyword) module-id
@param (map) menu-props
```

```
@usage
[side-menu :my-static-page {...}]
```