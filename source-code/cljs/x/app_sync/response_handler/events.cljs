
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.response-handler.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn target-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id server-response]]
  (if-let [target-path (get-in db [:sync :request-handler/data-items request-id :target-path])]
          (r db/set-item! db target-path server-response)
          (return db)))

(defn store-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id server-response]]
  (as-> db % (r target-request-response! % request-id server-response)
             ; DEBUG
             ; A request-id azonosítójú lekérés érkezett szerver-válasz utolsó 256 példányát eltárolja
             (r db/apply-item! % [:sync :response-handler/data-history request-id] vector/conj-item server-response)
             (r db/apply-item! % [:sync :response-handler/data-history request-id] vector/last-items 256)
             (r db/set-item!   % [:sync :response-handler/data-items   request-id] server-response)))
