
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.prototypes
    (:require [mid-fruits.candy             :refer [param return]]
              [x.app-elements.input.helpers :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ;  {:field-content-f (function)
  ;   :field-value-f (function)
  ;   :no-options-label (metamorphic-content)
  ;   :on-blur (metamorphic-event)
  ;   :on-change (metamorphic-event)
  ;   :on-focus (metamorphic-event)
  ;   :option-label-f (function)
  ;   :option-value-f (function)
  ;   :options-path (vector)}
  [box-id box-props]
  ; XXX#5067
  ; A field-content-f, field-value-f, option-label-f és option-value-f függvényeket szükséges a combo-box
  ; elem tulajdonságainak prototípusában is beállítani! Nem elég, hogy a text-field
  ; elem tulajdonságainak prototípusában be vannak állítva!
  ; Pl. A [:elements.combo-box/field-focused ...] esemény számára átadott box-props
  ;     térképben mindenképpen szerepelnie kell a :field-value-f tulajdonságnak, mivel
  ;     az esemény által regisztrált billentyűlenyomás-figyelő események között
  ;     szerepel az ESC billentyű lenyomására megtörténő [:elements.text-field/empty-field! ...]
  ;     esemény, ami az adatbázisba íráskor használja a field-value-f függvényt.
  (merge {:field-content-f  return
          :field-value-f    return
          :option-label-f   return
          :option-value-f   return
          :no-options-label :no-options
          :options-path     (input.helpers/default-options-path box-id)
          :value-path       (input.helpers/default-value-path   box-id)}
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
  ; Pl.: A regisztrált keypress események használják a box-props térkép options-path tulajdonságát.
  ;
  ; HACK#3031
  ; Meg kell oldani azt is, hogy a box-props térkép ne tartalmazza önmagát 3-4-5-... mélység mélyen!
  ;
  ; XXX#5055
  ; A combo-box elem a text-field elem on-blur és on-focus eseményeit használja
  ; a vezérléséhez szükséges billentyűlenyomás-figyelők regisztrálásához
  ; (pl. DOWN, UP, ENTER, ESC)
  (merge {:on-blur   [:elements.combo-box/field-blurred box-id box-props]
          :on-change [:elements.combo-box/field-changed box-id box-props]
          :on-focus  [:elements.combo-box/field-focused box-id box-props]}
         (param box-props)))