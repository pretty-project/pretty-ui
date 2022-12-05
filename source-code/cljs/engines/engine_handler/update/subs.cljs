
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.update.subs
    (:require [engines.engine-handler.errors.subs   :as errors.subs]
              [engines.engine-handler.transfer.subs :as transfer.subs]
              [re-frame.api                         :refer [r]]))



;; -- Request subscriptions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) action-key
  ;
  ; @example
  ; (r get-mutation-name db :my-engine :delete-items)
  ; =>
  ; :my-handler/delete-items!
  ;
  ; @return (keyword)
  [db [_ engine-id action-key]]
  (if-let [handler-key (r transfer.subs/get-transfer-item db engine-id :handler-key)]
          (keyword      (name handler-key)
                   (str (name action-key)))
          (r errors.subs/print-missing-handler-key db engine-id)))

(defn get-mutation-answer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) action-key
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-mutation-answer db :my-engine :delete-items {...})
  ; =>
  ; ["my-item" "your-item"]
  ;
  ; @return (*)
  [db [_ engine-id action-key server-response]]
  (let [mutation-name (r get-mutation-name db engine-id action-key)]
       ((symbol mutation-name) server-response)))
