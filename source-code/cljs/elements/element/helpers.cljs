
(ns elements.element.helpers
    (:require [candy.api  :refer [param return]]
              [css.api    :as css]
              [hiccup.api :as hiccup]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-preset
  ; @param (map) presets
  ; @param (map) element-props
  ; {:preset (keyword)(opt)}
  ;
  ; @usage
  ; (apply-preset {:my-preset {...}}
  ;               {:preset :my-preset ...})
  ;
  ; @return (map)
  [presets {:keys [preset] :as element-props}]
  (if preset (let [preset-props (get presets preset)]
                  (merge preset-props element-props))
             (return element-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-color
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-attributes
  ; {:style (map)(opt)}
  ; @param (keyword) color-key
  ; @param (keyword) color-data-key
  ; @param (keyword or string) color-value
  ;
  ; @example
  ; (apply-color {...} :color :data-color :muted)
  ; =>
  ; {:data-color :muted}
  ;
  ; @example
  ; (apply-color {...} :color :data-color "#fff")
  ; =>
  ; {:data-color :var :style {"--color" "fff"}}
  ;
  ; @example
  ; (apply-color {:style {:padding "12px"}} :color :data-color "#fff")
  ; =>
  ; {:data-color :var :style {"--color" "fff" :padding "12px"}}
  ;
  ; @return (map)
  ; {:style (map)}
  [element-attributes color-key color-data-key color-value]
  (cond (keyword? color-value) (-> element-attributes (assoc-in [color-data-key]                 color-value))
        (string?  color-value) (-> element-attributes (assoc-in [:style (css/var-key color-key)] color-value)
                                                      (assoc-in [color-data-key]                 :var))
        :return element-attributes))

(defn apply-dimension
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-attributes
  ; @param (keyword) dimension-key
  ; @param (keyword) dimension-data-key
  ; @param (keyword, px or string) dimension-value
  ;
  ; @example
  ; (apply-dimension {...} :width :data-width 42)
  ; =>
  ; {:style {:width "42px"}}
  ;
  ; @example
  ; (apply-dimension {...} :width :data-width "42%")
  ; =>
  ; {:style {:width "42%"}}
  ;
  ; @example
  ; (apply-dimension {...} :width :data-width :s)
  ; =>
  ; {:data-min-width :s}
  ;
  ; @return (map)
  [element-attributes dimension-key dimension-data-key dimension-value]
  (cond (keyword? dimension-value) (assoc    element-attributes dimension-data-key dimension-value)
        (integer? dimension-value) (assoc-in element-attributes [:style dimension-key] (css/px dimension-value))
        (string?  dimension-value) (assoc-in element-attributes [:style dimension-key] (param  dimension-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-default-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-disabled (boolean)
  ;  :id (string)
  ;  :key (string)
  ;  :style (map)}
  [element-id {:keys [class disabled? style]}]
  ; BUG#4044
  ; Ha egy listában a listaelemek toggle elemet tartalmaznak és ...
  ; ... a toggle elem nem kap egyedi azonosítót, mert ugyanaz az azonosító ismétlődne
  ;     az összes listaelem toggle elemében,
  ; ... a toggle elem {:hover-color ...} tulajdonsággal rendelkezik,
  ; ... az element-default-attributes függvény React kulcsként alkalmazza az elemek
  ;     azonosítóját,
  ; ... az egyes listaelemekre kattintva olyan változás történik (pl. kijelölés),
  ;     ami miatt az adott listaelem paraméterezése megváltozik,
  ; akkor az egyes listaelemekre kattintva ...
  ; ... a megváltozó paraméterek miatt a listaelem újrarenderelődik,
  ; ... a listaelem toggle eleme is újrarenderelődik, ami miatt új azonosítót kap,
  ; ... a toggle elem az új azonosítója miatt új React kulcsot kap,
  ; ... a toggle elem az új React kulcs beállításának pillanatában másik React-elemmé
  ;     változik és a váltás közben Ca. 15ms ideig nem látszódik a {:hover-color ...}
  ;     tulajdonság színe (rövid villanásnak tűnik)
  ;
  ; XXX#4004
  ; Az x4.7.6 verzióig egyetlen esetben sem volt rá szükség, hogy egy element rendelkezzen
  ; DOM azonosítóval.
  ; + Talán könnyebb a böngészőnek, ha kevesebb az azonosítóval rendelkező elem ...
  ;
  ; XXX#4005
  ; A {:hover-color ...} tulajdonság használatához, minden esetben szükséges a {:data-disabled ...}
  ; attribútumot alkalmazni!
  {:class         (hiccup/join-class :e-element class)
   :data-disabled (boolean disabled?)})
  ;:id            (hiccup/value      element-id)
  ;:key           (hiccup/value      element-id)

(defn element-indent-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:indent (map)(opt)
  ;   {}}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [indent]}]
  (cond-> {} (:bottom     indent) (assoc :data-indent-bottom     (:bottom     indent))
             (:left       indent) (assoc :data-indent-left       (:left       indent))
             (:right      indent) (assoc :data-indent-right      (:right      indent))
             (:top        indent) (assoc :data-indent-top        (:top        indent))
             (:horizontal indent) (assoc :data-indent-horizontal (:horizontal indent))
             (:vertical   indent) (assoc :data-indent-vertical   (:vertical   indent))
             (:all        indent) (assoc :data-indent-all        (:all        indent))))

(defn element-outdent-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:outdent (map)(opt)
  ;   {}}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [outdent]}]
  (cond-> {} (:bottom     outdent) (assoc :data-outdent-bottom     (:bottom     outdent))
             (:left       outdent) (assoc :data-outdent-left       (:left       outdent))
             (:right      outdent) (assoc :data-outdent-right      (:right      outdent))
             (:top        outdent) (assoc :data-outdent-top        (:top        outdent))
             (:horizontal outdent) (assoc :data-outdent-horizontal (:horizontal outdent))
             (:vertical   outdent) (assoc :data-outdent-vertical   (:vertical   outdent))
             (:all        outdent) (assoc :data-outdent-all        (:all        outdent))))
