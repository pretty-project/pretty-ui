
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.request-inspector.subs
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (map/get-keys (get-in db [:sync :request-handler/data-history])))

(defn get-inspected-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:developer :request-inspector/meta-items :inspected-request]))

(defn get-request-history-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [request-id      (r get-inspected-request-id db)
        request-history (get-in db [:sync :request-handler/data-history request-id])]
       (count request-history)))

(defn get-request-history-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [request-id (r get-inspected-request-id db)]
       (if-let [request-history-dex (get-in db [:developer :request-inspector/meta-items :request-history-dex])]
               (return request-history-dex)
               (let [request-history (get-in db [:sync :request-handler/data-history request-id])]
                    (vector/last-dex request-history)))))

(defn get-request-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [request-id          (r get-inspected-request-id db)
        request-history-dex (r get-request-history-dex  db)]
       (get-in db [:sync :request-handler/data-history request-id request-history-dex])))

(defn get-request-response
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [request-id          (r get-inspected-request-id db)
        request-history-dex (r get-request-history-dex  db)]
       (get-in db [:sync :response-handler/data-history request-id request-history-dex])))

(defn get-request-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ prop-key]]
  (let [request-id          (r get-inspected-request-id db)
        request-history-dex (r get-request-history-dex  db)]
       (get-in db [:sync :request-handler/data-history request-id request-history-dex prop-key])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer/get-request-ids get-request-ids)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer/get-inspected-request-id get-inspected-request-id)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer/get-request-history-count get-request-history-count)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer/get-request-history-dex get-request-history-dex)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer/get-request-props get-request-props)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer/get-request-response get-request-response)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer/get-request-prop get-request-prop)
