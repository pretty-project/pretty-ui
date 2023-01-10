
# layouts.popup-a.api ClojureScript namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > layouts.popup-a.api

### Index

- [layout](#layout)

### layout

```
@param (keyword) popup-id
@param (map) layout-props
{:body (metamorphic-content)
 :close-by-cover? (boolean)(opt)
  Default: true
 :footer (metamorphic-content)(opt)
 :header (metamorphic-content)(opt)
 :min-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl
  Default: :none
 :on-mount (metamorphic-event)(opt)
 :on-unmount (metamorphic-event)(opt)
 :stretch-orientation (keyword)(opt)
 :horizontal, :vertical, :both, :none,
  Default: :none
 :style (map)(opt)}
```

```
@usage
[layout :my-popup {...}]
```