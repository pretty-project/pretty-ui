
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.01
; Description:
; Version: v0.1.4
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.crawler-handler
    (:require [mid-fruits.uri    :as uri]
              [mid-fruits.time   :as time]
              [server-fruits.io  :as io]
              [x.app-details     :as details]
              [x.server-core.api :as a :refer [r]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def ROBOTS-TXT-FILEPATH "resources/public/robots.txt")



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/initialize-crawler-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [app-home (r a/get-app-detail db :app-home)]
           {:environment/set-robots-txt! app-home})))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-robots-txt!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) app-home
  [app-home]
  (let [app-home     (uri/valid-path app-home)
        timestamp    (time/timestamp-string)
        file-content (str "\n# Generated by: " details/app-codename
                          "\n# Generated at: " timestamp "\n"
                          "\nUser-agent: *\nAllow: /\nDisallow: " app-home "\n")]
       (println details/app-codename "generating robots.txt file ...")
       (io/write-file! ROBOTS-TXT-FILEPATH file-content)))

(a/reg-fx :environment/set-robots-txt! set-robots-txt!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:environment/initialize-crawler-handler!]})
