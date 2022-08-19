

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



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
  (merge {:value-path (engine/default-value-path selector-id)}
         (param selector-props)
         (if options-path {:options (get-in db options-path)}
                          {:options (or options color-selector.config/DEFAULT-OPTIONS)})))
