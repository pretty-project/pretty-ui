
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
  [:div.x-list-item-a--header-colors [elements/color-stamp {:colors (:colors header) :size :l}]])

(defn- list-item-header-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:icon (keyword)(opt)
  ;     :icon-family (keyword)(opt)}}
  [_ _ {:keys [header]}]
  (let [icon-props (select-keys header [:icon :icon-family])]
       [:div.x-list-item-a--header-icon [elements/icon icon-props]]))

(defn- list-item-header-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:thumbnail (string)(opt)}}
  [_ _ {:keys [header]}]
  [:div.x-list-item-a--header-thumbnail [elements/thumbnail {:size :s :uri (:thumbnail header)}]])

(defn- list-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:header (map)
  ;    {:colors (strings in vector)(opt)
  ;     :icon (keyword)(opt)
  ;     :thumbnail (string)(opt)}}
  [lister-id item-dex {:keys [header] :as item-props}]
  [:div.x-list-item-a--header (if (:colors    header) [list-item-header-colors    lister-id item-dex item-props])
                              (if (:icon      header) [list-item-header-icon      lister-id item-dex item-props])
                              (if (:thumbnail header) [list-item-header-thumbnail lister-id item-dex item-props])])

(defn- list-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)
  ;   :timestamp (string)(opt)}
  [_ _ {:keys [label description timestamp]}]
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
  [_ _ item-props]
  (let [icon-props (select-keys item-props [:icon :icon-family])]
       [:div.x-list-item-a--icon [elements/icon icon-props]]))

(defn- list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  [lister-id item-dex item-props]
  [:div.x-list-item-a (items.helpers/list-item-structure-attributes lister-id item-dex item-props)
                      [list-item-header                             lister-id item-dex item-props]
                      [list-item-details                            lister-id item-dex item-props]
                      [list-item-icon                               lister-id item-dex item-props]])

(defn toggle-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-click (metamorphic-event)
  ;   :on-right-click (metamorphic-event)(opt)}
  [lister-id item-dex {:keys [disabled? on-click on-right-click] :as item-props}]
  (let [on-click [:item-lister/item-clicked lister-id item-dex {:on-click on-click}]]
       [elements/toggle {:content        [list-item-structure lister-id item-dex item-props]
                         :disabled?      disabled?
                         :on-click       on-click
                         :on-right-click on-right-click
                         :hover-color    :highlight}]))

(defn static-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  [lister-id item-dex item-props]
  [list-item-structure lister-id item-dex item-props])

(defn list-item
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
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
  ;   :selected? (boolean)(opt)
  ;    Default: false
  ;   :timestamp (string)(opt)}
  ;
  ; @usage
  ;  [item-lister/list-item :my-lister 0 {...}]
  [lister-id item-dex {:keys [on-click] :as item-props}]
  (if on-click [toggle-list-item lister-id item-dex item-props]
               [static-list-item lister-id item-dex item-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-item
  [])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-item
  [])
