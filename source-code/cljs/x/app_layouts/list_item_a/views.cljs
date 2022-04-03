
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.list-item-a.views
    (:require [x.app-components.api :as components]
              [x.app-elements.api   :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-secondary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:timestamp (string)(opt)}
  [_ {:keys [timestamp]}]
  [:div.x-list-item-a--secondary-details [:div.x-list-item-a--timestamp (components/content timestamp)]])

(defn- list-item-primary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)}
  [_ {:keys [description label] :as item-props}]
  [:div.x-list-item-a--primary-details [:div.x-list-item-a--label       (components/content label)]
                                       [:div.x-list-item-a--description (components/content description)]])

(defn- list-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  [:div.x-list-item-a--details [list-item-primary-details   item-dex item-props]
                               [list-item-secondary-details item-dex item-props]])

(defn- list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:colors (strings in vector)(opt)}
  [item-dex {:keys [colors] :as item-props}]
  [:div.x-list-item-a [elements/color-stamp {:colors colors :size :s :style {:margin "12px"}}]
                      [list-item-details item-dex item-props]])

(defn toggle-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-click (metamorphic-event)}
  [item-dex {:keys [disabled? on-click] :as item-props}]
  [elements/toggle {:content   [list-item-structure item-dex item-props]
                    :disabled? disabled?
                    :on-click  on-click
                    :hover-color :highlight}])

(defn static-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  [list-item-structure item-dex item-props])

(defn list-item
  ; @param (integer)(opt) item-dex
  ; @param (map) item-props
  ;  {:color (string)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :label (metamorphic-content)
  ;   :description (metamorphic-content)(opt)
  ;   :on-click (metamorphic-event)(opt)
  ;   :timestamp (string)(opt)}
  ;
  ; @usage
  ;  [layouts/list-item-a {...}]
  ;
  ; @usage
  ;  [layouts/list-item-a 0 {...}]
  ([item-props]
   [list-item 0 item-props])

  ([item-dex {:keys [on-click] :as item-props}]
   (if on-click [toggle-list-item item-dex item-props]
                [static-list-item item-dex item-props])))
