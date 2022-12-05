
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.expandable.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) expandable-props
  ; {:icon (keyword)}
  ;
  ; @return (map)
  ; {:expanded? (boolean)
  ;  :icon-family (keyword)}
  [{:keys [icon] :as expandable-props}]
  (merge {:expanded? false}
         (if icon {:icon-family :material-icons-filled})
         (param expandable-props)))
