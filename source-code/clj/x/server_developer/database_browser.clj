
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.07
; Description:
; Version: v0.2.6
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.database-browser
    (:require [mid-fruits.reader  :as reader]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path]}]
  (if (vector/nonempty? path)
      (let [parent-path (vector/pop-last-item path)]
           (str "<div><a href=\"?path=" parent-path "\">..</a></div>"))
      (str "<div>..</div>")))

(defn- map-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path]} k v]
  (let [target-path (conj path k)]
       (str "<div><a href=\"?path=" target-path "\">" k "</a></div>")))

(defn- map-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [current-item] :as browser-props}]
  (reduce-kv #(str %1 (map-item browser-props %2 %3))
             "" current-item))

(defn- db-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [current-item] :as browser-props}]
  (str (up-button browser-props)
       (cond (map?    current-item) (map-items browser-props)
             (nil?    current-item) (str "nil")
             (string? current-item) (str "\"" current-item "\"")
             :else                  (str      current-item))))

(defn- print-db-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [path] :as browser-props}]
  (str "<html>"
       "<body>"
      (db-browser browser-props)
      "</body>"
      "</html>"))

(defn- download-db-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params]}]
  (let [db           (a/subscribed [:db/get-db])
        path         (reader/string->mixed (get query-params "path"))
        current-item (get-in db path)]
       (http/html-wrap {:body (print-db-browser {:current-item current-item :path path})})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:router/add-route! :developer/db-browser-route
                                                     {:route-template "/developer/db-browser"
                                                      :get (fn [request] (download-db-browser request))}]]}})
                                                     ;:restricted? true]]}})
