
(ns components.vector-item-controls.views
    (:require [components.vector-item-controls.prototypes :as vector-item-controls.prototypes]
              [fruits.random.api                          :as random]
              [fruits.vector.api                          :as vector]
              [pretty-elements.api                        :as pretty-elements]
              [re-frame.api                               :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-icon-button
  ; @ignore
  ;
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :on-change (Re-Frame metamorphic-event)(opt)
  ;  :tooltip-position (keyword)
  ;  :value-path (Re-Frame path vector)}
  [_ {:keys [disabled? item-dex on-change tooltip-position value-path]}]
  (let [duplicate-event [:update-item! value-path vector/duplicate-nth-item item-dex]]
       [pretty-elements/icon-button {:border-radius    {:all :s}
                                     :disabled?        disabled?
                                     :hover-color      :highlight
                                     :icon             :content_copy
                                     :on-click         {:dispatch-n [duplicate-event on-change]}
                                     :tooltip-content  :duplicate!
                                     :tooltip-position tooltip-position}]))

(defn- remove-icon-button
  ; @ignore
  ;
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :on-change (Re-Frame metamorphic-event)(opt)
  ;  :tooltip-position (keyword)
  ;  :value-path (Re-Frame path vector)}
  [_ {:keys [disabled? item-dex on-change tooltip-position value-path]}]
  (let [remove-event [:update-item! value-path vector/remove-nth-item item-dex]]
       [pretty-elements/icon-button {:border-radius    {:all :s}
                                     :disabled?        disabled?
                                     :hover-color      :highlight
                                     :icon             :close
                                     :on-click         {:dispatch-n [remove-event on-change]}
                                     :tooltip-content  :remove!
                                     :tooltip-position tooltip-position}]))

(defn- move-down-icon-button
  ; @ignore
  ;
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :on-change (Re-Frame metamorphic-event)(opt)
  ;  :tooltip-position (keyword)
  ;  :value-path (Re-Frame path vector)}
  [_ {:keys [disabled? item-dex on-change tooltip-position value-path]}]
  (let [move-event    [:update-item! value-path vector/move-nth-item-fwd item-dex]
        single-item? @(r/subscribe [:get-updated-item value-path vector/item-count? 1])]
       [pretty-elements/icon-button {:border-radius    {:all :s}
                                     :disabled?        (or disabled? single-item?)
                                     :hover-color      :highlight
                                     :icon             :arrow_drop_down
                                     :on-click         {:dispatch-n [move-event on-change]}
                                     :tooltip-content  :move-down!
                                     :tooltip-position tooltip-position}]))

(defn- move-up-icon-button
  ; @ignore
  ;
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :on-change (Re-Frame metamorphic-event)(opt)
  ;  :tooltip-position (keyword)
  ;  :value-path (Re-Frame path vector)}
  [_ {:keys [disabled? item-dex on-change tooltip-position value-path]}]
  (let [move-event [:update-item! value-path vector/move-nth-item-bwd item-dex]
        single-item? @(r/subscribe [:get-updated-item value-path vector/item-count? 1])]
       [pretty-elements/icon-button {:border-radius    {:all :s}
                                     :disabled?        (or disabled? single-item?)
                                     :hover-color      :highlight
                                     :icon             :arrow_drop_up
                                     :on-click         {:dispatch-n [move-event on-change]}
                                     :tooltip-content  :move-up!
                                     :tooltip-position tooltip-position}]))

(defn- vector-item-controls
  ; @ignore
  ;
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  [controls-id controls-props]
  [:div {:class :c-vector-item-controls}
        [:div {:class :c-vector-item-controls--inner}
              [move-up-icon-button   controls-id controls-props]
              [move-down-icon-button controls-id controls-props]
              [duplicate-icon-button controls-id controls-props]
              [remove-icon-button    controls-id controls-props]]])

(defn view
  ; @param (keyword)(opt) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :on-change (Re-Frame metamorphic-event)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;   Default: :right
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @usage
  ; [vector-item-controls {...}]
  ;
  ; @usage
  ; [vector-item-controls :my-vector-item-controls {...}]
  ([controls-props]
   [view (random/generate-keyword) controls-props])

  ([controls-id controls-props]
   ; @note (tutorials#parameterizing)
   (fn [_ controls-props]
       (let [controls-props (vector-item-controls.prototypes/controls-props-prototype controls-props)]
            [vector-item-controls controls-id controls-props]))))
