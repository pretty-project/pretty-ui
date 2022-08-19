
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.response-handler.effects
    (:require [mid-fruits.mixed :as mixed]
              [x.app-core.api   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/save-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-response
  ;  {:filename (string)}
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id {:keys [filename]} server-response-body]]
      (let [data-url (mixed/to-data-url server-response-body)])))
          ;[:tools/save-file! ...]
