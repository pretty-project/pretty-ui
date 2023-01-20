
### Basic rules of creating elements

- Every element has its entry component named as `element` in the element's
  `views.cljs` namespace.

- The functional documentation of the element has to be placed in this entry
  component's header. Every other component is ignored from documentation readers
  by the `@ignore` tag in their headers.

- Every element has to takes two arguments. An element ID (optional) and an element
  properties map (required).
  The `random.api/generate-keyword` function provides the element ID in case of it
  is not passed.

- Every element got its default properties by a prototype function placed in the
  element's `prototypes.cljs` namespace.

- The second component (used up in the entry component) is named as the element itself.

```
(ns elements.button.views
    (:require [elements.button.attributes :as button.attributes]  
              [elements.button.prototypes :as button.prototypes]  
              [random.api                 :as random]))

(defn- button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div ...])

(defn element
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:on-click (metamorphic-event)}
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (button.prototypes/button-props-prototype button-props)]
        [button button-id button-props])))
```

### HICCUP attribute maps in elements
 

### ...

###### BUG#4044

Ha egy listában a listaelemek toggle elemet tartalmaznak és ...
... a toggle elem nem kap egyedi azonosítót, különben ugyanaz az azonosító
    ismétlődne az összes listaelem toggle elemében,
... a toggle elem {:hover-color ...} tulajdonsággal rendelkezik,
... az elemek React kulcsként megkapják az azonosítójukat,
... az egyes listaelemekre kattintva olyan változás történik (pl. kijelölés),
    ami miatt az adott listaelem paraméterezése megváltozik,
akkor az egyes listaelemekre kattintva ...
... a megváltozó paraméterek miatt a listaelem újrarenderelődik,
... a listaelem toggle eleme is újrarenderelődik, ami miatt új azonosítót kap,
... a toggle elem az új azonosítója miatt új React kulcsot kap,
... a toggle elem az új React kulcs beállításának pillanatában másik React-elemmé
    változik és a váltás közben Ca. 15ms ideig nem látszódik a {:hover-color ...}
    tulajdonság színe (rövid villanásnak tűnik)

### ...

###### XXX#4004

Az x4.7.6 verzióig egyetlen esetben sem volt rá szükség, hogy egy elem rendelkezzen
DOM azonosítóval.

+ Talán könnyebb a böngészőnek, ha kevesebb az azonosítóval rendelkező elem ...
