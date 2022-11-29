
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-sorter.prototypes
    (:require [candy.api                   :refer [param]]
              [engines.text-editor.helpers :as helpers]
              [re-frame.api                :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sorter-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sorter-id
  ; @param (map) sorter-props
  ;
  ; @return (map)
  [sorter-id sorter-props]
  (merge {:value-path (helpers/default-value-path sorter-id)}
         (param sorter-props)))

(defn dnd-kit-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sorter-id
  ; @param (map) sorter-props
  ;
  ; @return (map)
  [sorter-id sorter-props]
  {:on-order-changed helpers/on-order-changed-f})
