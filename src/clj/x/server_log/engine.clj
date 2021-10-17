
; WARNING! EXPIRED! DO NOT USE!
;
; A log fájlok napi bontásban a /log könyvtárba íródjanak!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.08.29
; Description:
; Version: v0.3.4
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-log.engine
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param]]
              [mid-fruits.random  :as random]
              [mid-fruits.time    :as time]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-user.api  :as user]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- entry-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) entry-props
  ;
  ; @return (map)
  ;  {:app-build (string)
  ;   :app-name (string)
  ;   :time (object)}
  [entry-props]
  (merge (param entry-props)
         {:app-name  (a/subscribed [:x.server-core/get-config-item :app-name])
          :app-build (a/subscribed [:x.server-core/get-config-item :app-build])
          :time      (time/timestamp)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-entry!
  ; @param (map) entry-props
  ;
  ; @return (nil)
  [entry-props]
  (let [entry-props (entry-props-prototype entry-props)]
       (local-db/add-document! "log" entry-props)))

(defn upload-entry!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:entry-props (map)}}
  ;
  ; @return (map)
  [request]
  (if (user/request->authenticated? request)
      (let [entry-props (http/request->transit-param request :entry-props)]
           (add-entry! entry-props)
           (http/text-wrap {:body "Thank you!"}))))
