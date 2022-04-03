
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.list-item-a.views
    (:require [mid-fruits.css                    :as css]
              [x.app-components.api              :as components]
              [x.app-core.api                    :as a]
              [x.app-elements.api                :as elements]
              [x.app-layouts.list-item-a.helpers :as list-item-a.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-header-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:colors (strings in vector)(opt)}
  [_ {:keys [header]}]
  [elements/color-stamp {:colors (:colors header) :size :l}])

(defn- list-item-header-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:icon (keyword)(opt)
  ;     :icon-family (keyword)(opt)}}
  [_ {:keys [header]}]
  (let [icon-props (select-keys header [:icon :icon-family])]
       [elements/icon icon-props]))

(defn- list-item-header-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:thumbnail (string)(opt)}}
  [_ {:keys [header]}]
  [:div.x-list-item-a--thumbnail {:style {:background-image (-> header :thumbnail css/url)}}])

(defn- list-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:colors (strings in vector)(opt)
  ;     :icon (keyword)(opt)
  ;     :thumbnail (string)(opt)}}
  [item-dex {:keys [header] :as item-props}]
  [:div.x-list-item-a--header (if (:colors    header) [list-item-header-colors    item-dex item-props])
                              (if (:icon      header) [list-item-header-icon      item-dex item-props])
                              (if (:thumbnail header) [list-item-header-thumbnail item-dex item-props])])

(defn- list-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)
  ;   :timestamp (metamorphic-content)(opt)}
  [_ {:keys [label description timestamp]}]
  [:div.x-list-item-a--details                 [:div.x-list-item-a--label       (components/content label)]
                               (if timestamp   [:div.x-list-item-a--timestamp   (components/content timestamp)])
                               (if description [:div.x-list-item-a--description (components/content description)])])

(defn- list-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:icon (keyword)(opt)}
  [_ item-props]
  (let [icon-props (select-keys item-props [:icon :icon-family])]
       [:div.x-list-item-a--icon [elements/icon icon-props]]))

(defn- list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  [:div.x-list-item-a (list-item-a.helpers/list-item-structure-attributes item-dex item-props)
                      [list-item-header                                   item-dex item-props]
                      [list-item-details                                  item-dex item-props]
                      [list-item-icon                                     item-dex item-props]])

(defn toggle-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-click (metamorphic-event)
  ;   :on-right-click (metamorphic-event)(opt)}
  [item-dex {:keys [disabled? on-click on-right-click] :as item-props}]
  [elements/toggle {:content        [list-item-structure item-dex item-props]
                    :disabled?      disabled?
                    :on-click       on-click
                    :on-right-click on-right-click
                    :hover-color    :highlight}])

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
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :description (metamorphic-content)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)(opt)
  ;   :on-right-click (metamorphic-event)(opt)
  ;   :style (map)(opt)
  ;   :header (map)
  ;    {:colors (strings in vector)
  ;     :icon (keyword)(opt)
  ;     :icon-family (keyword)(opt)
  ;      :material-icons-filled, :material-icons-outlined
  ;      Default: :material-icons-filled
  ;      Only w/ {:thumbnail {:icon ...}}
  ;     :thumbnail (string)(opt)}
  ;   :timestamp (metamorphic-content)(opt)}
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
