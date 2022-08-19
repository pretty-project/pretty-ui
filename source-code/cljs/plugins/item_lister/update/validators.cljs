

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.validators
    (:require [mid-fruits.vector               :as vector]
              [plugins.item-lister.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]))



;; -- Delete items validators -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [mutation-name    (r update.subs/get-mutation-name db lister-id :delete-items!)
        deleted-item-ids (get server-response (symbol mutation-name))]
       (vector/nonempty? deleted-item-ids)))



;; -- Undo delete items validators --------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [mutation-name   (r update.subs/get-mutation-name db lister-id :undo-delete-items!)
        recovered-items (get server-response (symbol mutation-name))]
       (vector/nonempty? recovered-items)))



;; -- Duplicate items validators ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [mutation-name    (r update.subs/get-mutation-name db lister-id :duplicate-items!)
        duplicated-items (get server-response (symbol mutation-name))]
       (vector/nonempty? duplicated-items)))
