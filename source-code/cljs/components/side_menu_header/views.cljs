
(ns components.side-menu-header.views
    (:require [components.side-menu-header.prototypes :as side-menu-header.prototypes]
              [pretty-elements.api                    :as pretty-elements]
              [pretty-elements.api                    :as pretty-elements]
              [random.api                             :as random]
              [re-frame.api                           :as r]
              [string.api                             :as string]))

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

(defn component
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
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props] ; XXX#0106 (README.md#parametering)
       (let [] ; header-props (side-menu-header.prototypes/header-props-prototype header-props)
            [side-menu-header header-id header-props]))))
