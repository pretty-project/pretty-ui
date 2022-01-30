
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.22
; Description:
; Version: v0.5.0
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.developer-tools
    (:require [mid-fruits.pretty  :as pretty]
              [mid-fruits.string  :as string]
              [mongo-db.api       :as mongo-db]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path]}]
  (println path)
  (str "<a href=\"/developer-tools/re-frame-browser\">Re-Frame browser</a>"
       "<a href=\"/developer-tools/mongo-db-browser\">MongoDB browser</a>"))

(defn- print-developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (str "<html>"
       "<body>"
       "<pre style=\"white-space: normal\">"
       (developer-tools request)
       "</pre>"
       "</body>"
       "</html>"))

(defn- download-developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (http/html-wrap {:body (print-developer-tools request)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-if [(= (System/getenv "DEVELOPER") "true")
                                  [:router/add-route! :developer/developer-tools-route
                                                      {:route-template "/developer-tools"
                                                       :get (fn [request] (download-developer-tools request))}]
                                  (= (System/getenv "DEVELOPER") "true")
                                  [:router/add-route! :developer/developer-tools-extended-route
                                                      {:route-template "/developer-tools/:tool-id"
                                                       :get (fn [request] (download-developer-tools request))}]]}})
