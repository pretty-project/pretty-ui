
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.multi-combo-box.prototypes
    (:require [mid-fruits.candy                 :refer [param return]]
              [elements.input.helpers           :as input.helpers]
              [elements.multi-combo-box.helpers :as multi-combo-box.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {:value-path (vector)}
  [box-id box-props]
  ; XXX#5061
  ; XXX#5062
  (let [field-id    (multi-combo-box.helpers/box-id->field-id box-id)
        field-props (dissoc box-props :helper :indent :label :value-path)]
       (merge {:value-path (input.helpers/default-value-path field-id)}
              (param field-props))))

(defn group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {:chip-label-f (function)
  ;   :deletable? (boolean)
  ;   :indent (map)}
  [box-id box-props]
  (let [group-props (dissoc box-props :helper :label :indent :placeholder)]
       (merge {:chip-label-f return
               :indent       {:bottom :xxs}}
              (param group-props)
              {:deletable? true})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {:field-value-f (function)
  ;   :on-blur (metamorphic-event)
  ;   :on-change (metamorphic-event)
  ;   :on-focus (metamorphic-event)
  ;   :option-label-f (function)
  ;   :option-value-f (function)
  ;   :options-path (vector)}
  [box-id box-props]
  ; XXX#5067
  ; A combo-box elemhez hasonloan a multi-combo-box elem eseményeinek is szükségesek
  ; a field-value-f, option-label-f és option-value-f függvények!
  ;
  ; XXX#5061
  ; A multi-combo-box elem value-path útvonala, ahova a kiválasztott elemek kerülnek.
  ; Ez az útvonal nem egyezik meg a multi-combo-box elemben megjelenő combo-box
  ; elem value-path útvonalával, ahova a combo-box elembe írt szöveg kerül.
  ; Ezért a :value-path tulajdonság nem is öröklődik a box-props térképből a field-props
  ; térképbe.
  ;
  ; XXX#5062
  ; A multi-combo-box elem options-path útvonala megegyezik az elemben megjelenő
  ; combo-box elem options-path útvonalával, ezért az :options-path tulajdonság
  ; öröklődik a box-props térképből a field-props térképbe.
  (merge {:field-value-f  return
          :option-label-f return
          :option-value-f return
          :options-path   (input.helpers/default-options-path box-id)
          :value-path     (input.helpers/default-value-path   box-id)}
         (param box-props)))

(defn box-events-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {}
  [box-id box-props]
  ; HACK#3031
  ; A box-events-prototype függvényben lévő eseményeknek szükségük van a box-props-prototype
  ; függvényben beállított tulajdonságokra!
  ; Azért van két lépésre felosztva a box-props-prototype függvény, hogy ha a prototípusban kerül
  ; beállításra egyes tulajdonságok alapértelmezett értéke, akkor a második függvényben (box-events-prototype)
  ; a box-props térkép már tartalmazni fogja a tulajdonság értékét.
  ; Pl. A regisztrált keypress események használják a box-props térkép options-path tulajdonságát.
  ;
  ; HACK#3031
  ; Meg kell oldani azt is, hogy a box-props térkép ne tartalmazza önmagát 3-4-5-... mélység mélyen!
  ;
  ; XXX#5055
  ; A combo-box elem a text-field elem on-blur és on-focus eseményeit használja
  ; a vezérléséhez szükséges billentyűlenyomás-figyelők regisztrálásához
  ; (pl. DOWN, UP, ENTER, ESC)
  (merge {:on-blur   [:elements.multi-combo-box/field-blurred box-id box-props]
          :on-change [:elements.multi-combo-box/field-changed box-id box-props]
          :on-focus  [:elements.multi-combo-box/field-focused box-id box-props]}
         (param box-props)))
