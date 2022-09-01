
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.prototypes
    (:require [mid-fruits.candy                     :refer [param]]
              [x.app-elements.color-selector.config :as color-selector.config]
              [x.app-elements.engine.api            :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:options (strings in vector)(opt)
  ;   :options-path (vector)(opt)}
  ;
  ; @return (map)
  ;  {:options (strings in vector)
  ;   :value-path (vector)}
  [db [_ selector-id {:keys [options options-path] :as selector-props}]]
  ; A color-selector elem a többi opciós elemtől (checkbox, radio-button, ...)
  ; eltérően valósítja meg az initial-options, options és options-path kapcsolatát.
  ; Végeredményben ugyanaz, tehát az options paraméter az elsődleges forrás, aminek
  ; hiánya esetén az options-path útvonalon tárolt érték a másodlagos forrás, ami
  ; az initial-options használatával írható az elem inicializálásakor.
  ; Mivel a color-selector elem inicializálása egy Re-Frame effekt meghívásakor
  ; történik ([:elements.color-selector/render-selector! ...]), ezért egyszerűen
  ; lehetséges a selector-props-prototype függvény számára a Re-Frame adatbázist
  ; átadni, ami miatt kisérleti jelleggel került megírásra ez az eltérő megvalósítás.
  ;
  ; A color-selector elemet bármikor át lehet alakítani, hogy a többi opciós elemhez
  ; hasonlóan valósítsa meg az initial-options, options és options-path kapcsolatát!
  (merge {:value-path (engine/default-value-path selector-id)}
         (param selector-props)
         (if options-path {:options (get-in db options-path)}
                          {:options (or options color-selector.config/DEFAULT-OPTIONS)})))

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ;  {:on-click (metamorphic-event)}
  [selector-id selector-props]
  (assoc selector-props :on-click [:elements.color-selector/render-selector! selector-id selector-props]))
