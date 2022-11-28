
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.config-handler.side-effects
    (:require [io.api                           :as io]
              [re-frame.api                     :as r]
              [x.core.config-handler.checks     :as config-handler.checks]
              [x.core.config-handler.config     :as config-handler.config]
              [x.core.config-handler.prototypes :as config-handler.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- import-app-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (if-let [app-config (io/read-edn-file config-handler.config/APP-CONFIG-FILEPATH)]
          (let [app-config (config-handler.prototypes/app-config-prototype app-config)]
               (try (config-handler.checks/check-app-config app-config)
                    (r/dispatch [:x.core/store-app-config!  app-config])
                    (catch Exception e (println e))))))

(defn- import-server-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (if-let [server-config (io/read-edn-file config-handler.config/SERVER-CONFIG-FILEPATH)]
          (let [server-config (config-handler.prototypes/server-config-prototype server-config)]
               (try (config-handler.checks/check-server-config server-config)
                    (r/dispatch [:x.core/store-server-config!  server-config])
                    (catch Exception e (println e))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.core/import-app-config! import-app-config!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.core/import-server-config! import-server-config!)
