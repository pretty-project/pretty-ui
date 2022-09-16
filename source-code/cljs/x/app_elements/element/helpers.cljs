
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element.helpers
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.css    :as css]
              [mid-fruits.hiccup :as hiccup]
              [x.app-core.api    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-preset
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) presets
  ; @param (map) element-props
  ;  {:preset (keyword)(opt)}
  ;
  ; @usage
  ;  (element.helpers/apply-preset {:preset-name {...}}
  ;                                {:preset :preset-name ...})
  ;
  ; @return (map)
  [presets {:keys [preset] :as element-props}]
  (if preset (let [preset-props (get presets preset)]
                  (merge preset-props element-props))
             (return element-props)))

(defn apply-color
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ; @param (keyword) color-key
  ; @param (keyword) color-data-key
  ; @param (keyword or string) color-value
  ;
  ; @example
  ;  (element.helpers/apply-color {...} :color :data-color :muted)
  ;  =>
  ;  {:data-color :muted}
  ;
  ; @example
  ;  (element.helpers/apply-color {...} :color :data-color "#fff")
  ;  =>
  ;  {:style {:color "fff"}}
  ;
  ; @return (map)
  [element-props color-key color-data-key color-value]
  (cond (keyword? color-value) (assoc    element-props color-data-key     color-value)
        (string?  color-value) (assoc-in element-props [:style color-key] color-value)))

(defn apply-dimension
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ; @param (keyword) dimension-key
  ; @param (keyword) dimension-data-key
  ; @param (keyword, px or string) dimension-value
  ;
  ; @example
  ;  (element.helpers/apply-dimension {...} :width :data-width 128)
  ;  =>
  ;  {:style {:width "128px"}}
  ;
  ; @example
  ;  (element.helpers/apply-dimension {...} :width :data-width "128px")
  ;  =>
  ;  {:style {:width "128px"}}
  ;
  ; @example
  ;  (apply-dimension {...} :width :data-width :s)
  ;  =>
  ;  {:data-min-width :s}
  ;
  ; @return (map)
  [element-props dimension-key dimension-data-key dimension-value]
  (cond (keyword? dimension-value) (assoc    element-props dimension-data-key dimension-value)
        (integer? dimension-value) (assoc-in element-props [:style dimension-key] (css/px dimension-value))
        (string?  dimension-value) (assoc-in element-props [:style dimension-key] (param  dimension-value))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-default-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :data-disabled (boolean)
  ;   :id (string)
  ;   :key (string)
  ;   :style (map)}
  [element-id {:keys [class disabled? style]}]
  ; - BUG#4044
  ;   Ha egy listában a listaelemek toggle elemet tartalmaznak és ...
  ;   ... a toggle elem nem kap egyedi azonosítót, mert ugyanaz az azonosító ismétlődne
  ;       az összes listaelem toggle elemében,
  ;   ... a toggle elem {:hover-color ...} tulajdonsággal rendelkezik,
  ;   ... az element-default-attributes függvény React kulcsként alkalmazza az elemek
  ;       azonosítóját,
  ;   ... az egyes listaelemekre kattintva olyan változás történik (pl. kijelölés),
  ;       ami miatt az adott listaelem paraméterezése megváltozik,
  ;   akkor az egyes listaelemekre kattintva ...
  ;   ... a megváltozó paraméterek miatt a listaelem újrarenderelődik,
  ;   ... a listaelem toggle eleme is újrarenderelődik, ami miatt új azonosítót kap,
  ;   ... a toggle elem az új azonosítója miatt úja React kulcsot kap,
  ;   ... a toggle elem az új React kulcs beállításának pillanatában másik React-elemmé
  ;       változik és a váltás közben Ca. 15ms ideig nem látszódik a {:hover-color ...}
  ;       tulajdonság színe (rövid villanásnak tűnik)
  ;
  ; - XXX#4005
  ;   A {:hover-color ...} tulajdonság használatához, minden esetben szükséges a {:data-disabled ...}
  ;   attribútumot alkalmazni!
  {:class         (css/join-class :x-element class)
   :data-disabled (boolean     disabled?)
   :id            (hiccup/value element-id)
  ;:key           (hiccup/value element-id)
   :style         style})

(defn element-indent-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:indent (map)(opt)
  ;    {}}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [indent]}]
  (cond-> {} (:bottom     indent) (assoc :data-indent-bottom     (:bottom     indent))
             (:left       indent) (assoc :data-indent-left       (:left       indent))
             (:right      indent) (assoc :data-indent-right      (:right      indent))
             (:top        indent) (assoc :data-indent-top        (:top        indent))
             (:horizontal indent) (assoc :data-indent-horizontal (:horizontal indent))
             (:vertical   indent) (assoc :data-indent-vertical   (:vertical   indent))
             (:all        indent) (assoc :data-indent-all        (:all        indent))))
