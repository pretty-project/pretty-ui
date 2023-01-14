
# hiccuptopdf.api Clojure namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > hiccuptopdf.api

### Index

- [generate-base64-pdf!](#generate-base64-pdf)

- [generate-pdf!](#generate-pdf)

### generate-base64-pdf!

```
@param (hiccup) n
@param (map)(opt) options
{:author (string)(opt)
 :base-uri (string)(opt)
 :css-paths (strings in vector)(opt)
  [(string) css-path]
 :font-paths (maps in vector)(opt)
  [{:font-family (string)
    :src (string)}]
 :orientation (keyword)(opt)
  :landscape, :portrait
  Default: :portrait
 :page-size (keyword)(opt)
  Default: :A4
 :subject (string)(opt)
 :title (string)(opt)}
```

```
@usage
(generate-base64-pdf! [:html ...])
```

```
@usage
(generate-base64-pdf! [:html ...] {...})
```

```
@return (string)
```

---

### generate-pdf!

```
@param (hiccup) n
@param (map)(opt) options
{:author (string)(opt)
 :base-uri (string)(opt)
 :css-paths (strings in vector)(opt)
  [(string) css-path]
 :font-paths (maps in vector)(opt)
  [{:font-family (string)
    :src (string)}]
 :orientation (keyword)(opt)
  :landscape, :portrait
  Default: :portrait
 :page-size (keyword)(opt)
  Default: :A4
 :subject (string)(opt)
 :title (string)(opt)}
```

```
@usage
(generate-pdf! [:html ...])
```

```
@usage
(generate-pdf! [:html ...] {...})
```

```
@usage
(generate-pdf! [:html ...] {:base-uri   "http://localhost:3000/"
                            :css-paths  ["public/css/my-style.css"]
                            :font-paths [{:font-family "Montserrat"
                                          :src "public/fonts/Montserrat/.../Montserrat-Regular.ttf"}]})
```

```
@return (?)
```