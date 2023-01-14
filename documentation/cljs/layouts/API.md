
# layouts.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > layouts.api

### Index

- [popup-a](#popup-a)

- [popup-b](#popup-b)

- [sidebar-b](#sidebar-b)

- [surface-a](#surface-a)

### popup-a

```
@param (keyword) popup-id
@param (map) popup-props
{:body (metamorphic-content)
 :border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :close-by-cover? (boolean)(opt)
  Default: true
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :footer (metamorphic-content)(opt)
 :header (metamorphic-content)(opt)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :stretch-orientation (keyword)(opt)
 :horizontal, :vertical, :both
 :style (map)(opt)}
```

```
@usage
[popup-a :my-popup {...}]
```

---

### popup-b

```
@param (keyword) popup-id
@param (map) popup-props
{:close-by-cover? (boolean)(opt)
 :content (metamorphic-content)
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :style (map)(opt)}
```

```
@usage
[popup-b :my-popup {...}]
```

---

### sidebar-b

```
@param (keyword) sidebar-id
@param (map) sidebar-props
{:border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :content (metamorphic-content)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :position (keyword)(opt)
  :left, :right
  Default: :left
 :style (map)(opt)
 :viewport-min (px)(opt)
  Default: 720}
```

```
@usage
[sidebar-b :my-sidebar {...}]
```

---

### surface-a

```
@param (keyword) surface-id
@param (map) surface-props
{:content (metamorphic-content)
 :content-orientation (keyword)(opt)
  :horizontal, :vertical
  Default: :vertical
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :style (map)(opt)}
```

```
@usage
[surface-a :my-surface {...}]
```