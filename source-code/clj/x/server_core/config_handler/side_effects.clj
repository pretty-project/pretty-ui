
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler.side-effects
    (:require [server-fruits.io                    :as io]
              [x.server-core.config-handler.config :as config-handler.config]
              [x.server-core.event-handler         :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- import-app-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [app-config (io/read-edn-file config-handler.config/APP-CONFIG-FILEPATH)]
       (event-handler/dispatch [:core/store-app-config! app-config])))

(defn- import-server-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [server-config (io/read-edn-file config-handler.config/SERVER-CONFIG-FILEPATH)]
       (event-handler/dispatch [:core/store-server-config! server-config])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/import-app-config! import-app-config!)

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/import-server-config! import-server-config!)
