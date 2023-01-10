
# iso.forms.api isomorphic namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > iso.forms.api

### Index

- [items-different?](#items-different)

- [valid-string](#valid-string)

### items-different?

```
@param (map) a
@param (map) b
@param (vector) keys
```

```
@usage
(items-different? {...} {...})
```

```
@usage
(items-different? {...} {...} [...])
```

```
@example
(items-different? {:color "Red"   :size "XL"}
                  {:color "Green" :size "XL"})
=>
true
```

```
@example
(items-different? {:color "Red"}
                  {:color "Red" :size "XL"})
=>
false
```

```
@example
(items-different? {:color "Red"   :size "XL"}
                  {:color "Green" :size "XL"}
                  [:color])
=>
true
```

```
@example
(items-different? {:color "Red"   :size "XL"}
                  {:color "Green" :size "XL"}
                  [:size])
=>
false
```

```
@return (boolean)
```

---

### valid-string

```
@param (string) n
```

```
@usage
(valid-string " abCd12 ")
=>
"abCd12"
```

```
@return (string)
```