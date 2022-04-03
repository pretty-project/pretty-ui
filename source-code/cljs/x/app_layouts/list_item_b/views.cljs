
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.list-item-b.views
    (:require [mid-fruits.css                    :as css]
              [x.app-components.api              :as components]
              [x.app-core.api                    :as a]
              [x.app-elements.api                :as elements]
              [x.app-layouts.list-item-b.helpers :as list-item-b.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-header-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:thumbnail (map)
  ;    {:icon (keyword)(opt)
  ;     :icon-family (keyword)(opt)}}
  [_ {:keys [thumbnail]}]
  (let [icon-props (select-keys thumbnail [:icon :icon-family])]
       [elements/icon icon-props]))

(defn- list-item-header-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:thumbnail (map)
  ;    {:uri (string)(opt)}}
  [_ {:keys [thumbnail]}]
  [:div.x-list-item-b--thumbnail {:style {:background-image (-> thumbnail :uri css/url)}}])

(defn- list-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:thumbnail (map)
  ;    {:icon (keyword)(opt)
  ;     :uri (string)(opt)}}
  [item-dex {:keys [thumbnail] :as item-props}]
  [:div.x-list-item-b--header (if (:icon thumbnail) [list-item-header-icon      item-dex item-props])
                              (if (:uri  thumbnail) [list-item-header-thumbnail item-dex item-props])])

(defn- list-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:label (metamorphic-content)
  ;   :size (string)(opt)
  ;   :timestamp (string)(opt)}
  [_ {:keys [label size timestamp]}]
  [:div.x-list-item-b--details [:div.x-list-item-b--label     (components/content label)]
                               [:div.x-list-item-b--timestamp (str timestamp)]
                               [:div.x-list-item-b--size      (str size)]])

(defn- list-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:icon (keyword)(opt)}
  [_ item-props]
  (let [icon-props (select-keys item-props [:icon :icon-family])]
       [:div.x-list-item-b--icon [elements/icon icon-props]]))

(defn- list-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  [:div.x-list-item-b (list-item-b.helpers/list-item-structure-attributes item-dex item-props)
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
  ;   :size (string)(opt)
  ;   :style (map)(opt)
  ;   :thumbnail (map)
  ;    {:icon (keyword)(opt)
  ;     :icon-family (keyword)(opt)
  ;      :material-icons-filled, :material-icons-outlined
  ;      Default: :material-icons-filled
  ;      Only w/ {:thumbnail {:icon ...}}
  ;     :uri (string)(opt)}
  ;   :timestamp (string)(opt)}
  ;
  ; @usage
  ;  [layouts/list-item-b {...}]
  ;
  ; @usage
  ;  [layouts/list-item-b 0 {...}]
  ([item-props]
   [list-item 0 item-props])

  ([item-dex {:keys [on-click] :as item-props}]
   (if on-click [toggle-list-item item-dex item-props]
                [static-list-item item-dex item-props])))
