
(ns pretty-inputs.value-group.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [fruits.vector.api                   :as vector]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-elements.api                 :as pretty-elements]
              [pretty-inputs.value-group.attributes :as value-group.attributes]
              [pretty-inputs.value-group.prototypes :as value-group.prototypes]
              [pretty-inputs.engine.api            :as pretty-inputs.engine]
              [pretty-inputs.header.views          :as header.views]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-group-chip
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) chip-dex
  ; @param (*) chip-value
  [group-id group-props chip-dex chip-value]
  (let [chip-props (value-group.prototypes/value-props-prototype group-id group-props chip-dex chip-value)]
       [pretty-elements/chip chip-props]))

(defn- chip-group-chip-list
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:chips-placeholder (multitype-content)(opt)}
  [group-id {:keys [chips-placeholder] :as group-props}]
  (letfn [(f0 [chip-dex chip-value] [chip-group-chip group-id group-props chip-dex chip-value])]
         (let [chips (pretty-inputs.engine/get-input-displayed-value group-id group-props)]
              (cond (-> chips vector/not-empty?) (hiccup/put-with-indexed [:<>] chips f0)
                    (-> chips-placeholder) [:div (value-group.attributes/value-group-values-placeholder-attributes group-id group-props)
                                                 (metamorphic-content/compose chips-placeholder)]))))

(defn- chip-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div (value-group.attributes/value-group-attributes group-id group-props)
        [pretty-inputs.header.views/view             group-id group-props]
        [pretty-inputs.engine/input-synchronizer     group-id group-props]
        [:div (value-group.attributes/value-group-inner-attributes group-id group-props)
              [chip-group-chip-list                              group-id group-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    group-id group-props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount group-id group-props))
                         :reagent-render         (fn [_ group-props] [chip-group group-id group-props])}))

(defn view
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :chip-default (map)(opt)
  ;  :chip-label-f (function)(opt)
  ;  :chips-deletable? (boolean)(opt)
  ;  :chips-placeholder (multitype-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :get-value-f (function)(opt)
  ;  :helper (multitype-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info (multitype-content)(opt)
  ;  :initial-value (vector)(opt)
  ;  :label (multitype-content)(opt)
  ;  :on-changed-f (function)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :on-unselected-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :projected-value (vector)(opt)
  ;  :set-value-f (function)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [chip-group {...}]
  ;
  ; @usage
  ; [chip-group :my-chip-group {...}]
  ([group-props]
   [view (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parameterizing)
   (fn [_ group-props]
       (let [group-props (pretty-presets.engine/apply-preset          group-id group-props)
             group-props (value-group.prototypes/group-props-prototype group-id group-props)]
             ;group-props (pretty-elements.engine/apply-element-item-default     group-id group-props :chips :chip-default)
             ;group-props (pretty-elements.engine/inherit-element-disabled-state group-id group-props :chips :chip-default)
            [view-lifecycles group-id group-props]))))
