
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-combo-box.prototypes
    (:require [mid-fruits.candy                       :refer [param return]]
              [x.app-elements.input.helpers           :as input.helpers]
              [x.app-elements.multi-combo-box.helpers :as multi-combo-box.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {}
  [box-id box-props]
  ; XXX#5067
  ; A combo-box elemhez hasonloan a multi-combo-box elem eseményeinek is szükségesek
  ; a field-content-f, field-value-f, option-label-f és option-value-f függvények!
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
  (merge {;:on-select  [:elements.multi-combo-box/option-selected box-id box-props]
          :field-content-f return
          :field-value-f   return
          :option-label-f  return
          :option-value-f  return
          :options-path    (input.helpers/default-options-path box-id)
          :value-path      (input.helpers/default-value-path   box-id)}
         (param box-props)))

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {}
  [box-id box-props]
  ; TODO - A teljesség igénye nélkül ...
  ; XXX#5061
  ; XXX#5062
  (let [field-id    (multi-combo-box.helpers/box-id->field-id box-id)
        field-props (select-keys box-props [:emptiable?      :field-content-f  :field-value-f
                                            :initial-options :initial-value    :no-options-label
                                            :on-select       :option-component :option-label-f
                                            :option-value-f  :options          :options-path
                                            :required?       :validator])]
       (merge {:value-path (input.helpers/default-value-path field-id)}
              (param field-props))))

(defn field-events-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [box-id box-props field-id field-props]
  ; HACK#3031
  (merge {:on-blur  [:elements.multi-combo-box/field-blurred box-id box-props field-id field-props]
          :on-focus [:elements.multi-combo-box/field-focused box-id box-props field-id field-props]}
         (param field-props)))

(defn group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {}
  [box-id box-props]
  ; TODO - A teljesség igénye nélkül ...
  (select-keys box-props []))
