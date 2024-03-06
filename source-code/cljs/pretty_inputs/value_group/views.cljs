
(ns pretty-inputs.value-group.views
    (:require [fruits.hiccup.api                    :as hiccup]
              [fruits.random.api                    :as random]
              [fruits.vector.api                    :as vector]
              [multitype-content.api                :as multitype-content]
              [pretty-elements.api                  :as pretty-elements]
              [pretty-inputs.engine.api             :as pretty-inputs.engine]
              [pretty-inputs.header.views           :as header.views]
              [pretty-inputs.value-group.attributes :as value-group.attributes]
              [pretty-inputs.value-group.prototypes :as value-group.prototypes]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [reagent.core                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-group-chip
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) props
  ; @param (integer) chip-dex
  ; @param (*) chip-value
  [input-id props chip-dex chip-value]
  (let [chip-props (value-group.prototypes/value-props-prototype input-id props chip-dex chip-value)]
       [pretty-elements/chip chip-props]))

(defn- chip-group-chip-list
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) props
  ; {:chips-placeholder (multitype-content)(opt)}
  [group-id {:keys [chips-placeholder] :as group-props}])
  ;(letfn [(f0 [chip-dex chip-value] [chip-group-chip input-id props chip-dex chip-value])]))
         ;(let [chips (pretty-inputs.engine/get-input-displayed-value input-id props)]
          ;    (cond (-> chips vector/not-empty?) (hiccup/put-with-indexed [:<>] chips f0)))]))
                    ;(-> chips-placeholder) [:div (value-group.attributes/value-group-values-placeholder-attributes input-id props)
                    ;                             (multitype-content/compose chips-placeholder)))))

(defn- chip-group
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) props
  [input-id props]
  [:div (value-group.attributes/outer-attributes input-id props)
        [pretty-inputs.header.views/view             input-id props]
        [pretty-inputs.engine/input-synchronizer     input-id props]
        [:div (value-group.attributes/inner-attributes input-id props)
              [chip-group-chip-list                              input-id props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) props
  [input-id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    input-id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount input-id props))
                         :reagent-render         (fn [_ props] [chip-group input-id props])}))

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
  ([props]
   [view (random/generate-keyword) props])

  ([input-id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset           input-id props)
             props (value-group.prototypes/props-prototype input-id props)]
             ;group-props (pretty-elements.engine/apply-element-item-default     input-id props :chips :chip-default)
             ;group-props (pretty-elements.engine/inherit-element-disabled-state input-id props :chips :chip-default)
            (if (:mounted? props)
                [view-lifecycles input-id props])))))
