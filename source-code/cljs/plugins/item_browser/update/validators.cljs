
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.validators
    (:require [mid-fruits.string                :as string]
              [plugins.item-browser.update.subs :as update.subs]
              [x.app-core.api                   :refer [r]]
              [x.app-db.api                     :as db]))



;; -- Delete item validators --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [mutation-name (r update.subs/get-mutation-name db browser-id :delete-item!)
        document-id   (get server-response (symbol mutation-name))]
       (string/nonempty? document-id)))



;; -- Undo delete item validators ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [mutation-name (r update.subs/get-mutation-name db browser-id :undo-delete-item!)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))



;; -- Duplicate item validators -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [mutation-name (r update.subs/get-mutation-name db browser-id :duplicate-item!)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))



;; -- Update item validators --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ browser-id server-response]]
  (let [mutation-name (r update.subs/get-mutation-name db browser-id :update-item!)
        document      (get server-response (symbol mutation-name))]
       (db/document->document-namespaced? document)))
