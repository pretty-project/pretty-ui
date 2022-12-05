
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.horizontal-separator.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:height (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-height (keyword)
  ;  :style (map)}
  [separator-id {:keys [height style] :as separator-props}]
  (merge (element.helpers/element-default-attributes separator-id separator-props)
         (element.helpers/element-indent-attributes  separator-id separator-props)
         {:data-height height
          :style       style}))
