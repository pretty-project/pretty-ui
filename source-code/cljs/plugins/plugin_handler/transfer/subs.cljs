
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.transfer.subs
    (:require [plugins.plugin-handler.body.subs :as body.subs]
              [re-frame.api                     :refer [r]]))



;; -- Transfer item subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ plugin-id item-key]]
  (let [transfer-id (r body.subs/get-body-prop db plugin-id :transfer-id)]
       (get-in db [:plugins :plugin-handler/transfer-items plugin-id item-key])))
