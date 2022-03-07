
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.subs
    (:require [mid-fruits.time         :as time]
              [x.app-core.api          :as a :refer [r]]
              [x.app-ui.bubbles.engine :as bubbles.engine]
              [x.app-ui.renderer       :as renderer]
              [x.app-user.api          :as user]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bubbles-enabled-by-user?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (r user/get-user-settings-item db :notification-bubbles-enabled?))

(defn- bubble-lifetime-elapsed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (boolean)
  [db [_ bubble-id]]
  (let [render-requested-at (r renderer/get-render-log db :bubbles bubble-id :render-requested-at)]
       (> (time/elapsed)
          (+ render-requested-at bubbles.engine/BUBBLE-LIFETIME))))

(defn- get-bubble-lifetime-left
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (ms)
  [db [_ bubble-id]]
  (let [render-requested-at (r renderer/get-render-log db :bubbles bubble-id :render-requested-at)
        bubble-pop-time     (+ render-requested-at bubbles.engine/BUBBLE-LIFETIME)]
       (- bubble-pop-time (time/elapsed))))

(defn- autopop-bubble?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (boolean)
  [db [_ bubble-id]]
  (boolean (r renderer/get-element-prop db :bubbles bubble-id :autopop?)))
