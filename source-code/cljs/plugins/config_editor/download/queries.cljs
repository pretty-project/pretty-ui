
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.download.queries
    (:require [plugins.config-editor.core.subs     :as core.subs]
              [plugins.config-editor.download.subs :as download.subs]
              [x.app-core.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-config-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r core.subs/use-query-params db editor-id {}))

(defn get-request-config-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  (let [resolver-id    (r download.subs/get-resolver-id     db editor-id :get-config)
        resolver-props (r get-request-config-resolver-props db editor-id)
        query         `(~resolver-id ~resolver-props)]
       (r core.subs/use-query-prop db editor-id query)))
