
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v0.1.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-log.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.random :as random]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- entry-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) entry-props
  ;
  ; @return (map)
  ;  {:route-path (string)}
  [db [_ entry-props]]
  (merge (param entry-props)
         {:route-path (r router/get-current-route-path db)}))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :log/add-entry!
  ; @param (keyword)(opt) entry-id
  ; @param (map) entry-props
  ;  {:entry-type (keyword)(opt)
  ;    XXX#4982
  ;    :db, :error, :user
  ;    Default: :error}
  (fn [{:keys [db]} event-vector]
      (let [entry-id    (a/event-vector->second-id   event-vector)
            entry-props (a/event-vector->first-props event-vector)
            entry-props (r entry-props-prototype db entry-props)]
           [:sync/send-request! ::synchronize!
                                {:method :post
                                 :params {:entry-props entry-props}
                                 :uri    "/log/upload-entry"}])))
