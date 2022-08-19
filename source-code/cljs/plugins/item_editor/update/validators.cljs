

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.validators
    (:require [plugins.item-editor.core.subs   :as core.subs]
              [plugins.item-editor.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]
              [x.app-db.api                    :as db]))



;; -- Save item validators ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [new-item?     (r core.subs/new-item? db editor-id)
        mutation-name (r update.subs/get-mutation-name db editor-id (if new-item? :add-item! :save-item!))
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))
