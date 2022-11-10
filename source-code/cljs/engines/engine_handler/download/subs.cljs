
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.download.subs
    (:require [engines.engine-handler.transfer.subs :as transfer.subs]
              [re-frame.api                         :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r get-resolver-id db :my-engine :get-items)
  ;  =>
  ;  :my-handler/get-items
  ;
  ; @return (keyword)
  [db [_ engine-id action-key]]
  (let [handler-key (r transfer.subs/get-transfer-item db engine-id :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key)))))

(defn get-resolver-answer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) action-key
  ; @param (map) server-response
  ;
  ; @example
  ;  (r get-resolver-answer db :my-engine :get-items {...})
  ;  =>
  ;  [{...} {...}]
  ;
  ; @return (*)
  [db [_ engine-id action-key server-response]]
  (let [resolver-id (r get-resolver-id db engine-id action-key)]
       (resolver-id server-response)))



;; -- Data-receiving subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (get-in db [:engines :engine-handler/meta-items engine-id :data-received?]))