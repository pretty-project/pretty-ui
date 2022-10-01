
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.queries
    (:require [plugins.item-editor.core.subs   :as core.subs]
              [plugins.item-editor.update.subs :as update.subs]
              [re-frame.api                    :refer [r]]))



;; -- Save item queries -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ editor-id]]
  (let [exported-item (r core.subs/export-current-item db editor-id)]
       (r core.subs/use-query-params db editor-id {:item exported-item})))

(defn get-save-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  (let [new-item?      (r core.subs/new-item?           db editor-id)
        mutation-name  (r update.subs/get-mutation-name db editor-id (if new-item? :add-item! :save-item!))
        mutation-props (r get-save-item-mutation-props  db editor-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))
