
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.css-handler.events
    (:require [mid-fruits.vector :as vector]
              [re-frame.api      :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-css!
  ; @param (map) css-props
  ;  {:js-build (keyword)(opt)
  ;    A fájl kiszolgálása hozzárendelhető egy megadott JS build-hez.
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
(r/reg-event-db :environment/add-css! add-css!)
