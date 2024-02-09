
(ns pretty-elements.adornment-group.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [pretty-elements.adornment-group.attributes :as adornment-group.attributes]
              [pretty-elements.adornment-group.prototypes :as adornment-group.prototypes]
              [pretty-elements.adornment.views            :as adornment.views]
              [pretty-elements.engine.api                 :as pretty-elements.engine]
              [pretty-presets.engine.api                  :as pretty-presets.engine]
              [reagent.api                                :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-group-adornment
  ; @ignore
  ;
  ; @param (integer) adornment-dex
  ; @param (map) adornment-props
  [adornment-dex adornment-props]
  (let [adornment-props (adornment-group.prototypes/adornment-props-prototype adornment-dex adornment-props)]
       [adornment.views/view adornment-props]))

(defn- adornment-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:adornments (maps in vector)(opt)}
  [group-id {:keys [adornments] :as group-props}]
  [:div (adornment-group.attributes/group-attributes group-id group-props)
        [:div (adornment-group.attributes/group-body-attributes group-id group-props)
              (letfn [(f0 [adornment-dex adornment-props] [adornment-group-adornment adornment-dex adornment-props])]
                     (hiccup/put-with-indexed [:<>] adornments f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    group-id group-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount group-id group-props))
                       :reagent-render         (fn [_ group-props] [adornment-group group-id group-props])}))

(defn view
  ; @description
  ; Group of downsized button elements.
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:adornment-default (map)(opt)
  ;  :adornments (maps in vector)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :orientation (keyword)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :overflow (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [adornment-group {...}]
  ;
  ; @usage
  ; [adornment-group :my-adornment-group {...}]
  ([group-props]
   [view (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parameterizing)
   (fn [_ group-props]
       (let [group-props (pretty-presets.engine/apply-preset                            group-id group-props)
             group-props (adornment-group.prototypes/group-props-prototype              group-id group-props)
             group-props (pretty-elements.engine/element-subitem-group<-subitem-default group-id group-props :adornments :adornment-default)
             group-props (pretty-elements.engine/element-subitem-group<-disabled-state  group-id group-props :adornments)
             group-props (pretty-elements.engine/leave-element-disabled-state           group-id group-props :adornments)]
            [view-lifecycles group-id group-props]))))
