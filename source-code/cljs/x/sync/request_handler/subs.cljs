
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.sync.request-handler.subs
    (:require [re-frame.api :as r :refer [r]]
              [x.core.api   :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-status
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r get-request-status db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r x.core/get-process-status db request-id))

(defn get-request-sent-time
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r get-request-sent-time db :my-request)
  ;
  ; @return (string)
  [db [_ request-id]]
  (get-in db [:x.sync :request-handler/meta-items request-id :sent-time]))

(defn get-request-activity
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r get-request-activity db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r x.core/get-process-activity db request-id))

(defn get-request-progress
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r get-request-progress db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r x.core/get-process-progress db request-id))

(defn request-active?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r request-active? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (r x.core/process-active? db request-id))

(defn request-sent?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r request-sent? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [sent-time (r get-request-sent-time db request-id)]
       (some? sent-time)))

(defn request-successed?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r request-successed? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-status (r x.core/get-process-status db request-id)]
       (= request-status :success)))

(defn request-failured?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r request-failured? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-status (r x.core/get-process-status db request-id)]
       (= request-status :failure)))

(defn request-stalled?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r request-stalled? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-activity (r x.core/get-process-activity db request-id)]
       (= request-activity :stalled)))

(defn request-aborted?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r request-aborted? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (get-in db [:x.sync :request-handler/meta-items request-id :aborted?]))

(defn request-resent?
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:sent-time (string)}
  ;
  ; @usage
  ;  (r request-resent? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id {:keys [sent-time]}]]
  (not= sent-time (r get-request-sent-time db request-id)))

(defn listening-to-request?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r listening-to-request? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-activity (r x.core/get-process-activity db request-id)]
       (or (= request-activity :active)
           (= request-activity :idle))))

(defn get-request-on-failure-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:on-failure (metamorphic-event)(opt)}
  ; @param (map) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ _ {:keys [on-failure]} server-response]]
  (if on-failure (r/metamorphic-event<-params on-failure server-response)))

(defn get-request-on-success-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:on-success (metamorphic-event)(opt)}
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ _ {:keys [on-success]} server-response]]
  (if on-success (r/metamorphic-event<-params on-success server-response)))

(defn get-request-on-responsed-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:on-responsed (metamorphic-event)(opt)}
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ _ {:keys [on-responsed]} server-response]]
  (if on-responsed (r/metamorphic-event<-params on-responsed server-response)))

(defn get-request-on-stalled-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:on-stalled (metamorphic-event)(opt)
  ;   :request-successed? (boolean)(opt)}
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ _ {:keys [on-stalled request-successed?]} server-response]]
  ; Az {:on-stalled ...} tulajdonságként átadott esemény használható az {:on-success ...}
  ; tulajdonságként átadott esemény alternatívájaként, mert hibás teljesítés esetén nem történik meg.
  (if (and on-stalled request-successed?)
      (r/metamorphic-event<-params on-stalled server-response)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.sync/get-request-status :my-request]
(r/reg-sub :x.sync/get-request-status get-request-status)

; @usage
;  [:x.sync/get-request-sent-time :my-request]
(r/reg-sub :x.sync/get-request-sent-time get-request-sent-time)

; @usage
;  [:x.sync/get-request-activity :my-request]
(r/reg-sub :x.sync/get-request-activity get-request-activity)

; @usage
;  [:x.sync/get-request-progress :my-request]
(r/reg-sub :x.sync/get-request-progress get-request-progress)

; @usage
;  [:x.sync/request-active? :my-request]
(r/reg-sub :x.sync/request-active? request-active?)

; @usage
;  [:x.sync/request-sent? :my-request]
(r/reg-sub :x.sync/request-sent? request-sent?)

; @usage
;  [:x.sync/request-successed? :my-request]
(r/reg-sub :x.sync/request-successed? request-successed?)

; @usage
;  [:x.sync/request-failured? :my-request]
(r/reg-sub :x.sync/request-failured? request-failured?)

; @usage
;  [:x.sync/request-stalled? :my-request]
(r/reg-sub :x.sync/request-stalled? request-stalled?)

; @usage
;  [:x.sync/request-aborted? :my-request]
(r/reg-sub :x.sync/request-aborted? request-aborted?)

; @usage
;  [:x.sync/request-resent? :my-request]
(r/reg-sub :x.sync/request-resent? request-resent?)

; @usage
;  [:x.sync/listening-to-request? :my-request]
(r/reg-sub :x.sync/listening-to-request? listening-to-request?)
