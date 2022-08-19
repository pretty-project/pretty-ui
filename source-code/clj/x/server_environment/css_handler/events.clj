
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.css-handler.events
    (:require [mid-fruits.vector :as vector]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-css!
  ; @param (map) css-props
  ;  {:core-js (string)(opt)
  ;   :uri (string)}
  ;
  ; @usage
  ;  (r environment/add-css! db {...})
  ;
  ; @return (map)
  [db [_ css-props]]
  (update-in db [:environment :css-handler/data-items] vector/conj-item css-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/add-css! {...}]
(a/reg-event-db :environment/add-css! add-css!)
