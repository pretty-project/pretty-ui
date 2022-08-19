

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.passfield-handler.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn visibility-toggle-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ;  {:icon (keyword)
  ;    Default: :visibility
  ;   :on-click (metamorphic-event)
  ;    Default: [...]
  ;   :tooltip (keyword)
  ;    Default: :show-password!}
  [field-id toggle-props]
  (merge {:icon     :visibility
          :on-click [:elements/toggle-passphrase-visibility! field-id]
          :tooltip  :show-password!}
         (param toggle-props)))
