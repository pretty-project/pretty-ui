
# templates.board-frame.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > templates.board-frame.api

### Index

- [get-layout](#get-layout)

- [layout-selected?](#layout-selected)

- [set-layout!](#set-layout)

- [side-menu](#side-menu)

- [wrapper](#wrapper)

### get-layout

```
@param (keyword) board-id
```

```
@usage
(get-layout :my-board)
```

```
@example
(get-layout :my-board)
=>
:my-layout
```

```
@return (keyword)
```

---

### layout-selected?

```
@param (keyword) board-id
@param (keyword) layout
```

```
@usage
(layout-selected? :my-board :my-layout)
```

```
@return (boolean)
```

---

### set-layout!

```
@param (keyword) board-id
@param (keyword) layout
```

```
@usage
(set-layout! :my-board :my-layout)
```

---

### side-menu

```
@param (keyword) board-id
@param (map) menu-props
```

```
@usage
[side-menu :my-board {...}]
```

---

### wrapper

```
@param (keyword) board-id
@param (map) wrapper-props
{:content (metamorphic content)
 :menu (metamorphic content)(opt)}
```