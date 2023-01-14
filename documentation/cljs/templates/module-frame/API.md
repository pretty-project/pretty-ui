
# templates.module-frame.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.module-frame.api

### Index

- [get-layout](#get-layout)

- [layout-selected?](#layout-selected)

- [set-layout!](#set-layout)

- [side-menu](#side-menu)

- [wrapper](#wrapper)

### get-layout

```
@param (keyword) module-id
```

```
@usage
(get-layout :my-module)
```

```
@example
(get-layout :my-module)
=>
:my-layout
```

```
@return (keyword)
```

---

### layout-selected?

```
@param (keyword) module-id
@param (keyword) layout
```

```
@usage
(layout-selected? :my-module :my-layout)
```

```
@return (boolean)
```

---

### set-layout!

```
@param (keyword) module-id
@param (keyword) layout
```

```
@usage
(set-layout! :my-module :my-layout)
```

---

### side-menu

```
@param (keyword) module-id
@param (map) menu-props
```

---

### wrapper

```
@param (keyword) module-id
@param (map) wrapper-props
{:content (metamorphic content)
 :menu (metamorphic content)(opt)}
```