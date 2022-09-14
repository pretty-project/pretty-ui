
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.update.queries
    (:require [plugins.config-editor.core.subs   :as core.subs]
              [plugins.config-editor.update.subs :as update.subs]
              [x.app-core.api                    :refer [r]]))



;; -- Save item queries -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-config-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:config (map)}
  [db [_ editor-id]]
  (let [current-config (r core.subs/get-current-config db editor-id)]
       (r core.subs/use-query-params db editor-id {:config current-config})))

(defn get-save-config-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db editor-id :save-config!)
        mutation-props (r get-save-config-mutation-props db editor-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))
