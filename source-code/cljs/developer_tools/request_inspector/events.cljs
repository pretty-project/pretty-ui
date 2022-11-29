
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.request-inspector.events
    (:require [developer-tools.request-inspector.subs :as request-inspector.subs]
              [map.api                                :refer [dissoc-in]]
              [re-frame.api                           :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-requests!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (-> db (dissoc-in [:developer-tools :request-inspector/meta-items :inspected-request])
         (dissoc-in [:developer-tools :request-inspector/meta-items :request-history-dex])))

(defn inspect-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ request-id]]
  (assoc-in db [:developer-tools :request-inspector/meta-items :inspected-request] request-id))

(defn inspect-prev-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [request-history-dex (r request-inspector.subs/get-request-history-dex db)]
       (assoc-in db [:developer-tools :request-inspector/meta-items :request-history-dex]
                    (dec request-history-dex))))

(defn inspect-next-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [request-history-dex (r request-inspector.subs/get-request-history-dex db)]
       (assoc-in db [:developer-tools :request-inspector/meta-items :request-history-dex]
                    (inc request-history-dex))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :developer-tools.request-inspector/show-requests! show-requests!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :developer-tools.request-inspector/inspect-request! inspect-request!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :developer-tools.request-inspector/inspect-prev-request! inspect-prev-request!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :developer-tools.request-inspector/inspect-next-request! inspect-next-request!)
