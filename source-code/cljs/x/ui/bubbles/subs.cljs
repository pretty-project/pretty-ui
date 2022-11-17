
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.bubbles.subs
    (:require [re-frame.api        :as r :refer [r]]
              [time.api            :as time]
              [x.ui.bubbles.config :as bubbles.config]
              [x.ui.renderer.subs  :as renderer.subs]
              [x.user.api          :as x.user]))



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
  (r renderer.subs/get-element-prop db :bubbles bubble-id prop-key))

(defn bubbles-enabled-by-user?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (r x.user/get-user-settings-item db :notification-bubbles-enabled?))

(defn bubble-lifetime-elapsed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (boolean)
  [db [_ bubble-id]]
  (let [render-requested-at (r renderer.subs/get-render-log db :bubbles bubble-id :render-requested-at)]
       (> (time/elapsed)
          (+ render-requested-at bubbles.config/BUBBLE-LIFETIME))))

(defn get-bubble-lifetime-left
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (ms)
  [db [_ bubble-id]]
  (let [render-requested-at (r renderer.subs/get-render-log db :bubbles bubble-id :render-requested-at)
        bubble-close-time     (+ render-requested-at bubbles.config/BUBBLE-LIFETIME)]
       (- bubble-close-time (time/elapsed))))

(defn autoclose-bubble?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (boolean)
  [db [_ bubble-id]]
  (boolean (r renderer.subs/get-element-prop db :bubbles bubble-id :autoclose?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/get-bubble-prop get-bubble-prop)
