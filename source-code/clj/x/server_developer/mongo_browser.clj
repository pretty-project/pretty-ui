
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.07
; Description:
; Version: v0.2.6
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.mongo-browser
    (:require [mid-fruits.reader  :as reader]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- download-mongo-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:router/add-route! :developer/mongo-browser-route
                                                     {:route-template "/developer/mongo-browser"
                                                      :get (fn [request] (download-mongo-browser request))}]]}})
                                                     ;:restricted? true]]}})
