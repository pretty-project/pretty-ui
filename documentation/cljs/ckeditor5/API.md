
# ckeditor5.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > ckeditor5.api

### Index

- [body](#body)

### body

```
@param (keyword)(opt) editor-id
@param (map) editor-props
{:autofocus? (boolean)(opt)
  Default: false
 :buttons (keywords in vector)(opt)
  [:fontColor, :fontBackgroundColor, :heading, :bold, :italic, :underline, :link, ...]
 :disabled? (boolean)(opt)
  Default: false
 :font-colors (maps in vector)(opt)
  [{:color (string)
    :label (string)}]
 :fill-colors (maps in vector)(opt)
  [{:color (string)
    :label (string)}]
 :on-blur (function)(opt)
 :on-change (function)(opt)
 :on-focus (function)(opt)
 :placeholder (metamorphic-content)(opt)
 :value (string)(opt)}
```

```
@usage
[body {...}]
```

```
@usage
[body :my-editor {...}]
```

```
@usage
(defn on-blur-f   [editor-id editor-props])
(defn on-focus-f  [editor-id editor-props])
(defn on-change-f [editor-id editor-props value])
[body :my-editor {:on-blur   on-blur-f
                  :on-focus  on-focus-f
                  :on-change on-change-f}]
```