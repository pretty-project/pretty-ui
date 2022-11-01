
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.download.queries
    (:require [engines.item-editor.body.subs     :as body.subs]
              [engines.item-editor.core.subs     :as core.subs]
              [engines.item-editor.download.subs :as download.subs]
              [re-frame.api                      :refer [r]]))



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
       (r core.subs/use-query-params db editor-id {:editor-id       editor-id
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
       (r core.subs/use-query-params db editor-id {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  (let [query [(if (r core.subs/download-item? db editor-id)
                   ; If download item ...
                   (let [resolver-id    (r download.subs/get-resolver-id   db editor-id :get-item)
                         resolver-props (r get-request-item-resolver-props db editor-id)]
                       `(~resolver-id ~resolver-props)))
               (if (r core.subs/download-suggestions? db editor-id)
                   ; If download suggestions ...
                   (let [resolver-id    :item-editor/get-item-suggestions
                         resolver-props (r get-request-suggestions-resolver-props db editor-id)]
                       `(~resolver-id ~resolver-props)))]]
       (r core.subs/use-query-prop db editor-id query)))
