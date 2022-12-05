
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dnd-kit.prototypes
    (:require [candy.api :refer [param return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sortable-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sortable-props
  ;
  ; @return (map)
  ; {:item-id-f (function)}
  [sortable-props]
  (merge {:item-id-f return}
         (param sortable-props)))
