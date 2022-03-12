
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.subs
    (:require [x.app-core.api                    :as a :refer [r]]
              [x.app-sync.request-handler.config :as request-handler.config]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-status
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/get-request-status db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-status db request-id))

(defn get-request-sent-time
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/get-request-sent-time db :my-request)
  ;
  ; @return (string)
  [db [_ request-id]]
  (get-in db [:sync :request-handler/data-items request-id :sent-time]))

(defn get-request-activity
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/get-request-activity db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-activity db request-id))

(defn get-request-progress
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/get-request-progress db :my-request)
  ;
  ; @return (keyword)
  [db [_ request-id]]
  (r a/get-process-progress db request-id))

(defn request-active?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-active? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (r a/process-active? db request-id))

(defn request-sent?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-sent? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [sent-time (r get-request-sent-time db request-id)]
       (some? sent-time)))

(defn request-successed?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-successed? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-status (r a/get-process-status db request-id)]
       (= request-status :success)))

(defn request-failured?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-failured? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-status (r a/get-process-status db request-id)]
       (= request-status :failure)))

(defn request-aborted?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/request-aborted? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (get-in db [:sync :request-handler/data-items request-id :aborted?]))

(defn request-resent?
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:sent-time (string)}
  ;
  ; @usage
  ;  (r sync/request-resent? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id {:keys [sent-time]}]]
  (not= sent-time (get-in db [:sync :request-handler/data-items request-id :sent-time])))

(defn listening-to-request?
  ; @param (keyword) request-id
  ;
  ; @usage
  ;  (r sync/listening-to-request? db :my-request)
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [request-activity (r a/get-process-activity db request-id)]
       (or (= request-activity :active)
           (= request-activity :idle))))

(defn get-request-on-failure-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-failure-event (get-in db [:sync :request-handler/data-items request-id :on-failure])]
          (a/metamorphic-event<-params on-failure-event server-response)))

(defn get-request-on-success-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-success-event (get-in db [:sync :request-handler/data-items request-id :on-success])]
          (a/metamorphic-event<-params on-success-event server-response)))

(defn get-request-on-sent-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (metamorphic-event)
  [db [_ request-id]]
  (get-in db [:sync :request-handler/data-items request-id :on-sent]))

(defn get-request-on-responsed-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ request-id server-response]]
  (if-let [on-responsed-event (get-in db [:sync :request-handler/data-items request-id :on-responsed])]
          (a/metamorphic-event<-params on-responsed-event server-response)))

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
  [db [_ request-id {:keys [on-stalled request-successed?]} server-response]]
  ; Az {:on-stalled ...} esemény használható az {:on-success ...} esemény alternatívájaként,
  ; mert hibás teljesítés esetén nem történik meg.
  (if (and on-stalled request-successed?)
      (a/metamorphic-event<-params on-stalled server-response)))

(defn get-request-idle-timeout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (integer)
  [db [_ request-id]]
  (get-in db [:sync :request-handler/data-items request-id :idle-timeout]
             request-handler.config/DEFAULT-IDLE-TIMEOUT))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:sync/get-request-status :my-request]
(a/reg-sub :sync/get-request-status get-request-status)

; @usage
;  [:sync/get-request-sent-time :my-request]
(a/reg-sub :sync/get-request-sent-time get-request-sent-time)

; @usage
;  [:sync/get-request-activity :my-request]
(a/reg-sub :sync/get-request-activity get-request-activity)

; @usage
;  [:sync/get-request-progress :my-request]
(a/reg-sub :sync/get-request-progress get-request-progress)

; @usage
;  [:sync/request-active? :my-request]
(a/reg-sub :sync/request-active? request-active?)

; @usage
;  [:sync/request-sent? :my-request]
(a/reg-sub :sync/request-sent? request-sent?)

; @usage
;  [:sync/request-successed? :my-request]
(a/reg-sub :sync/request-successed? request-successed?)
; @usage
;  [:sync/request-failured? :my-request]
(a/reg-sub :sync/request-failured? request-failured?)

; @usage
;  [:sync/request-aborted? :my-request]
(a/reg-sub :sync/request-aborted? request-aborted?)

; @usage
;  [:sync/request-resent? :my-request]
(a/reg-sub :sync/request-resent? request-resent?)

; @usage
;  [:sync/listening-to-request? :my-request]
(a/reg-sub :sync/listening-to-request? listening-to-request?)
