
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.breadcrumbs.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) anchor-props
  ;
  ; @return (map)
  ;  {}
  [breadcrumbs-props]
  (merge {}
         (param breadcrumbs-props)))
