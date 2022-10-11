
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.subs
    (:require [re-frame.api            :as r :refer [r]]
              [time.api                :as time]
              [x.app-ui.bubbles.config :as bubbles.config]
              [x.app-ui.renderer       :as renderer]
              [x.app-user.api          :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-bubble-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (keyword) prop-key
  ;
  ; @return (boolean)
  [db [_ bubble-id prop-key]]
  (r renderer/get-element-prop db :bubbles bubble-id prop-key))

(defn bubbles-enabled-by-user?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (r user/get-user-settings-item db :notification-bubbles-enabled?))

(defn bubble-lifetime-elapsed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (boolean)
  [db [_ bubble-id]]
  (let [render-requested-at (r renderer/get-render-log db :bubbles bubble-id :render-requested-at)]
       (> (time/elapsed)
          (+ render-requested-at bubbles.config/BUBBLE-LIFETIME))))

(defn get-bubble-lifetime-left
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (ms)
  [db [_ bubble-id]]
  (let [render-requested-at (r renderer/get-render-log db :bubbles bubble-id :render-requested-at)
        bubble-close-time     (+ render-requested-at bubbles.config/BUBBLE-LIFETIME)]
       (- bubble-close-time (time/elapsed))))

(defn autoclose-bubble?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (boolean)
  [db [_ bubble-id]]
  (boolean (r renderer/get-element-prop db :bubbles bubble-id :autoclose?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :ui/get-bubble-prop get-bubble-prop)
