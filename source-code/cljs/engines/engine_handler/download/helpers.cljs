
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.download.helpers
    (:require [keyword.api :as keyword]
              [map.api     :as map]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn received-item->item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced map) received-item
  ;
  ; @return (string)
  [received-item]
  (let [namespace (map/get-namespace received-item)]
       ((keyword/add-namespace namespace :id) received-item)))
