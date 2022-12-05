
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.vertical-separator.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:width (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-width (keyword)
  ;  :style (map)}
  [separator-id {:keys [style width] :as separator-props}]
  (merge (element.helpers/element-default-attributes separator-id separator-props)
         (element.helpers/element-indent-attributes  separator-id separator-props)
         {:data-width width
          :style      style}))
