
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.download.queries
    (:require [engines.file-editor.core.subs     :as core.subs]
              [engines.file-editor.download.subs :as download.subs]
              [re-frame.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-content-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r core.subs/use-query-params db editor-id {}))

(defn get-request-content-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  (let [resolver-id    (r download.subs/get-resolver-id      db editor-id :get-content)
        resolver-props (r get-request-content-resolver-props db editor-id)
        query          [`(~resolver-id ~resolver-props)]]
       (r core.subs/use-query-prop db editor-id query)))
