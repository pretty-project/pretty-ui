
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v0.1.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-log.engine
    (:require [mid-fruits.random :as random]
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
  (merge entry-props
         {:route-path (r router/get-current-route-path db)}))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-log/add-entry!
  ; @param (map) entry-props
  ;  {:entry-type (keyword)(opt)
  ;    XXX#4982
  ;    :db, :error, :user
  ;    Default: :error}
  (fn [{:keys [db]} [_ entry-props]]
      (let [entry-props (r entry-props-prototype db entry-props)]
           {:dispatch [:x.app-sync/send-request!
                       (random/generate-keyword)
                       {:method :post
                        :params {:entry-props entry-props}
                        :uri "/log/upload-entry"}]})))
