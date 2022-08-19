
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.response-handler.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn target-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:target-path (vector)(opt)}
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ _ {:keys [target-path]} server-response]]
  (if target-path (r db/set-item! db target-path server-response)
                  (return         db)))

(defn store-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id request-props server-response]]
  ; DEBUG
  ; A request-id azonosítójú lekérés érkezett szerver-válasz utolsó 256 példányát eltárolja
  (as-> db % (r target-request-response! % request-id server-response)
             (r db/apply-item! % [:sync :response-handler/data-history request-id] vector/conj-item server-response)
             (r db/apply-item! % [:sync :response-handler/data-history request-id] vector/last-items 256)
             (r db/set-item!   % [:sync :response-handler/data-items   request-id] server-response)))
