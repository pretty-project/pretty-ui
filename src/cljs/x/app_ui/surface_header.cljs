
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.29
; Description:
; Version: v0.2.8
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface-header
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.css            :as css]
              [mid-fruits.map            :as map]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-ui.surface-geometry :as geometry]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) surface-props
  ; @param (map) view-props
  ;  {:extended? (boolean)}
  ;
  ; @return (hiccup)
  [component-id surface-props {:keys [extended?]}]
  (let [height (if extended? (geometry/surface-props->extended-surface-header-height surface-props)
                             (geometry/surface-props->compact-surface-header-height  surface-props))]
       {:class "x-app-surface-header"
        :style {:height (css/px height)}}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:extended? (boolean)}
  [db _]
  {:extended? (r geometry/surface-header-extended? db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-header-control-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) surface-props
  ;  {:control-bar (map)}
  ;
  ; @return (hiccup)
  [component-id {:keys [control-bar]}]
  [:div.x-app-surface-header--control-bar
    [components/content component-id control-bar]])

(defn- surface-header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) surface-props
  ;  {:label-bar (map)}
  ;
  ; @return (hiccup)
  [component-id {:keys [label-bar]}]
  [:div.x-app-surface-header--label-bar
    [components/content component-id label-bar]])

(defn- surface-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) surface-props
  ; @param (map) view-props

  ;
  ; @return (hiccup)
  [component-id surface-props view-props]
  [:div (header-attributes component-id surface-props view-props)
        (if (geometry/surface-props->render-control-bar? surface-props)
            [surface-header-control-bar component-id surface-props])
        (if (geometry/surface-props->render-label-bar?   surface-props)
            [surface-header-label-bar   component-id surface-props])])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) surface-props
  ;  {:label-bar (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :control-bar (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}}
  ;
  ; @return (hiccup)
  [_ surface-props]
  [components/subscriber {:component    #'surface-header
                          :static-props surface-props
                          :subscriber   [::get-view-props]}])
