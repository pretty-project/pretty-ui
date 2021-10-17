
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.28
; Description:
; Version: v0.4.6
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popup-header
    (:require [x.app-components.api    :as components]
              [x.app-core.api          :as a :refer [r]]
              [x.app-ui.popup-geometry :as geometry]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-popup-header-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  ;  {:render-touch-anchor? (boolean)}
  [db [_ popup-id]]
  {:render-touch-anchor? (r geometry/render-touch-anchor? db popup-id)})

(a/reg-sub ::get-popup-header-view-props get-popup-header-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:label-bar (map)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [label-bar]}]
  [:div.x-app-popups--element--label-bar
    [components/content popup-id label-bar]])

(defn- popup-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; @param (map) view-props
  ;  {:render-touch-anchor? (boolean)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [] :as popup-props}
            {:keys [render-touch-anchor?]}]
  [:div.x-app-popups--element--header
    (if render-touch-anchor?
        [:div.x-app-popups--element--touch-anchor])
    (if (geometry/popup-props->render-label-bar? popup-props)
        [popup-label-bar popup-id popup-props])])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (component)
  [popup-id popup-props]
  [components/subscriber popup-id
    {:component    #'popup-header
     :static-props popup-props
     :subscriber   [::get-popup-header-view-props popup-id]}])
