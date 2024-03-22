
(ns components.side-menu-header.views
    (:require [components.side-menu-header.prototypes :as side-menu-header.prototypes]
              [fruits.random.api                      :as random]
              [fruits.string.api                      :as string]
              [pretty-elements.api                    :as pretty-elements]
              [pretty-elements.api                    :as pretty-elements]
              [re-frame.extra.api                     :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-home-button
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [_ _]
  (let [app-title @(r/subscribe [:x.core/get-app-config-item :app-title])]
       [pretty-elements/button ::app-home-button
                               {:font-weight :semi-bold
                                :icon        :cloud
                                :icon-color  "#0aaaa0"
                                :label (string/to-uppercase app-title)
                                :on-click []
                                :preset :default-presets/side-menu-button}]))

(defn- side-menu-header
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  [:<> [pretty-elements/horizontal-separator {:height :xs}]
       [app-home-button header-id header-props]])

(defn view
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {}
  ;
  ; @usage
  ; [side-menu-header {...}]
  ;
  ; @usage
  ; [side-menu-header :my-side-menu-header {...}]
  ([header-props]
   [view (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parameterizing)
   (fn [_ header-props]
       (let [] ; header-props (side-menu-header.prototypes/header-props-prototype header-props)
            [side-menu-header header-id header-props]))))
