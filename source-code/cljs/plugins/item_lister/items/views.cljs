
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.views
    (:require [mid-fruits.css                    :as css]
              [plugins.item-lister.items.helpers :as items.helpers]
              [x.app-components.api              :as components]
              [x.app-core.api                    :as a]
              [x.app-elements.api                :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-header-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:colors (strings in vector)(opt)}
  [_ _ {:keys [header]}]
  (if-let [colors (:colors header)]
          [:div.x-list-item-a--header-colors [elements/color-stamp {:colors colors :size :l}]]))

(defn- list-item-header-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:icon (keyword)(opt)}}
  [_ _ {:keys [header]}]
  (if-let [icon (:icon header)]
          [:div.x-list-item-a--header-icon [elements/icon {:icon icon}]]))

(defn- list-item-header-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:thumbnail (string)(opt)}}
  [_ _ {:keys [header]}]
  (if-let [thumbnail (:thumbnail header)]
          [:div.x-list-item-a--header-thumbnail [elements/thumbnail {:size :s :uri thumbnail}]]))

(defn- list-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  [lister-id item-dex item-props]
  [:div.x-list-item-a--header [list-item-header-colors    lister-id item-dex item-props]
                              [list-item-header-icon      lister-id item-dex item-props]
                              [list-item-header-thumbnail lister-id item-dex item-props]])

(defn- list-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)
  ;   :timestamp (string)(opt)}
  [lister-id item-dex {:keys [label description timestamp] :as item-props}]
  [:div.x-list-item-a--details                 [:div.x-list-item-a--label       (components/content label)]
                               (if timestamp   [:div.x-list-item-a--timestamp  @(a/subscribe [:activities/get-actual-timestamp timestamp])])
                               (if description [:div.x-list-item-a--description (components/content description)])])

(defn- list-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:icon (keyword)(opt)}
  [lister-id _ {:keys [icon]}]
  (if icon (if-let [select-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :select-mode?])]
                   [:div.x-list-item-a--icon [elements/icon {:icon icon :color :highlight}]]
                   [:div.x-list-item-a--icon [elements/icon {:icon icon}]])))

(defn- list-item-selection-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  [lister-id item-dex item-props]
  (if-let [select-mode? @(a/subscribe [:item-lister/get-meta-item lister-id :select-mode?])]
          (if-let [item-selected? @(a/subscribe [:item-lister/item-selected? lister-id item-dex])]
                  [:div.x-list-item-a--selection-icon [elements/icon {:icon :check_circle :color :primary}]]
                  [:div.x-list-item-a--selection-icon [elements/icon {:icon :radio_button_unchecked}]])))

(defn- list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  [lister-id item-dex item-props]
  [:div.x-list-item-a (items.helpers/list-item-structure-attributes lister-id item-dex item-props)
                      [list-item-selection-icon                     lister-id item-dex item-props]
                      [list-item-header                             lister-id item-dex item-props]
                      [list-item-details                            lister-id item-dex item-props]
                      [list-item-icon                               lister-id item-dex item-props]])

(defn- list-item-toggle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {}
  [lister-id item-dex {:keys [on-right-click] :as item-props}]
  (let [lister-disabled? @(a/subscribe [:item-lister/lister-disabled? lister-id])
        item-disabled?   @(a/subscribe [:item-lister/item-disabled?   lister-id item-dex])
        on-click          [:item-lister/item-clicked lister-id item-dex item-props]]
       [elements/toggle {:content        [list-item-structure lister-id item-dex item-props]
                         :disabled?      (or item-disabled?) ; lister-disabled?
                         :on-click       on-click
                         :on-right-click on-right-click}]))

(defn list-item
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :description (metamorphic-content)(opt)
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)(opt)
  ;   :on-right-click (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(opt)
  ;   :style (map)(opt)
  ;   :header (map)
  ;    {:colors (strings in vector)
  ;     :icon (keyword)(opt)
  ;     :icon-family (keyword)(opt)
  ;      :material-icons-filled, :material-icons-outlined
  ;      Default: :material-icons-filled
  ;     :thumbnail (string)(opt)}
  ;   :timestamp (string)(opt)}
  ;
  ; @usage
  ;  [item-lister/list-item :my-lister 0 {...}]
  [lister-id item-dex item-props]
  [list-item-toggle lister-id item-dex item-props])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-item
  [])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-item
  [])
