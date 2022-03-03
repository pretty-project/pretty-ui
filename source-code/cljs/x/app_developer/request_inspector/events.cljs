
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.request-inspector.events
    (:require [mid-fruits.map                         :refer [dissoc-in]]
              [x.app-core.api                         :as a :refer [r]]
              [x.app-developer.request-inspector.subs :as request-inspector.subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-requests!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (-> db (dissoc-in [:developer :request-inspector/meta-items :inspected-request])
         (dissoc-in [:developer :request-inspector/meta-items :request-history-dex])))

(defn inspect-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ request-id]]
  (assoc-in db [:developer :request-inspector/meta-items :inspected-request] request-id))

(defn inspect-prev-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [request-history-dex (r request-inspector.subs/get-request-history-dex db)]
       (assoc-in db [:developer :request-inspector/meta-items :request-history-dex]
                    (dec request-history-dex))))

(defn inspect-next-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [request-history-dex (r request-inspector.subs/get-request-history-dex db)]
       (assoc-in db [:developer :request-inspector/meta-items :request-history-dex]
                    (inc request-history-dex))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :request-inspector/show-requests! show-requests!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :request-inspector/inspect-request! inspect-request!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :request-inspector/inspect-prev-request! inspect-prev-request!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :request-inspector/inspect-next-request! inspect-next-request!)
