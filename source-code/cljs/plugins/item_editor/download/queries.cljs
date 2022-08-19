

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.queries
    (:require [plugins.item-editor.body.subs     :as body.subs]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [x.app-core.api                    :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-suggestions-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:editor-id (keyword)
  ;   :suggestion-keys (keywords in vector)}
  [db [_ editor-id]]
  (let [suggestion-keys (r body.subs/get-body-prop db editor-id :suggestion-keys)]
       (merge (r core.subs/get-query-params db editor-id)
              {:editor-id       editor-id
               :suggestion-keys suggestion-keys})))

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ editor-id]]
  (let [current-item-id (r core.subs/get-current-item-id db editor-id)]
       (merge (r core.subs/get-query-params db editor-id)
              {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  [(if (r core.subs/download-item? db editor-id)
       ; If download item ...
       (let [resolver-id    (r download.subs/get-resolver-id   db editor-id :get-item)
             resolver-props (r get-request-item-resolver-props db editor-id)]
           `(~resolver-id ~resolver-props)))
   (if (r core.subs/download-suggestions? db editor-id)
       ; If download suggestions ...
       (let [resolver-id :item-editor/get-item-suggestions
             resolver-props (r get-request-suggestions-resolver-props db editor-id)]
           `(~resolver-id ~resolver-props)))])
