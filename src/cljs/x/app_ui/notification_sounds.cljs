
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.3.8
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.notification-sounds
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.keyword :as keyword]
              [x.app-core.api     :as a :refer [r]]
              [x.app-user.api     :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sound-id->catalog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sound-id
  ;
  ; @example
  ;  (sound-id->catalog-id :click-1)
  ;  => "x-app-sound--click-1"
  ;
  ; @return (string)
  [sound-id]
  (str "x-app-sound--" (keyword/to-string sound-id)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn enabled-by-user?
  ; @return (boolean)
  [db _]
  (r user/get-user-profile-item db :notifications :notification-sounds.enabled?))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- play-sound!
  ; @param (keyword) sound-id
  [sound-id]
  (let [catalog-id      (sound-id->catalog-id  sound-id)
        catalog-element (dom/get-element-by-id catalog-id)]
       (.play catalog-element)))

; @usage
;  [:x.app-ui/play-sound! :click-1]
(a/reg-handled-fx :x.app-ui/play-sound! play-sound!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (hiccup)
  []
  [:div#x-app-sounds
    [:audio#x-app-sound--click-1
      [:source {:src "/sounds/click-1.ogg" :type "audio/ogg"}]
      [:source {:src "/sounds/click-1.mp3" :type "audio/mp3"}]]])
