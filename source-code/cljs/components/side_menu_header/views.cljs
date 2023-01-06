
(ns components.side-menu-header.views
    (:require [components.side-menu-button.views      :as side-menu-button.views]
              [components.side-menu-header.prototypes :as side-menu-header.prototypes]
              [elements.api                           :as elements]
              [random.api                             :as random]
              [re-frame.api                           :as r]
              [string.api                             :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-home-button
  ; @param (keyword) header-id
  ; @param (map) header-props
  [_ _]
  (let [app-title @(r/subscribe [:x.core/get-app-config-item :app-title])]
       [side-menu-button.views/component ::app-home-button
                                         {:font-weight :extra-bold
                                          :icon        :polymer
                                          :icon-color "var(--soft-blue-xx-dark)"
                                          :label (string/to-uppercase app-title)
                                          :on-click []
                                          :outdent {:top :xs}}]))

(defn- side-menu-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  [:<> [elements/horizontal-separator {:height :xs}]
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
   (let [] ; header-props (side-menu-header.prototypes/header-props-prototype header-props)
        [side-menu-header header-id header-props])))
