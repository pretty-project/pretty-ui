
# tools.temporary-component.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > tools.temporary-component.api

### Index

- [append-component!](#append-component)

- [remove-component!](#remove-component)

### append-component!

```
@param (component) component
@param (function)(opt) render-callback
```

```
@usage W/O callback
(defn my-component [])
(append-component! [my-component])
```

```
@usage W/ callback
(defn my-button [] [:a {:href "foo/bar"}])
(defn click-my-button! [] ...)
(append-component! [my-button] click-my-button!)
```

---

### remove-component!

```
@usage
(remove-component!)
```