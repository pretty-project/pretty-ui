
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.no-internet-notification.views
    (:require [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-internet-connection-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/label ::no-internet-connection-label
                  {:content :no-internet-connection
                   :indent  {:left :xs}}])

(defn- refresh-app-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::refresh-app-button
                   {:indent   {:right :xs}
                    :label    :refresh!
                    :on-click [:boot-loader/refresh-app!]
                    :preset   :primary}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [_]
  [elements/horizontal-polarity ::body
                                {:end-content   [refresh-app-button]
                                 :start-content [no-internet-connection-label]}])
