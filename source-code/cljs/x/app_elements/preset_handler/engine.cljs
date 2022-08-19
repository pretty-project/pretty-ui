
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.preset-handler.engine
    (:require [mid-fruits.candy :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-preset
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) presets
  ; @param (map) element-props
  ;  {:on-click (metamorphic-event)(opt)
  ;   :preset (keyword)(opt)}
  ;
  ; @usage
  ;  (preset-handler.engine/apply-preset {:preset-name {...}}
  ;                                       {:preset :preset-name ...})
  ;
  ; @return (map)
  [presets {:keys [preset] :as element-props}]
  (if preset (let [preset-props (get presets preset)]
                  (merge preset-props element-props))
             (return element-props)))
