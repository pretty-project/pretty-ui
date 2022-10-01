
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.update.subs
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [re-frame.api                         :refer [r]]))



;; -- Request subscriptions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r update.subs/get-mutation-name db :my-plugin :delete-items)
  ;  =>
  ;  :my-handler/delete-items!
  ;
  ; @return (keyword)
  [db [_ plugin-id action-key]]
  (let [handler-key (r transfer.subs/get-transfer-item db plugin-id :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key)))))
