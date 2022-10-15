
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.validators
    (:require [mid-fruits.vector               :as vector]
              [plugins.item-lister.update.subs :as update.subs]
              [re-frame.api                    :refer [r]]))



;; -- Reorder items validators ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reorder-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [reordered-items (r update.subs/get-mutation-answer db lister-id :reorder-items! server-response)]
       (vector/nonempty? reordered-items)))



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
  (let [deleted-item-ids (r update.subs/get-mutation-answer db lister-id :delete-items! server-response)]
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
  (let [recovered-items (r update.subs/get-mutation-answer db lister-id :undo-delete-items! server-response)]
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
  (let [duplicated-items (r update.subs/get-mutation-answer db lister-id :duplicate-items! server-response)]
       (vector/nonempty? duplicated-items)))
