
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.install-handler.side-effects
    (:require [candy.api                     :refer [return]]
              [io.api                        :as io]
              [re-frame.api                  :as r]
              [time.api                      :as time]
              [x.app-details                 :as x.app-details]
              [x.core.install-handler.config :as install-handler.config]
              [x.core.install-handler.state  :as install-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-installer!
  ; @param (keyword) installer-id
  ; @param (function) installer-f
  ;
  ; @usage
  ; (defn my-installer-f [] ...)
  ; (reg-installer! :my-installer my-installer-f)
  [installer-id installer-f]
  (swap! install-handler.state/INSTALLERS assoc installer-id installer-f))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- install-server!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [install-log (io/read-edn-file install-handler.config/INSTALL-LOG-FILEPATH {:warn? false})]
       (letfn [(f [_ installer-id installer-f]
                  (if-let [installed? (get install-log installer-id)]
                          (return nil)
                          (do (println x.app-details/app-codename "installing:" installer-id)
                              (io/swap-edn-file! install-handler.config/INSTALL-LOG-FILEPATH assoc installer-id
                                                 {:result       (installer-f)
                                                  :installed-at (time/timestamp-string)}))))]
              (reduce-kv f {} @install-handler.state/INSTALLERS))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.core/install-server! install-server!)
