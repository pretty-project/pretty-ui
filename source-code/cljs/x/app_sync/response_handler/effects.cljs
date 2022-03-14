
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
  ; @param (string) server-response-body
  ;  "{...}"
  (fn [{:keys [db]} [_ request-id server-response-body]]
      (let [filename (get-in db [:sync :request-handler/data-items request-id :filename])
            data-url (mixed/to-data-url server-response-body)])))
          ;[:tools/save-file! ...]
