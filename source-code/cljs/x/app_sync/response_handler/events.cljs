
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.response-handler.events
    (:require [candy.api         :refer [return]]
              [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [re-frame.api      :as r :refer [r]]
              [x.app-core.api    :as x.core]
              [x.app-db.api      :as x.db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-response-f!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:response-f (function)(opt)}
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id {:keys [response-f]} server-response]]
  (if response-f (r response-f db request-id server-response)
                 (return       db)))

(defn store-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id request-props server-response]]
  (if (r x.core/debug-mode-detected? db)
      (as-> db % (r use-response-f!  % request-id request-props server-response)
                 (r x.db/set-item!   % [:sync :response-handler/data-items request-id] server-response)
                 ; DEBUG A request-id azonosítójú lekérésre érkezett szerver-válasz utolsó 256 példányát eltárolja
                 (r x.db/apply-item! % [:sync :response-handler/data-history request-id] vector/conj-item server-response)
                 (r x.db/apply-item! % [:sync :response-handler/data-history request-id] vector/last-items 256))
      (as-> db % (r use-response-f!  % request-id request-props server-response)
                 (r x.db/set-item!   % [:sync :response-handler/data-items request-id] server-response))))

(defn clear-request-response!
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/clear-request-response! db :my-request)
  ;
  ; @return (map)
  [db [_ request-id]]
  (dissoc-in db [:sync :response-handler/data-items request-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:sync/clear-request-response! :my-request]
(r/reg-event-db :sync/clear-request-response! clear-request-response!)
