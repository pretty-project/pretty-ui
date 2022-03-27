
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.validators
    (:require [mid-fruits.vector               :as vector]
              [plugins.item-lister.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [mutation-name    (r update.subs/get-mutation-name db lister-id :delete-items)
        deleted-item-ids (get server-response (symbol mutation-name))]
       (vector/nonempty? deleted-item-ids)))

(defn undo-delete-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [mutation-name   (r update.subs/get-mutation-name db lister-id :undo-delete-items)
        recovered-items (get server-response (symbol mutation-name))]
       (vector/nonempty? recovered-items)))

(defn duplicate-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [mutation-name    (r update.subs/get-mutation-name db lister-id :duplicate-items)
        duplicated-items (get server-response (symbol mutation-name))]
       (vector/nonempty? duplicated-items)))

(defn undo-duplicate-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [mutation-name    (r update.subs/get-mutation-name db lister-id :undo-duplicate-items)
        deleted-item-ids (get server-response (symbol mutation-name))]
       (vector/nonempty? deleted-item-ids)))
