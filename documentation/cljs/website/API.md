
# website.api ClojureScript namespace

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > website.api

### Index

- [contacts](#contacts)

- [copyright-label](#copyright-label)

- [created-by-link](#created-by-link)

- [credits](#credits)

- [follow-us](#follow-us)

- [language-selector](#language-selector)

- [menu](#menu)

- [mt-logo](#mt-logo)

- [navbar](#navbar)

- [navbar-menu](#navbar-menu)

- [scroll-icon](#scroll-icon)

- [scroll-sensor](#scroll-sensor)

- [scroll-to-top](#scroll-to-top)

- [sidebar](#sidebar)

### contacts

```
@param (keyword)(opt) component-id
@param (map) component-props
{}
```

```
@usage
[contacts {...}]
```

```
@usage
[contacts :my-contacts {...}]
```

---

### copyright-label

```
@param (keyword)(opt) component-id
@param (map) component-props
{:color (keyword or string)(opt)
  Default: :inherit}
```

```
@usage
[copyright-label {...}]
```

```
@usage
[copyright-label :my-copyright-label {...}]
```

---

### created-by-link

```
@param (keyword)(opt) component-id
@param (map) component-props
{:color (keyword or string)(opt)
  Default: :inherit
 :style (map)(opt)}
```

```
@usage
[created-by-link {...}]
```

```
@usage
[created-by-link :my-created-by-link {...}]
```

---

### credits

```
@param (keyword)(opt) component-id
@param (map) component-props
{:color (keyword or string)(opt)
  Default: :inherit
 :style (map)(opt)
 :theme (keyword)(opt)
  :light, :dark
  Default: :light}
```

```
@usage
[credits {...}]
```

```
@usage
[credits :my-credits {...}]
```

---

### follow-us

```
@param (keyword)(opt) component-id
@param (map) component-props
{:color (string)(opt)
  Default: "white"
 :links (map)
  {:facebook (string)
   :instagram (string)
   :linkedin (string)}
 :style (map)(opt)}
```

```
@usage
[follow-us {...}]
```

```
@usage
[follow-us :my-follow-us {...}]
```

---

### language-selector

```
@param (keyword)(opt) component-id
@param (map) component-props
{:languages (keywords in vector)
 :style (map)(opt)}
```

```
@usage
[language-selector {...}]
```

```
@usage
[language-selector :my-language-selector {...}]
```

```
@usage
[language-selector :my-language-selector
                   {:languages [:en :hu]}]
```

---

### menu

```
@param (keyword)(opt) component-id
@param (map) component-props
{:class (keyword or keywords in vector)(opt)
 :menu-link (namespaced map)
  {:menu/id (string)}
 :style (map)(opt)}
```

```
@usage
[menu {...}]
```

```
@usage
[menu :my-menu {...}]
```

---

### mt-logo

```
@param (keyword)(opt) component-id
@param (map) component-props
{:theme (keyword)(opt)
  :light, :dark
  Default: :light}
```

```
@usage
[mt-logo {...}]
```

```
@usage
[mt-logo :my-mt-logo {...}]
```

---

### navbar

```
@param (keyword)(opt) component-id
@param (map) component-props
{:class (keyword or keywords in vector)(opt)
 :logo (metamorphic-content)(opt)
 :menu-link (namespaced map)
  {:menu/id (string)}
 :on-menu (metamorphic-event)(opt)
  Click event on the hamburger menu button
 :style (map)(opt)
 :threshold (px)(opt)
  Threshold of the desktop view}
```

```
@usage
[navbar {...}]
```

```
@usage
[navbar :my-navbar {...}]
```

---

### navbar-menu

```
@description
This component implements the dropdown-menu element and in case of the viewport
width is smaller than the given threshold, it displays the menu items on
a sidebar and replaces the menu bar with a single menu button.
```

```
@param (keyword)(opt) menu-id
@param (map) menu-props
{
 :threshold (px)(opt)}
```

```
@usage
[navbar-menu {...}]
```

```
@usage
[navbar-menu :my-navbar-menu {...}]
```

---

### scroll-icon

```
@param (keyword)(opt) component-id
@param (map) component-props
{:color (string)(opt)
  Default: "white"
 :style (map)(opt)}
```

```
@usage
[scroll-icon {...}]
```

```
@usage
[scroll-icon :my-scroll-icon {...}]
```

---

### scroll-sensor

```
@param (keyword)(opt) sensor-id
@param (map) sensor-props
{:callback-f (function)
 :style (map)(opt)}
```

```
@usage
[scroll-sensor {...}]
```

```
@usage
[scroll-sensor :my-sensor {...}]
```

```
@usage
(defn my-scroll-f [intersecting?] ...)
[scroll-sensor {:callback-f my-scroll-f}]
```

---

### scroll-to-top

```
@param (keyword)(opt) component-id
@param (map) component-props
{:color (string)(opt)
  Default: "white"
 :style (map)(opt)}
```

```
@usage
[scroll-to-top {...}]
```

```
@usage
[scroll-to-top :my-scroll-to-top {...}]
```

---

### sidebar

```
@param (keyword)(opt) sidebar-id
@param (map) sidebar-props
{:border-color (keyword or string)(opt)
  :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
 :border-position (keyword)(opt)
  :left, :right
 :border-width (keyword)(opt)
  :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
 :class (keyword or keywords in vector)(opt)
 :content (metamorphic-content)
 :cover-color (keyword or string)(opt)
  Default: :black
 :fill-color (keyword or string)(opt)
  Default: :white
 :indent (map)(opt)
 :outdent (map)(opt)
 :position (keyword)(opt)
  :left, :right
  Default: :left
 :style (map)(opt)}
```

```
@usage
[sidebar {...}]
```

```
@usage
[sidebar :my-sidebar {...}]
```