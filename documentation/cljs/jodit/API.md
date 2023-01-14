
# jodit.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > jodit.api

### Index

- [body](#body)

### body

```
@param (keyword)(opt) editor-id
@param (map) editor-props
{:autofocus? (boolean)(opt)
  Default: false
 :buttons (keywords in vector)(opt)
  [:bold, :italic, :underline, :font, :font-size, :cut, :copy, :paste
   :link, :undo, :redo, :brush]
  Default: [:bold :italic :underline :brush]
 :disabled? (boolean)(opt)
  Default: false
 :insert-as (keyword)(opt)
  :cleared-html, :html, :only-text, :plain-text
  Default: :cleared-html
 :min-height (px)(opt)
 :on-blur (function)(opt)
 :on-change (function)(opt)
 :on-focus (function)(opt)
 :placeholder (metamorphic-content)(opt)
  Default: :write-something!
 :update-trigger (keyword or string)(opt)
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