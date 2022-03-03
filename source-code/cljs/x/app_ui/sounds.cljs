
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.6.2
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sounds
    (:require [app-fruits.dom :as dom]
              [x.app-core.api :as a :refer [r]]
              [x.app-user.api :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sound-id->catalog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sound-id
  ;
  ; @example
  ;  (sound-id->catalog-id :my-sound)
  ;  =>
  ;  "x-app-sound--my-sound"
  ;
  ; @return (string)
  [sound-id]
  (str "x-app-sound--" (name sound-id)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn enabled-by-user?
  ; @return (boolean)
  [db _]
  (r user/get-user-profile-item db :notifications :notification-sounds.enabled?))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn play-sound!
  ; @param (keyword) sound-id
  ;
  ; @usage
  ;  (ui/play-sound! :my-sound)
  [sound-id]
  (let [catalog-id      (sound-id->catalog-id  sound-id)
        catalog-element (dom/get-element-by-id catalog-id)]
       (.play catalog-element)))

; @usage
;  [:ui/play-sound! :my-sound]
(a/reg-fx :ui/play-sound! play-sound!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div#x-app-sounds [:audio#x-app-sound--click-1 [:source {:src "/sounds/click-1.ogg" :type "audio/ogg"}]
                                                  [:source {:src "/sounds/click-1.mp3" :type "audio/mp3"}]]])
