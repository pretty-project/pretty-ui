
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v0.8.2
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler
    (:require [server-fruits.io            :as io]
              [x.mid-core.config-handler   :as config-handler]
              [x.server-core.event-handler :as event-handler :refer [r]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-CONFIG-FILEPATH "project-details.edn")



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.config-handler
(def get-app-details        config-handler/get-app-details)
(def get-app-detail         config-handler/get-app-detail)
(def get-browser-details    config-handler/get-browser-details)
(def get-browser-detail     config-handler/get-browser-detail)
(def get-database-details   config-handler/get-database-details)
(def get-database-detail    config-handler/get-database-detail)
(def get-site-links         config-handler/get-site-links)
(def get-site-link          config-handler/get-site-link)
(def get-seo-details        config-handler/get-seo-details)
(def get-seo-detail         config-handler/get-seo-detail)
(def get-storage-details    config-handler/get-storage-details)
(def get-storage-detail     config-handler/get-storage-detail)
(def get-js-details         config-handler/get-js-details)
(def get-js-detail          config-handler/get-js-detail)
(def get-css-paths          config-handler/get-css-paths)
(def get-favicon-paths      config-handler/get-favicon-paths)
(def get-plugin-js-paths    config-handler/get-plugin-js-paths)
(def get-configs            config-handler/get-configs)
(def get-destructed-configs config-handler/get-destructed-configs)
(def get-config-item        config-handler/get-config-item)
(def store-configs!         config-handler/store-configs!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-sub :x.server-core/get-app-details        get-app-details)
(event-handler/reg-sub :x.server-core/get-site-links         get-site-links)
(event-handler/reg-sub :x.server-core/get-storage-details    get-storage-details)
(event-handler/reg-sub :x.server-core/get-configs            get-configs)
(event-handler/reg-sub :x.server-core/get-destructed-configs get-destructed-configs)
(event-handler/reg-sub :x.server-core/get-config-item        get-config-item)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-db :x.server-core/store-configs! store-configs!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-handled-fx
  :x.server-core/config-app!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) config-filepath
  (fn [[config-filepath]]
      (let [config-filepath     (or config-filepath DEFAULT-CONFIG-FILEPATH)
            config-file-content (io/read-edn-file config-filepath)]
           (event-handler/dispatch [:x.server-core/store-configs! config-file-content]))))
