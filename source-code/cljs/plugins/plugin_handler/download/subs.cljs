
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.download.subs
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [re-frame.api                         :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r download.subs/get-resolver-id db :my-plugin :get-items)
  ;  =>
  ;  :my-handler/get-items
  ;
  ; @return (keyword)
  [db [_ plugin-id action-key]]
  (let [handler-key (r transfer.subs/get-transfer-item db plugin-id :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key)))))

(defn get-resolver-answer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) action-key
  ; @param (map) server-response
  ;
  ; @example
  ;  (r download.subs/get-resolver-answer db :my-plugin :get-items {...})
  ;  =>
  ;  [{...} {...}]
  ;
  ; @return (*)
  [db [_ plugin-id action-key server-response]]
  (let [resolver-id (r get-resolver-id db plugin-id action-key)]
       (resolver-id server-response)))



;; -- Data-receiving subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (get-in db [:plugins :plugin-handler/meta-items plugin-id :data-received?]))
