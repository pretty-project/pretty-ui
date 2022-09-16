
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.helpers
    (:require [mid-fruits.hiccup :as hiccup]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  ;  {}
  [surface-id]
  {:class :x-app-surface--element
   :id    (hiccup/value surface-id)
   :key   (hiccup/value surface-id)})
