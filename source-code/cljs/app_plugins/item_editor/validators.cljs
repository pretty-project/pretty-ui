
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.validators
    (:require [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]
              [app-plugins.item-editor.engine :as engine]
              [app-plugins.item-editor.subs   :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id (engine/resolver-id extension-id item-namespace :get)
        document    (get server-response resolver-id)
        suggestions (get server-response :item-editor/get-item-suggestions)]
       (and (or (map? suggestions)
                (not (r subs/download-suggestions? db extension-id item-namespace)))
            (or (db/document->document-namespaced? document)
                (not (r subs/download-item?        db extension-id item-namespace))))))

(defn save-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]]
  (let [mutation-name (engine/mutation-name extension-id item-namespace :save)
        document      (get server-response (symbol mutation-name))]))
      ;(db/document->document-namespaced? document)))

(defn delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]])

(defn undo-delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]])

(defn duplicate-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]])
