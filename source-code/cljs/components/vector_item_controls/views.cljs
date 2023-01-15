
(ns components.vector-item-controls.views
    (:require [components.vector-item-controls.prototypes :as vector-item-controls.prototypes]
              [elements.api                               :as elements]
              [random.api                                 :as random]
              [vector.api                                 :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-icon-button
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :tooltip-position (keyword)
  ;  :value-path (vector)}
  [_ {:keys [disabled? item-dex tooltip-position value-path]}]
  [elements/icon-button {:border-radius    :s
                         :disabled?        disabled?
                         :hover-color      :highlight
                         :icon             :content_copy
                         :on-click         [:x.db/apply-item! value-path vector/duplicate-nth-item item-dex]
                         :tooltip          :duplicate!
                         :tooltip-position tooltip-position}])

(defn- remove-icon-button
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :tooltip-position (keyword)
  ;  :value-path (vector)}
  [_ {:keys [disabled? item-dex tooltip-position value-path]}]
  [elements/icon-button {:border-radius    :s
                         :disabled?        disabled?
                         :hover-color      :highlight
                         :icon             :close
                         :on-click         [:x.db/apply-item! value-path vector/remove-nth-item item-dex]
                         :tooltip          :remove!
                         :tooltip-position tooltip-position}])

(defn- move-down-icon-button
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :tooltip-position (keyword)
  ;  :value-path (vector)}
  [_ {:keys [disabled? item-dex tooltip-position value-path]}]
  [elements/icon-button {:border-radius    :s
                         :disabled?        disabled?
                         :hover-color      :highlight
                         :icon             :arrow_drop_down
                         :on-click         [:x.db/apply-item! value-path vector/move-nth-item-bwd item-dex]
                         :tooltip          :move-down!
                         :tooltip-position tooltip-position}])

(defn- move-up-icon-button
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :tooltip-position (keyword)
  ;  :value-path (vector)}
  [_ {:keys [disabled? item-dex tooltip-position value-path]}]
  [elements/icon-button {:border-radius    :s
                         :disabled?        disabled?
                         :hover-color      :highlight
                         :icon             :arrow_drop_up
                         :on-click         [:x.db/apply-item! value-path vector/move-nth-item-fwd item-dex]
                         :tooltip          :move-up!
                         :tooltip-position tooltip-position}])

(defn- vector-item-controls-body
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  [controls-id controls-props]
  [:div.c-vector-item-controls--body [move-up-icon-button   controls-id controls-props]
                                     [move-down-icon-button controls-id controls-props]
                                     [duplicate-icon-button controls-id controls-props]
                                     [remove-icon-button    controls-id controls-props]])

(defn- vector-item-controls
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  [controls-id controls-props]
  [:div.c-vector-item-controls [vector-item-controls-body controls-id controls-props]])

(defn component
  ; @param (keyword)(opt) controls-id
  ; @param (map) controls-props
  ; {:disabled? (boolean)(opt)
  ;  :item-dex (integer)
  ;  :tooltip-position (keyword)(opt)
  ;   :left, :right
  ;   Default: :right
  ;  :value-path (vector)}
  ;
  ; @usage
  ; [vector-item-controls {...}]
  ;
  ; @usage
  ; [vector-item-controls :my-vector-item-controls {...}]
  ([controls-props]
   [component (random/generate-keyword) controls-props])

  ([controls-id controls-props]
   (let [controls-props (vector-item-controls.prototypes/controls-props-prototype controls-props)]
        [vector-item-controls controls-id controls-props])))
