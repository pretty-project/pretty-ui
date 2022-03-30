
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.validators
    (:require [mid-fruits.string               :as string]
              [plugins.item-editor.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]
              [x.app-db.api                    :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [mutation-name (r update.subs/get-mutation-name db editor-id :save-item!)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))

(defn delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [mutation-name (r update.subs/get-mutation-name db editor-id :delete-item!)
        document-id   (get server-response (symbol mutation-name))]
       (string/nonempty? document-id)))

(defn undo-delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [mutation-name (r update.subs/get-mutation-name db editor-id :undo-delete-item!)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))

(defn duplicate-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ editor-id server-response]]
  (let [mutation-name (r update.subs/get-mutation-name db editor-id :duplicate-item!)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))
