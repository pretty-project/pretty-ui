
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.07
; Description:
; Version: v0.1.8
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.database-browser
    (:require [server-fruits.http :as http]
              [x.server-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- print-db-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db]
  (str db))

(defn- download-db-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [db (a/subscribed [:db/get-db])]
       (http/text-wrap {:body (print-db-browser db)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:router/add-route! :developer/db-browser-route
                                                     {:route-template "/developer/db-browser"
                                                      :get (fn [request] (download-db-browser request))}]
                                                     ;:restricted? true
                                 [:router/add-route! :developer/db-browser-extended-route
                                                     {:route-template "/developer/db-browser/:path"
                                                      :get (fn [request] (download-db-browser request))}]]}})
                                                     ;:restricted? true
