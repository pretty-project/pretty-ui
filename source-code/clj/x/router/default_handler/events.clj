
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.router.default-handler.events
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-default-route!
  ; https://github.com/metosin/reitit/blob/master/doc/ring/default_handler.md
  ;
  ; @param (keyword) route-id
  ;  :method-not-allowed, :not-acceptable, :not-found
  ; @param (map) route-props
  ;  {:body (*)
  ;   :status (integer)}
  ;
  ; @usage
  ;  (r set-default-route! db :my-default-route {...})
  ;
  ; @return (map)
  [db [_ route-id route-props]]
  (assoc-in db [:x.router :default-handler/data-items route-id] route-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.router/set-default-route! :my-default-route {...}]
(r/reg-event-db :x.router/set-default-route! set-default-route!)