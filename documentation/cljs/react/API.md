
# react.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > react.api

### Index

- [get-reference](#get-reference)

- [mount-animation](#mount-animation)

- [set-reference-f](#set-reference-f)

### get-reference

```
@param (keyword) element-id
```

```
@usage
(get-reference :my-element)
```

```
@return (?)
```

---

### mount-animation

```
@param (component) component
@param (map) options
{:animation-timeout (ms)(opt)
  Default: 350
 :mounted? (boolean)(opt)
  Default: false}
```

```
@usage
(defn my-component   [] [:div "My component"])
(defn your-component [] [mount-animation {:mounted? true}
                                         [my-component]])
```

---

### set-reference-f

```
@param (keyword) element-id
```

```
@usage
[:div {:ref (set-reference-f :my-element)}]
```

```
@return (function)
```