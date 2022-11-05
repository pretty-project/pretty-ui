
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.queries
    (:require [engines.item-handler.core.subs   :as core.subs]
              [engines.item-handler.update.subs :as update.subs]
              [re-frame.api                     :refer [r]]))



;; -- Save item queries -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ handler-id]]
  (let [exported-item (r core.subs/export-current-item db handler-id)]
       (r core.subs/use-query-params db handler-id {:item exported-item})))

(defn get-save-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [new-item?      (r core.subs/new-item?           db handler-id)
        mutation-name  (r update.subs/get-mutation-name db handler-id (if new-item? :add-item! :save-item!))
        mutation-props (r get-save-item-mutation-props  db handler-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))
