
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.config-handler.side-effects
    (:require [re-frame.api                        :as r :refer [r]]
              [server-fruits.io                    :as io]
              [x.server-core.config-handler.config :as config-handler.config]))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- import-app-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [app-config (io/read-edn-file config-handler.config/APP-CONFIG-FILEPATH)]
       (r/dispatch [:core/store-app-config! app-config])))

(defn- import-server-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [server-config (io/read-edn-file config-handler.config/SERVER-CONFIG-FILEPATH)]
       (r/dispatch [:core/store-server-config! server-config])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :core/import-app-config! import-app-config!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :core/import-server-config! import-server-config!)
