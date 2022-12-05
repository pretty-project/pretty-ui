
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.thumbnail.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  ; {:background-size (keyword)
  ;  :height (keyword)
  ;  :icon (keyword)
  ;  :icon-family (keyword)
  ;  :width (keyword)}
  [thumbnail-props]
  (merge {:background-size :contain
          :height          :s
          :width           :s
          :icon            :image
          :icon-family     :material-icons-filled}
         (param thumbnail-props)))
