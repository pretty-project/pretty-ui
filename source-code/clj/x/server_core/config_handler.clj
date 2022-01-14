
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v1.0.2
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler
    (:require [server-fruits.io            :as io]
              [x.mid-core.config-handler   :as config-handler]
              [x.server-core.event-handler :as event-handler :refer [r]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def APP-CONFIG-FILEPATH    "monoset-environment/x.app-config.edn")

; @constant (string)
(def SERVER-CONFIG-FILEPATH "monoset-environment/x.server-config.edn")

; @constant (string)
(def SITE-CONFIG-FILEPATH   "monoset-environment/x.site-config.edn")



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.config-handler
(def get-app-config         config-handler/get-app-config)
(def get-app-config-item    config-handler/get-app-config-item)
(def get-server-config      config-handler/get-server-config)
(def get-server-config-item config-handler/get-server-config-item)
(def get-site-config        config-handler/get-site-config)
(def get-site-config-item   config-handler/get-site-config-item)
(def store-configs!         config-handler/store-configs!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-sub :core/get-app-config         get-app-config)
(event-handler/reg-sub :core/get-app-config-item    get-app-config-item)
(event-handler/reg-sub :core/get-server-config      get-server-config)
(event-handler/reg-sub :core/get-server-config-item get-server-config-item)
(event-handler/reg-sub :core/get-site-config        get-site-config)
(event-handler/reg-sub :core/get-site-config-item   get-site-config-item)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-db :core/store-configs! store-configs!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- config-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [app-config    (io/read-edn-file APP-CONFIG-FILEPATH)
        server-config (io/read-edn-file SERVER-CONFIG-FILEPATH)
        site-config   (io/read-edn-file SITE-CONFIG-FILEPATH)]
       (event-handler/dispatch [:core/store-configs! {:app-config    app-config
                                                      :server-config server-config
                                                      :site-config   site-config}])))

(event-handler/reg-handled-fx :core/config-server! config-server!)
