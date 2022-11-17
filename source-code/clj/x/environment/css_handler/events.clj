
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.css-handler.events
    (:require [re-frame.api :as r]
              [vector.api   :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-css!
  ; @param (map) css-props
  ;  {:js-build (keyword)(opt)
  ;    A fájl kiszolgálása hozzárendelhető egy megadott JS build-hez.
  ;   :uri (string)}
  ;
  ; @usage
  ;  (r add-css! db {...})
  ;
  ; @return (map)
  [db [_ css-props]]
  (update-in db [:x.environment :css-handler/data-items] vector/conj-item css-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.environment/add-css! {...}]
(r/reg-event-db :x.environment/add-css! add-css!)
