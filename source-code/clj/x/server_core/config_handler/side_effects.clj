
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler.side-effects
    (:require [io.api                              :as io]
              [re-frame.api                        :as r :refer [r]]
              [x.server-core.config-handler.checks :as config-handler.checks]
              [x.server-core.config-handler.config :as config-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- import-app-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [app-config (io/read-edn-file config-handler.config/APP-CONFIG-FILEPATH)]
       (try (config-handler.checks/check-app-config app-config)
            (r/dispatch [:core/store-app-config! app-config])
            (catch Exception e (println e)))))

(defn- import-server-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [server-config (io/read-edn-file config-handler.config/SERVER-CONFIG-FILEPATH)]
       (try (config-handler.checks/check-server-config server-config)
            (r/dispatch [:core/store-server-config! server-config])
            (catch Exception e (println e)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :core/import-app-config! import-app-config!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :core/import-server-config! import-server-config!)
