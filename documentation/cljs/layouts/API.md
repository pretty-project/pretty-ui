
# layouts.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > layouts.api

### Index

- [box-popup](#box-popup)

- [plain-popup](#plain-popup)

- [plain-surface](#plain-surface)

- [sidebar](#sidebar)

- [struct-popup](#struct-popup)

### box-popup

```
@param (keyword)(opt) popup-id
@param (map) popup-props
{:border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :content (metamorphic-content)
 :cover-color (keyword or string)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :indent (map)(opt)
 :lock-scroll? (boolean)(opt)
 :max-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :max-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-cover (metamorphic-event)(opt)
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :outdent (map)(opt)
 :stretch-orientation (keyword)(opt)
  :both, :horizontal, :vertical
 :style (map)(opt)}
```

```
@usage
[box-popup {...}]
```

```
@usage
[box-popup :my-box-popup {...}]
```

---

### plain-popup

```
@param (keyword)(opt) popup-id
@param (map) popup-props
{:content (metamorphic-content)(opt)
 :cover-color (keyword or string)(opt)
 :lock-scroll? (boolean)(opt)
 :on-cover (metamorphic-event)(opt)
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :style (map)(opt)}
```

```
@usage
[plain-popup {...}]
```

```
@usage
[plain-popup :my-plain-popup {...}]
```

---

### plain-surface

```
@param (keyword)(opt) surface-id
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
[plain-surface {...}]
```

```
@usage
[plain-surface :my-plain-surface {...}]
```

---

### sidebar

```
@param (keyword)(opt) sidebar-id
@param (map) sidebar-props
{:border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :content (metamorphic-content)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :indent (map)(opt)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :position (keyword)(opt)
  :left, :right
  Default: :left
 :style (map)(opt)
 :threshold (px)(opt)
  Default: 720}
```

```
@usage
[sidebar {...}]
```

```
@usage
[sidebar :my-sidebar {...}]
```

---

### struct-popup

```
@param (keyword)(opt) popup-id
@param (map) popup-props
{:body (metamorphic-content)
 :border-color (keyword)(opt)
  :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
 :border-radius (map)(opt)
  {:tl (keyword)(opt)
   :tr (keyword)(opt)
   :br (keyword)(opt)
   :bl (keyword)(opt)
   :all (keyword)(opt)
    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  Default: :xxs
 :cover-color (keyword or string)(opt)
 :fill-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  Default: :default
 :footer (metamorphic-content)(opt)
 :header (metamorphic-content)(opt)
 :indent (map)(opt)
 :lock-scroll? (boolean)(opt)
 :max-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :max-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-height (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :on-cover (metamorphic-event)(opt)
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :outdent (map)(opt)
 :stretch-orientation (keyword)(opt)
  :both, :horizontal, :vertical
 :style (map)(opt)}
```

```
@usage
[struct-popup {...}]
```

```
@usage
[struct-popup :my-struct-popup {...}]
```