
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.icon.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :icon-family (keyword)
  ;  :size (keyword)}
  [icon-props]
  (merge {:color       :default
          :icon-family :material-icons-filled
          :size        :m}
         (param icon-props)))
