
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.update.queries
    (:require [engines.file-editor.core.subs   :as core.subs]
              [engines.file-editor.update.subs :as update.subs]
              [re-frame.api                    :refer [r]]))



;; -- Save item queries -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-content-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:content (map)}
  [db [_ editor-id]]
  (let [current-content (r core.subs/get-current-content db editor-id)]
       (r core.subs/use-query-params db editor-id {:content current-content})))

(defn get-save-content-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  (let [mutation-name  (r update.subs/get-mutation-name   db editor-id :save-content!)
        mutation-props (r get-save-content-mutation-props db editor-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))
